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

package net.imagej.ops.stats.mean;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.stats.size.SizeOp;
import net.imagej.ops.stats.sum.SumOp;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Computes the mean of values for the input {@link Iterable}.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Stats.Mean.class, name = Ops.Stats.Mean.NAME,
	priority = Priority.LOW_PRIORITY)
public class MeanRealType<I extends RealType<I>, O extends RealType<O>> extends
	AbstractComputerOp<Iterable<I>, O> implements MeanOp<Iterable<I>, O>
{

	@Parameter(required = false)
	private SumOp<Iterable<I>, DoubleType> sumFunc;

	@Parameter(required = false)
	private SizeOp<Iterable<I>> sizeFunc;

	@Parameter
	private OpService ops;

	@Override
	public void compute(final Iterable<I> input, final O output) {
		if (sumFunc == null) {
			sumFunc = ops.op(SumOp.class, DoubleType.class, input);
		}
		if (sizeFunc == null) {
			sizeFunc = ops.op(SizeOp.class, LongType.class, input);
		}

		final LongType size = new LongType();
		sizeFunc.compute(input, size);
		final DoubleType sum = new DoubleType();
		sumFunc.compute(input, sum);

		final double mean = sum.getRealDouble() / size.getRealDouble();

		output.setReal(mean);
	}

}
