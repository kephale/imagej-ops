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
package net.imagej.ops.features.provider;

import java.util.Arrays;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

import net.imagej.ops.Contingent;
import net.imagej.ops.OutputOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

/**
 * 
 * Write an iterable interval in an 2d int array
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
public class IterableIntervalTo3dArrayProvider implements OutputOp<int[][][]>, Contingent {

	@Parameter
	private IterableInterval<? extends RealType<?>> ii;
	
	@Parameter(type = ItemIO.OUTPUT)
	private int[][] output;
	
	@Override
	public void run() {
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
		
		setOutput(pixels);
	}

	@Override
	public boolean conforms() {
		// check if iterable interval has dimension of 2.
		// But Iterable interval is null...
		return true;
	}

	@Override
	public int[][][] getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOutput(int[][][] output) {
		// TODO Auto-generated method stub
		
	}

}
