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
package net.imagej.ops.features.sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;
import net.imagej.ops.features.AbstractFeatureSet;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.LabeledFeatures;
import net.imagej.ops.features.lbp.LocalBinaryPatterns2D;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
@Plugin(type = FeatureSet.class, label = "Local Binary Patterns Features", description = "Calculates the Local Binary Patterns Features")
public class LBP2DFeatureSet<T extends RealType<T>> extends
		AbstractFeatureSet<IterableInterval<T>, List<DoubleType>> implements
		Contingent, LabeledFeatures<IterableInterval<T>, DoubleType> {

	@Parameter
	private OpService ops;

	private LocalBinaryPatterns2D op;

	@Override
	public void run() {
		if (this.op == null) {
			try {
				this.op = this.ops.op(LocalBinaryPatterns2D.class, getInput());
			} catch (final Exception e) {
				throw new IllegalStateException(
						"Can not find suitable op! Error message: "
								+ e.getMessage());
			}

			setOutput(new HashMap<OpRef<? extends Op>, List<DoubleType>>());
		}

		op.run();
	}

	@Override
	public boolean conforms() {
		return getInput().numDimensions() == 2;
	}

	@Override
	public Map<OpRef<? extends Op>, List<DoubleType>> getFeaturesByRef(
			IterableInterval<T> input) {
		return compute(input);
	}

	@Override
	public List<Pair<String, DoubleType>> getFeatureList(IterableInterval<T> input) {
		compute(input);
		final List<Pair<String, DoubleType>> res = new ArrayList<Pair<String, DoubleType>>();
		
		int n = 0;
		for (double type : op.getOutput()) {
			res.add(new ValuePair<String, DoubleType>("LBP [" + ++n + "]",
					new DoubleType(type)));
		}
	
		return res;
	}
}
