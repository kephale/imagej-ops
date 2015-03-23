/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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
 * #L%
 */
package net.imagej.ops.features.tamura;

import java.util.HashMap;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Op;
import net.imagej.ops.features.provider.IterableIntervalTo2dArrayProvider;
import net.imagej.ops.features.tamura.TamuraFeatures.Coarsness2dFeature;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * 
 * Default implementation of 2D Tamura Texture Feature Coarsness
 * 
 * @author Andreas Graumann, Univesity of Konstanz
 * @author Christian Dietz, Univesity of Konstanz
 *
 */
@Plugin(type = Op.class, name = "Tamura 2D: Coarsness")
public class DefaultCoarsness2dFeature implements Coarsness2dFeature<DoubleType> {
	
	@Parameter
	IterableIntervalTo2dArrayProvider imgProvider;
	
	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;
	
	// array to store iterable interval
	private int[][] img;
	
	// map to store all Mean images
	HashMap<Integer, double[][]> meanImgs;
	
	// width of image in iterable interval
	private int width;

	// height of image in iterable interval
	private int height;


	@Override
	public DoubleType getOutput() {
		return out;
	}

	@Override
	public void setOutput(DoubleType output) {
		out = output;
	}

	@Override
	public void run() {
		img = imgProvider.getOutput();
		width = img.length;
		height = img[0].length;
		
		setOutput(new DoubleType(coarseness()));
	}
	
	/**
	 * @return caorsness feature value
	 */
	public double coarseness() {

		meanImgs = new HashMap<Integer, double[][]>();
		for (int k = 1; k <= 3; k++) {
			meanImgs.put(new Integer(k), averageOverNeighborHoods(k));
		}

		double result = 0;
		int count = 0;
		for (int i = 1; i < width - 1; i++) {
			for (int j = 1; j < height - 1; j++) {
				if (img[i][j] != Integer.MAX_VALUE) {
					count++;
					int k = this.sizeLeadDiffValue(i, j);
					result += k;
				}
			}
		}
		result /= count;
		return result;
	}

	public double[][] averageOverNeighborHoods(int k) {
		double[][] meanImg = new double[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				meanImg[x][y] = averageOverNeighborhoods(x, y, k);
			}
		}

		return meanImg;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param k
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

		result = result / count;
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param k
	 * @return
	 */
	public double differencesBetweenNeighborhoodsHorizontal(int x, int y, int k) {
		double result = 0;
		double[][] i = meanImgs.get(k);

		int x0 = x + (int) Math.pow(2, k - 1);
		int x1 = x - (int) Math.pow(2, k - 1);

		if (x0 < width && x1 > 0)
			result = Math.abs(i[x0][y] - i[x1][y]);

		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param k
	 * @return
	 */
	public double differencesBetweenNeighborhoodsVertical(int x, int y, int k) {
		double result = 0;
		double[][] i = meanImgs.get(k);

		int y0 = y + (int) Math.pow(2, k - 1);
		int y1 = y - (int) Math.pow(2, k - 1);

		if (y0 < height && y1 > 0)
			result = Math.abs(i[x][y0] - i[x][y1]);
		
		return result;
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public int sizeLeadDiffValue(int x, int y) {
		double result = 0, tmp;
		int maxK = 1;

		for (int k = 1; k < meanImgs.size(); k++) {
			tmp = Math.max(
					this.differencesBetweenNeighborhoodsHorizontal(x, y, k),
					this.differencesBetweenNeighborhoodsVertical(x, y, k));
			if (result < tmp) {
				maxK = k;
				result = tmp;
			}
		}
		return maxK;
	}

}
