package net.imagej.ops.convert;

import static org.junit.Assert.assertTrue;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;
import org.scijava.convert.ConvertService;

public class ConvertRAIToIITest extends AbstractOpTest {
	private Img<DoubleType> input;

	@Before
	public void createData() {
		input = new ArrayImgFactory().create(new int[] { 50, 50, 1 },
				new DoubleType());
	}

	@Test
	public void convertTest() {

		ConvertService cs = context.getService(ConvertService.class);
		IterableInterval out1 = cs.convert(Views.subsample(input, 10),
				IterableInterval.class);
		assertTrue(out1 != null);
	}
}
