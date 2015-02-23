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
package net.imagej.ops.features.tamura;

import java.util.Arrays;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix.MatrixOrientation;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * Class to compute three Tamura Texture Features: Coarsness, Contrast and
 * Directionality. Implementation is based on the paper
 * "Extension of Tamura Texture Features for 3D Fluroscence Microscopy" by
 * Majtner et. al. and the implementation of Lire (http://www.lire-project.net)
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
@Plugin(type = Op.class)
public class Tamura2DComputer implements Contingent {

	// input image
	@Parameter
	private IterableInterval<? extends RealType<?>> ii;

	// output array with all three computed features
	@Parameter(type = ItemIO.OUTPUT)
	private double[] output;

	/**
	 * Constructor
	 */
	public Tamura2DComputer() {
		// convert iterarble interval to array
		int[][] img = writeImageToArray();
		
	}
	
	/**
	 * 
	 * @return
	 */
	private int[][] writeImageToArray() {
		int dimX = -1;
		int dimY = -1;

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

		final int[][] pixels = new int[(int) ii.dimension(dimX)][(int) ii
				.dimension(dimY)];

		for (int i = 0; i < pixels.length; i++) {
			Arrays.fill(pixels[i], Integer.MAX_VALUE);
		}

		while (cursor.hasNext()) {
			cursor.fwd();
			pixels[cursor.getIntPosition(dimY) - (int) ii.min(dimY)][cursor
					.getIntPosition(dimX) - (int) ii.min(dimX)] = (int) (cursor
					.get().getRealDouble());
		}
		
		return pixels;
	}
	
	@Override
	public boolean conforms() {
		if (ii.numDimensions() == 2)
			return true;
		return false;
	}

}
