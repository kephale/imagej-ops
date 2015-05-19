package net.imagej.ops.convert;

import java.lang.reflect.Type;

import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.Views;

import org.scijava.convert.AbstractConverter;
import org.scijava.convert.ConversionRequest;
import org.scijava.convert.Converter;
import org.scijava.plugin.Plugin;

@Plugin(type = Converter.class)
public class ConvertRAIToII extends
		AbstractConverter<RandomAccessibleInterval, IterableInterval> implements
		Converter<RandomAccessibleInterval, IterableInterval> {

	@Override
	public <T> T convert(Object src, Class<T> dest) {
		return (T) Views.iterable((RandomAccessibleInterval<T>) src);
	}

	@Override
	public Class<IterableInterval> getOutputType() {
		return IterableInterval.class;
	}

	@Override
	public Class<RandomAccessibleInterval> getInputType() {
		return RandomAccessibleInterval.class;
	}

}
