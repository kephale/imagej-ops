package net.imagej.ops.views;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;

public class RotateAxesViewRAI<T> extends
		AbstractViewOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements
		RotateAxes {

	@Parameter
	private int fromAxis;

	@Parameter
	private int toAxis;

	@Override
	public void run() {
		setOutput(Views.rotate(getInput(), fromAxis, toAxis));
	}

	@Override
	public Object getIndependentInstance() {
		return new RotateAxesViewRAI<T>();
	}
}
