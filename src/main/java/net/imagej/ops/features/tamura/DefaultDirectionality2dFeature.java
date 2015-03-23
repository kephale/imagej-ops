/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
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
 */package net.imagej.ops.features.tamura;

import net.imagej.ops.Op;
import net.imagej.ops.features.provider.IterableIntervalTo2dArrayProvider;
import net.imagej.ops.features.tamura.TamuraFeatures.Directionality2dFeature;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * Default implementation of 2d Tamura Texture Direcionality Feature
 * 
 * @author Andreas Graumann, University of Konstanz
 * @author Christian Dietz, Univesity of Konstanz
 *
 */
@Plugin(type = Op.class, name = "Tamura 2D: Directionality")
public class DefaultDirectionality2dFeature implements Directionality2dFeature<DoubleType> {

	@Parameter
	IterableIntervalTo2dArrayProvider imgProvider;
	
	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;
	
	// filter matrix for sobel in horizontal direction
	private static final double[][] filterH = { { -1, 0, 1 }, { -1, 0, 1 },
			{ -1, 0, 1 } };
	// filter matrix for sobel in vertical direction
	private static final double[][] filterV = { { -1, -1, -1 }, { 0, 0, 0 },
			{ 1, 1, 1 } };
	
	// array to store iterable interval
	private int[][] img;
	
	@Override
	public DoubleType getOutput() {
		return out;
	}

	@Override
	public void setOutput(DoubleType output) {
		out = output;
	}

	@Override
	public void run() {
		img = imgProvider.getOutput();	
		setOutput(new DoubleType(directionality()));
	}
	
	/**
	 * Compute tamura directionality
	 * 
	 * @return directionality as histogram
	 */
	public double directionality() {
		double[] histogram = new double[16];
		double maxResult = 3;
		double binWindow = maxResult / (double) (histogram.length - 1);
		int bin = -1;
		for (int x = 1; x < img.length - 1; x++) {
			for (int y = 1; y < img[x].length - 1; y++) {
				if (img[x][y] != Integer.MAX_VALUE) {
					bin = (int) ((Math.PI / 2 + Math.atan(this.calculateDeltaV(
							x, y) / this.calculateDeltaH(x, y))) / binWindow);
					histogram[bin]++;
				}
			}
		}

		for (int i = 0; i < histogram.length; i++) {
			System.out.print(histogram[i] + " ");
		}
		System.out.println(" ");
		
		double dir = 0.0;
		// get second moment of all histogram values
		for (int i = 0; i < histogram.length; i++) {
			dir += Math.pow(histogram[i], 2);
		}
		dir /= histogram.length;

		return dir;
	}

	/**
	 * @return
	 */
	public double calculateDeltaH(int x, int y) {
		double result = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (img[x - 1 + i][y - 1 + j] != Integer.MAX_VALUE)
					result = result + img[x - 1 + i][y - 1 + j] * filterH[i][j];
			}
		}
		return result;
	}

	/**
	 * @return
	 */
	public double calculateDeltaV(int x, int y) {
		double result = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (img[x - 1 + i][y - 1 + j] != Integer.MAX_VALUE)
					result = result + img[x - 1 + i][y - 1 + j] * filterV[i][j];
			}
		}
		return result;
	}

}
