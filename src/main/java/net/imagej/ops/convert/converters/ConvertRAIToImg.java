package net.imagej.ops.convert.converters;

import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.ImgView;
import net.imglib2.type.Type;

import org.scijava.convert.AbstractConverter;
import org.scijava.convert.Converter;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Convert RandomAccessibleInterval to Img
 * 
 * @author Christian Dietz, University of Konstanz
 *
 * @param <V>
 */
@Plugin(type = Converter.class)
public class ConvertRAIToImg<V extends Type<V>> extends
		AbstractConverter<RandomAccessibleInterval, Img> {

	@Parameter
	private OpService ops;

	@Override
	public <T> T convert(Object src, Class<T> dest) {
		// TODO Use: CreateFactory
		return (T) ImgView.wrap((RandomAccessibleInterval<V>) src,
				(ImgFactory<V>) null);
	}

	@Override
	public Class<Img> getOutputType() {
		return Img.class;
	}

	@Override
	public Class<RandomAccessibleInterval> getInputType() {
		return RandomAccessibleInterval.class;
	}

	@Override
	public boolean canConvert(Class<?> src, Class<?> dest) {
		return src != null && RandomAccessibleInterval.class.isAssignableFrom(src)
				&& Img.class.isAssignableFrom(dest);
	}

	@Override
	public boolean canConvert(Object src, Class<?> dest) {
		return src != null && src instanceof RandomAccessibleInterval
				&& Img.class.isAssignableFrom(dest);
	}

}
