package net.imagej.ops.features.geometric3d;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Op;
import net.imagej.ops.descriptor3d.QuickHull3D;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.geometric.Geometric3DFeatures.ConvexHullSurfacePixelFeature;
import net.imagej.ops.statistics.Geometric3DOps.ConvexHullSurfacePixel;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Generic implementation of {@link ConvexHullSurfacePixelFeature}. Use
 * {@link FeatureSet} to compile this {@link Op}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = ConvexHullSurfacePixel.NAME, label = ConvexHullSurfacePixel.LABEL, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultConvexHullSurfacePixelFeature
		implements
			ConvexHullSurfacePixelFeature<DoubleType> {

	@Parameter(type = ItemIO.INPUT)
	private QuickHull3D input;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;

	@Override
	public DoubleType getOutput() {
		return out;
	}

	@Override
	public void setOutput(DoubleType output) {
		this.out = output;
	}

	@Override
	public void run() {
		out = new DoubleType(input.getOutput().getPoints().size());
	}

}