/*
 * #%L
 * SciJava OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2013 Board of Regents of the University of
 * Wisconsin-Madison, and University of Konstanz.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */
package net.imagej.ops.features.tamura.helper;

import java.util.Arrays;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops.Map;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment4AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.VarianceFeature;
import net.imagej.ops.statistics.FirstOrderOps.Mean;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.localneighborhood.RectangleShape;
import net.imglib2.algorithm.region.localneighborhood.Shape;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * Class to compute three Tamura Texture Features: Coarsness, Contrast and
 * Directionality. Implementation is based on the paper
 * "Extension of Tamura Texture Features for 3D Fluroscence Microscopy" by
 * Majtner et. al. and the tamura implementation of Lire
 * (http://www.lire-project.net)
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
@Plugin(type = Op.class)
public class TamuraTexture2DComputer implements TamuraTextureComputer,
		Contingent {

	@Parameter
	private IterableInterval<? extends RealType<?>> ii;
	
	//@Parameter
	//private RandomAccessibleInterval<? extends RealType<?>> ra;

	@Parameter
	private MeanFeature<? extends RealType<?>> my;

	@Parameter
	private VarianceFeature<? extends RealType<?>> var;

	@Parameter
	private Moment4AboutMeanFeature<? extends RealType<?>> m4;

	@Parameter
	private OpService ops;

	// Wite output to TamuraFeatures Class
	@Parameter(type = ItemIO.OUTPUT)
	private TamuraFeatures output;

	// filter matrix for sobel in horizontal direction
	private static final double[][] filterH = { { -1, 0, 1 }, { -1, 0, 1 },
			{ -1, 0, 1 } };
	// filter matrix for sobel in vertical direction
	private static final double[][] filterV = { { -1, -1, -1 }, { 0, 0, 0 },
			{ 1, 1, 1 } };

	// width of image in iterable interval
	private int width;
	
	// height of image in iterable interval
	private int height;

	// array to store iterable interval
	private int[][] img;

	@Override
	public void run() {

		// create output
		if (output == null) {
			output = new TamuraFeatures();
		}
		
		//Shape rect = new RectangleShape(3, false);
		//ops.run(Map.class, rect.neighborhoodsSafe(ra), Mean.class);
		
		// convert iterarble interval to array
		img = writeIterableIntervalToArray();

		// set output
		output.setDirectionality(directionality());
		output.setContrast(contrast());
		output.setCoarsness((coarseness()));
	}

	/**
	 * @return
	 */
	public double coarseness() {
		double result = 0;
		for (int i = 1; i < width - 1; i++) {
			for (int j = 1; j < height - 1; j++) {
				result = result + Math.pow(2, this.sizeLeadDiffValue(i, j));
			}
		}

		result = (1.0 / (width * height)) * result;
		return result;
	}

	/**
	 * 1. For every point(x, y) calculate the average over neighborhoods. 
	 * TODO: Replace with OP!!!
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public double averageOverNeighborhoods(int x, int y, int k) {
		double result = 0, border;
		border = Math.pow(2, 2 * k);
		int x0 = 0, y0 = 0;

		int count = 0;
		for (int i = 0; i < border; i++) {
			for (int j = 0; j < border; j++) {
				x0 = x - (int) Math.pow(2, k - 1) + i;
				y0 = y - (int) Math.pow(2, k - 1) + j;

				// check image borders
				if (x0 < 0)
					x0 = 0;
				if (y0 < 0)
					y0 = 0;
				if (x0 >= width)
					x0 = width - 1;
				if (y0 >= height)
					y0 = height - 1;

				// check if position is in ROI
				if (img[x0][y0] == Integer.MAX_VALUE)
					continue;

				count++;
				result = result + img[x0][y0];
			}
		}
		result = (1 / count) * result;
		return result;
	}

	/**
	 * 2. For every point (x, y) calculate differences between the not
	 * overlapping neighborhoods on opposite sides of the point in horizontal
	 * direction.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public double differencesBetweenNeighborhoodsHorizontal(int x, int y, int k) {
		double result = 0;
		result = Math.abs(this.averageOverNeighborhoods(
				x + (int) Math.pow(2, k - 1), y, k)
				- this.averageOverNeighborhoods(x - (int) Math.pow(2, k - 1),
						y, k));
		return result;
	}

	/**
	 * 2. For every point (x, y) calculate differences between the not
	 * overlapping neighborhoods on opposite sides of the point in vertical
	 * direction.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public double differencesBetweenNeighborhoodsVertical(int x, int y, int k) {
		double result = 0;
		result = Math.abs(this.averageOverNeighborhoods(x,
				y + (int) Math.pow(2, k - 1), k)
				- this.averageOverNeighborhoods(x,
						y - (int) Math.pow(2, k - 1), k));
		return result;
	}

	/**
	 * 3. At each point (x, y) select the size leading to the highest difference
	 * value.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public int sizeLeadDiffValue(int x, int y) {
		double result = 0, tmp;
		int maxK = 1;

		for (int k = 0; k < 5; k++) {
			tmp = Math.max(this.differencesBetweenNeighborhoodsHorizontal(x, y,
					k), this.differencesBetweenNeighborhoodsVertical(x, y,
					k));
			if (result < tmp) {
				maxK = k;
				result = tmp;
			}
		}
		return maxK;
	}

	/**
	 * Compute tamura contrast feature
	 * 
	 * @return contrast feature
	 */
	public double contrast() {
		final double l4 = m4.getOutput().getRealDouble()
				/ (Math.pow(var.getOutput().getRealDouble(), 2));
		return Math.sqrt(var.getOutput().getRealDouble()) / Math.pow(l4, 0.25);
	}

	/**
	 * Compute tamura directionality
	 * 
	 * @return directionality as histogram
	 */
	public double[] directionality() {
		// TODO make histogram size user defineable
		double[] histogram = new double[16];
		double maxResult = 3;
		double binWindow = maxResult / (double) (histogram.length - 1);
		int bin = -1;
		for (int x = 1; x < img.length - 1; x++) {
			for (int y = 1; y < img[x].length - 1; y++) {
				bin = (int) ((Math.PI / 2 + Math.atan(this.calculateDeltaV(x,
						y) / this.calculateDeltaH(x, y, img))) / binWindow);
				histogram[bin]++;
			}
		}
		return histogram;
	}

	/**
	 * @return
	 */
	public double calculateDeltaH(int x, int y, int[][] img) {
		double result = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				result = result + img[x - 1 + i][y - 1 + j] * filterH[i][j];
			}
		}
		return result;
	}

	/**
	 * @return
	 */
	public double calculateDeltaV(int x, int y) {
		double result = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				result = result + img[x - 1 + i][y - 1 + j] * filterV[i][j];
			}
		}
		return result;
	}

	/**
	 * Write iterable interval in int array
	 * 
	 * @return integer pixel array
	 */
	private int[][] writeIterableIntervalToArray() {
		int dimX = -1;
		int dimY = -1;

		// select dimensions
		for (int d = 0; d < ii.numDimensions(); d++) {
			if (ii.dimension(d) > 1) {
				if (dimX == -1) {
					dimX = d;
				} else {
					dimY = d;
					break;
				}
			}
		}
		final Cursor<? extends RealType<?>> cursor = ii.cursor();

		width = (int) ii.dimension(dimX);
		height = (int) ii.dimension(dimY);

		// create array
		int[][] pixels = new int[width][height];

		// fill array with integer max value to be able to seperate between back
		// and foreground
		for (int i = 0; i < pixels.length; i++) {
			Arrays.fill(pixels[i], Integer.MAX_VALUE);
		}

		// fill array with values from iterable interval
		while (cursor.hasNext()) {
			cursor.fwd();
			pixels[cursor.getIntPosition(dimY) - (int) ii.min(dimY)][cursor
					.getIntPosition(dimX) - (int) ii.min(dimX)] = (int) (cursor
					.get().getRealDouble());
		}

		// return pixel array
		return pixels;
	}

	@Override
	public boolean conforms() {
		// always true.
		return true;
	}

	@Override
	public TamuraFeatures getOutput() {
		return output;
	}

	@Override
	public void setOutput(TamuraFeatures _output) {
		output = _output;
	}

}
