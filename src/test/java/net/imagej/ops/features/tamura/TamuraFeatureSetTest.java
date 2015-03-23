/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package net.imagej.ops.features.tamura;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.features.sets.Tamura2DFeatureSet;
import net.imagej.ops.features.sets.Tamura3DFeatureSet;
import net.imglib2.Cursor;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

/**
 * Test of {@Link TamuraTexture2DFeatureSet}
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
public class TamuraFeatureSetTest extends AbstractFeatureTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		List<Pair<String, DoubleType>> res = ops.op(
				Tamura2DFeatureSet.class, random).getFeatureList(random);

		for (Pair<String, DoubleType> entry : res) {
			System.out.println(entry.getA() + ": " + entry.getB());
		}
//		
//		res = ops.op(
//				Tamura3DFeatureSet.class, random3d).getFeatureList(random3d);
//
//		for (Pair<String, DoubleType> entry : res) {
//			System.out.println(entry.getA() + ": " + entry.getB());
//		}
//		
		Cursor<UnsignedByteType> cur = random.cursor();
		
		BufferedImage image = new BufferedImage((int)random.dimension(0), (int)random.dimension(1), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();
		
		while (cur.hasNext()) {
			cur.next();
			int x = cur.getIntPosition(0);
			int y = cur.getIntPosition(1);
			int val = cur.get().get();
			raster.setSample(x, y, 0, val);
			
		}
		
//        Tamura sch = new Tamura();
//        System.out.println("image = " + image.getWidth() + " x " + image.getHeight());
//        sch.extract(image);
//        System.out.println("sch = " + sch.getStringRepresentation());
		
	}
}
