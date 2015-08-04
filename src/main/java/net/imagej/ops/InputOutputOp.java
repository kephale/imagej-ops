package net.imagej.ops;

/**
 * A {@link Threadable} {@link Op} with a special input parameter (exposed via
 * the {@link Input} interface}, and a special output parameter (exposed via the
 * {@link Output} interface).
 * <p>
 * This interface exists because the union of these four interfaces is an
 * extremely common pattern; in particular, both {@link FunctionOp} and
 * {@link ComputerOp} extend this interface.
 * </p>
 * 
 * @author Curtis Rueden
 * @param <I> type of input
 * @param <O> type of output
 */
public interface InputOutputOp<I, O> extends Op, Input<I>, Output<O>,
	Threadable
{

	@Override
	InputOutputOp<I, O> getIndependentInstance();

}
