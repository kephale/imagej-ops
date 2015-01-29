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
import net.imagej.ops.features.zernike.ZernikeComputer;
import net.imagej.ops.features.zernike.ZernikeMoment;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} containing Zernike Moments {@link Feature}s
 * 
 * @author Andreas Graumann, University of Konstanz
 * 
 * @param <I>
 */
@Plugin(type = FeatureSet.class, label = "Zernike Moment Features")
public class ZernikeFeatureSet<T extends RealType<T>> extends
		AbstractFeatureSet<IterableInterval<T>, DoubleType> implements
		Contingent {

	@Parameter
	private OpService ops;

	@Parameter(label = "Compute Magnitude")
	private boolean computeMagnitude;

	@Parameter(label = "Compute Phase")
	private boolean computePhase;

	@Parameter(label = "Order Min", min = "1", max = "10", stepSize = "1", initializer = "2")
	private int orderMin;

	@Parameter(label = "Oder Max", min = "1", max = "10", stepSize = "1", initializer = "6")
	private int orderMax;

	private ZernikeComputer op;

	@Override
	public boolean conforms() {
		// something to compute?
		if (!computeMagnitude && !computePhase) {
			return false;
		}

		// dimension must be 2
		if (!(getInput().numDimensions() == 2)) {
			return false;
		}

		return true;
	}

	@Override
	public void run() {
		// get ZernikeComputer
		if (op == null) {
			try {
				op = ops.op(ZernikeComputer.class, getInput(), orderMin,
						orderMax);
			} catch (Exception e) {
				throw new IllegalStateException(
						"Can not find suitable op! Error message: "
								+ e.getMessage());
			}
		}

		// run zernike computer
		op.run();

		final Map<OpRef<?>, Op> result = new HashMap<OpRef<?>, Op>();
		result.put(new OpRef<ZernikeComputer>(ZernikeComputer.class,
				computeMagnitude, computePhase, orderMin, orderMax), op);
		setOutput(result);
	}

	@Override
	public List<Pair<String, DoubleType>> getFeatures(
			final IterableInterval<T> input) {
		compute(input);
		final List<Pair<String, DoubleType>> res = new ArrayList<Pair<String, DoubleType>>();
		for (ZernikeMoment moment : op.getAllZernikeMoments()) {
			if (computeMagnitude) {
				String featureName = "Zernike Magnitude of order "
						+ moment.getN() + " and repitition " + moment.getM();
				res.add(new ValuePair<String, DoubleType>(featureName,
						new DoubleType(moment.getMagnitude())));
			}
			if (computePhase) {
				String featureName = "Zernike Phase of order " + moment.getN()
						+ " and repitition " + moment.getM();
				res.add(new ValuePair<String, DoubleType>(featureName,
						new DoubleType(moment.getPhase())));
			}
		}

		return res;
	}
}