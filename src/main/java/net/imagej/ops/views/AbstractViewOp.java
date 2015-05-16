package net.imagej.ops.views;

import net.imagej.ops.Threadable;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public abstract class AbstractViewOp<I, O> implements ViewOp<I, O>, Threadable {

	@Parameter(type = ItemIO.BOTH)
	private O out;

	@Parameter
	private I in;

	// -- InputOp methods --

	@Override
	public I getInput() {
		return in;
	}

	@Override
	public O getOutput() {
		return out;
	}

	// -- OutputOp methods --
	@Override
	public void setInput(final I input) {
		in = input;
	}

	@Override
	public void setOutput(final O output) {
		out = output;
	}
}
