package net.imagej.ops.views;

import java.util.List;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.Stack.class, name = Ops.View.Stack.NAME)
public class DefaultStack<T> extends AbstractFunctionOp<List<RandomAccessibleInterval<T>>, RandomAccessibleInterval<T>>
		implements Ops.View.Stack {

	@Override
	public RandomAccessibleInterval<T> compute(List<RandomAccessibleInterval<T>> input) {
		return Views.stack(input);
	}

}
