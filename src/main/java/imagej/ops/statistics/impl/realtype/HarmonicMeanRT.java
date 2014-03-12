/*
 * #%L
 * SciJava OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2013 - 2014 Board of Regents of the University of
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
 * #L%
 */

package imagej.ops.statistics.impl.realtype;

import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.misc.Area;
import imagej.ops.statistics.HarmonicMean;
import imagej.ops.statistics.Kurtosis;
import imagej.ops.statistics.impl.realtype.sums.SumOfInversesRT;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = HarmonicMean.NAME, label = HarmonicMean.LABEL,
	priority = Priority.LOW_PRIORITY)
public class HarmonicMeanRT extends AbstractFunctionIRT2RT implements
	Kurtosis<Iterable<? extends RealType<?>>, RealType<?>>
{

	@Parameter
	private OpService ops;

	@Parameter(required = false)
	private SumOfInversesRT inverseSum;

	@Parameter(required = false)
	private Area<Iterable<?>, RealType<?>> area;

	// TODO remove?
	final DoubleType tmp1 = new DoubleType();
	final DoubleType tmp2 = new DoubleType();

	@Override
	public RealType<?> compute(final Iterable<? extends RealType<?>> input,
		final RealType<?> output)
	{

		initFunctions(input);

		output.setReal(area.compute(input, tmp1).getRealDouble() /
			inverseSum.compute(input, tmp2).getRealDouble());

		return output;
	}

	@SuppressWarnings("unchecked")
	private void initFunctions(final Iterable<? extends RealType<?>> input) {
		if (area == null) area =
			(Area<Iterable<?>, RealType<?>>) ops
				.op(Area.class, RealType.class, input);

		if (inverseSum == null) inverseSum =
			(SumOfInversesRT) ops.op(SumOfInversesRT.class,
				RealType.class, input);
	}
}