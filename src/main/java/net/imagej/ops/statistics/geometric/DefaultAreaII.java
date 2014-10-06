package net.imagej.ops.statistics.geometric;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.statistics.geometric.GeometricStatOps.Area;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;

import org.scijava.plugin.Plugin;

/**
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Op.class, name = Area.NAME, label = Area.NAME)
public class DefaultAreaII extends
		AbstractOutputFunction<IterableInterval<?>, RealType<?>> implements
		AreaFeature, AreaII {

	@Override
	public double getFeatureValue() {
		return getOutput().getRealDouble();
	}

	@Override
	public RealType<?> createOutput(IterableInterval<?> input) {
		return new LongType();
	}

	@Override
	protected RealType<?> safeCompute(IterableInterval<?> input,
			RealType<?> output) {
		output.setReal((double) input.size());
		return output;
	}

}