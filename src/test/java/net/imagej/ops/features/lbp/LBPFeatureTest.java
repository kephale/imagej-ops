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
package net.imagej.ops.features.lbp;

import java.util.List;

import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.features.sets.LBP2DFeatureSet;
import net.imagej.ops.features.sets.LBP3DFeatureSet;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.junit.Test;

/**
 * 
 * Test of Local Binary Patterns
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
public class LBPFeatureTest extends AbstractFeatureTest {

	@Test
	public void test() {
		
		List<Pair<String, DoubleType>> res = ops.op(LBP2DFeatureSet.class, random).getFeatureList(random);
		
		// TODO: Check values against other implementation
		for (int i = 0; i < res.size(); i++) {
			System.out.println(res.get(i).getA() + " " +res.get(i).getB());
		}
		
		res = ops.op(LBP3DFeatureSet.class, random3d).getFeatureList(random3d);
		
		// TODO: Check values against other implementation
		for (int i = 0; i < res.size(); i++) {
			System.out.println(res.get(i).getA() + " " +res.get(i).getB());
		}
		
	}

}
