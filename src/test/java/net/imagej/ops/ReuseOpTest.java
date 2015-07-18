
package net.imagej.ops;

import net.imagej.ops.create.img.CreateImgFromImg;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Test;

public class ReuseOpTest extends AbstractOpTest {

	@Test
	public void reuseOpTest() {
		@SuppressWarnings("unchecked")
		CreateImgFromImg<UnsignedByteType> op =
			ops.op(CreateImgFromImg.class, Img.class);

		Img<ByteType> img = generateByteTestImg(false, 2, 2);
		for (int k = 0; k < 3; k++)
			ops.run(op, img);
	}
}
