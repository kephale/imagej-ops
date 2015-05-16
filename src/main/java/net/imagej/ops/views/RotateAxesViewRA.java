package net.imagej.ops.views;

import net.imglib2.RandomAccessible;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;

public class RotateAxesViewRA<T> extends
		AbstractViewOp<RandomAccessible<T>, RandomAccessible<T>> implements
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
		return new RotateAxesViewRA<T>();
	}
}
