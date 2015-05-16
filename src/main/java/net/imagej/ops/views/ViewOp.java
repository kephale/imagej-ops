package net.imagej.ops.views;

import net.imagej.ops.InputOp;
import net.imagej.ops.OutputOp;

public interface ViewOp<I, O> extends InputOp<I>, OutputOp<O> {
	// NB: Marker interface
}
