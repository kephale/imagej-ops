
package net.imagej.ops.filter.median;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.Ops;
import net.imagej.ops.filter.AbstractNeighborhoodBasedFilter;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link MedianFilterOp}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @param <T> type
 */
@Plugin(type = Ops.Filter.Median.class, name = Ops.Filter.Median.NAME,
	priority = Priority.LOW_PRIORITY)
public class DefaultMedianFilter<T extends RealType<T>> extends
	AbstractNeighborhoodBasedFilter<T, T> implements
	MedianFilterOp<RandomAccessibleInterval<T>>
{

	@SuppressWarnings("unchecked")
	@Override
	protected ComputerOp<Iterable<T>, T> getComputer(Class<?> inClass,
		Class<?> outClass)
	{
		return (ComputerOp<Iterable<T>, T>) ops.op(Ops.Stats.Median.class, inClass,
			Iterable.class);
	}

}
