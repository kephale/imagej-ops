/*
 * #%L
 * SciJava OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2013 Board of Regents of the University of
 * Wisconsin-Madison, and University of Konstanz.
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
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */
package net.imagej.ops.features.tamura.helper;

import java.util.Arrays;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment4AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.VarianceFeature;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * @author Andreas Graumann, University of Konstanz
 * @author Veronika Becker , University of Konstanz
 *
 */
@Plugin(type = Op.class)
public class TamuraTexture3DComputer implements TamuraTextureComputer, Contingent {

	// input image
	@Parameter
	private IterableInterval<? extends RealType<?>> ii;
	
	@Parameter
	private MeanFeature<? extends RealType<?>> my;

	@Parameter
	private VarianceFeature<? extends RealType<?>> var;

	@Parameter
	private Moment4AboutMeanFeature<? extends RealType<?>> m4;
	
	@Parameter
	private OpService ops;

	// output array with all three computed features
	@Parameter(type = ItemIO.OUTPUT)
	private TamuraFeatures output;
	
    private static final double[][][] filterH = {{{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}}, {{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}}, {{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}}};
    private static final double[][][] filterV = {{{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}}, {{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}}, {{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}}};
    private static final double[][][] filterZ = {{{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}}, {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}, {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}};

	@Override
	public void run() {
		
		if (output == null) {
			output = new TamuraFeatures();
		}
		
		// convert iterarble interval to array
		int[][][] img = writeImageToArray();
		output.setDirectionality(directionality(img));
	}
	
	/**
	 * Compute tamura contrast feature
	 * 
	 * @return contrast feature
	 */
	public double contrast() {
		final double l4 = m4.getOutput().getRealDouble()
				/ (Math.pow(var.getOutput().getRealDouble(), 2));
		return Math.sqrt(var.getOutput().getRealDouble()) / Math.pow(l4, 0.25);
	}
	
    /**
     * @return
     */
    public double[] directionality(int[][][] img) {
        double[] histogram = new double[16];
        double maxResult = 3;
        double binWindow = maxResult / (double) (histogram.length - 1);
        int bin = -1;
        for (int x = 1; x < img.length - 1; x++) {
            for (int y = 1; y < img[x].length - 1; y++) {
               for (int z = 1; z < img[x][y].length -1; z++) {
            	   bin = (int) ((Math.PI / 2 + Math.atan(this.calculateDeltaV(x, y, z, img) / this.calculateDeltaH(x, y, z, img))) / binWindow);
            	   histogram[bin]++;
            	   bin = (int) ((Math.PI / 2 + Math.atan(this.calculateDeltaZ(x, y, z, img) / this.calculateDeltaV(x, y, z, img))) / binWindow);
            	   histogram[bin]++;
            	   bin = (int) ((Math.PI / 2 + Math.atan(this.calculateDeltaH(x, y, z, img) / this.calculateDeltaZ(x, y, z, img))) / binWindow);
            	   histogram[bin]++;
               }
            }
        }
        return histogram;
    }

    /**
     * @return
     */
    public double calculateDeltaH(int x, int y, int z, int[][][] img) {
        double result = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	for (int k = 0; k < 3; k++) {
            		result = result + img[x - 1 + i][y - 1 + j][z - 1 + k] * filterH[i][j][k];
            	}
            }
        }
        return result;
    }

    /**
     * @return
     */
    public double calculateDeltaV(int x, int y, int z,  int[][][] img) {
        double result = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	for (int k = 0; k < 3; k++) {
            		result = result + img[x - 1 + i][y - 1 + j][z - 1 + k] * filterV[i][j][k];
            	}
            }
        }
        return result;
    }
    
    /**
     * @return
     */
    public double calculateDeltaZ(int x, int y, int z,  int[][][] img) {
        double result = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	for (int k = 0; k < 3; k++) {
            		result = result + img[x - 1 + i][y - 1 + j][z - 1 + k] * filterZ[i][j][k];
            	}
            }
        }
        return result;
    }
	
	/**
	 * 
	 * @return
	 */
	private int[][][] writeImageToArray() {
		int dimX = -1;
		int dimY = -1;
		int dimZ = -1;

		for (int d = 0; d < ii.numDimensions(); d++) {
			if (ii.dimension(d) > 1) {
				if (dimX == -1) {
					dimX = d;
				} else if (dimY == -1) {
					dimY = d;
				} else {
					dimZ = d;
					break;
				}
			}
		}
		final Cursor<? extends RealType<?>> cursor = ii.cursor();

		 int[][][] pixels = new int[(int) ii.dimension(dimX)][(int) ii
				.dimension(dimY)][(int) ii.dimension(dimZ)];

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				Arrays.fill(pixels[i][j], Integer.MAX_VALUE);
			}
		}

		while (cursor.hasNext()) {
			cursor.fwd();
			pixels[cursor.getIntPosition(dimY) - (int) ii.min(dimY)][cursor
					.getIntPosition(dimX) - (int) ii.min(dimX)][cursor.getIntPosition(dimZ) - (int) ii.min(dimZ)] = (int) (cursor
					.get().getRealDouble());
		}
		
		return pixels;
	}
	
	@Override
	public boolean conforms() {
		//if (ii.numDimensions() == 2)
		//	return true;
		return true;
	}

	@Override
	public TamuraFeatures getOutput() {
		return output;
	}

	@Override
	public void setOutput(TamuraFeatures _output) {
		output = _output;
	}

}
