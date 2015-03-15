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
package net.imagej.ops.features.lbp;

import java.util.Arrays;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * Simple implementation of local binary patterns in 3d
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
@Plugin(type = Op.class)
public class LocalBinaryPatterns3D implements OutputOp<double[]> {

	@Parameter
	private IterableInterval<? extends RealType<?>> ii;

	// width of image in iterable interval
	private int width;

	// height of image in iterable interval
	private int height;

	// depth of image in iterable interval
	private int depth;

	// array to store iterable interval
	private int[][][] img;

	private double[] output;

	@Override
	public void run() {

		// HyperSphereShape shape = new HyperSphereShape(1);
		// shape.neighborhoods(ii);
		// NeighborhoodsIterableInterval<T> ni = new
		// NeighborhoodsIterableInterval(ii, 1, shape.neighborhoods(ii));

		img = writeIterableIntervalToArray();

		int[] values = new int[] { 1, 2, 4, 8, 16, 32 };
		double[] hist = new double[64];
		Arrays.fill(hist, 0d);

		// iterate over all pixels, iterate over its neighborhood in the same
		// direction
		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				for (int z = 1; z < depth - 1; z++) {
					int center = img[x][y][z];

					if (center == Integer.MAX_VALUE)
						continue;

					int bin = 0;
					
					if (img[x-1][y][z] > center) bin += 1;
					if (img[x+1][y][z] > center) bin += 2;
					if (img[x][y-1][z] > center) bin += 4;
					if (img[x][y+1][z] > center) bin += 8;
					if (img[x][y][z-1] > center) bin += 16;
					if (img[x][y][z+1] > center) bin += 32;
							
					
					hist[bin]++;
				}
			}
		}

		// normalize & quantize histogram.
		double max = 0;
		for (int i = 0; i < hist.length; i++) {
			max = Math.max(hist[i], max);
		}
		for (int i = 0; i < hist.length; i++) {
			hist[i] = Math.floor((hist[i] / max) * 31);
		}

		setOutput(hist);
	}

	/**
	 * 
	 * @return
	 */
	private int[][][] writeIterableIntervalToArray() {
		int dimX = -1;
		int dimY = -1;
		int dimZ = -1;

		for (int d = 0; d < ii.numDimensions(); d++) {
			if (ii.dimension(d) > 1) {
				if (dimX == -1) {
					dimX = d;
				} else if (dimY == -1) {
					dimY = d;
				} else {
					dimZ = d;
					break;
				}
			}
		}

		width = (int) ii.dimension(dimX);
		height = (int) ii.dimension(dimY);
		depth = (int) ii.dimension(dimZ);

		final Cursor<? extends RealType<?>> cursor = ii.cursor();

		int[][][] pixels = new int[(int) ii.dimension(dimX)][(int) ii
				.dimension(dimY)][(int) ii.dimension(dimZ)];

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				Arrays.fill(pixels[i][j], Integer.MAX_VALUE);
			}
		}

		while (cursor.hasNext()) {
			cursor.fwd();
			pixels[cursor.getIntPosition(dimY) - (int) ii.min(dimY)][cursor
					.getIntPosition(dimX) - (int) ii.min(dimX)][cursor
					.getIntPosition(dimZ) - (int) ii.min(dimZ)] = (int) (cursor
					.get().getRealDouble());
		}

		return pixels;
	}

	@Override
	public double[] getOutput() {
		return output;
	}

	@Override
	public void setOutput(double[] _output) {
		output = _output;
	}
}
