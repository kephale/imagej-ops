/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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
package net.imagej.ops.features.tamura2d;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Tamura2d;
import net.imagej.ops.Ops.Tamura2d.Directionality;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gradient.PartialDerivative;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

/**
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 * @param <I>
 * @param <O>
 */
@Plugin(type = Tamura2d.Directionality.class, label = "Tamura 2D: Directionality", name = Tamura2d.Directionality.NAME)
public class DefaultDirectionalityFeature<I extends RealType<I>, O extends RealType<O>>
		extends AbstractTamuraFeature<I, O>implements Directionality {

	@SuppressWarnings("unchecked")
	@Override
	public void compute(RandomAccessibleInterval<I> input, O output) {

		long[] dims = new long[input.numDimensions()];
		input.dimensions(dims);

		final byte[] arrayX = new byte[(int) Intervals.numElements(new FinalInterval(dims))];
		final byte[] arrayY = new byte[(int) Intervals.numElements(new FinalInterval(dims))];
		final byte[] arrayAll = new byte[(int) Intervals.numElements(new FinalInterval(dims))];
		Img<I> derX = (Img<I>) ArrayImgs.unsignedBytes(arrayX, dims);
		Img<I> derY = (Img<I>) ArrayImgs.unsignedBytes(arrayY, dims);
		//Img<I> derAll = (Img<I>) ArrayImgs.unsignedBytes(arrayAll, dims);

		// filter iterable interval in x and y direction
		PartialDerivative.gradientCentralDifference2(Views.extendMirrorSingle(input), derX, 0);
		PartialDerivative.gradientCentralDifference2(Views.extendMirrorSingle(input), derY, 1);

		// calculate theta at each position: theta = atan(dX/dY) + pi/2
		Cursor<I> cX = derX.cursor();
		Cursor<I> cY = derY.cursor();
		//Cursor<I> cAll = derAll.cursor();
		while (cX.hasNext()) {
			cX.next();
			cY.next();
			//cAll.next();

			double x = cX.get().getRealDouble();
			double y = cY.get().getRealDouble();

			if (x != 0) {
				double dir = Math.atan(y / x) + Math.PI / 2;

			}
		}
	}
}
