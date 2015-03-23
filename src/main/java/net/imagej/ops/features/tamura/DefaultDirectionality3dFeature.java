package net.imagej.ops.features.tamura;

import net.imagej.ops.features.provider.IterableIntervalTo3dArrayProvider;
import net.imagej.ops.features.tamura.TamuraFeatures.Directionality2dFeature;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * 
 * Default implementation of 3d Tamura Texture Direcionality Feature
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
public class DefaultDirectionality3dFeature implements
		Directionality2dFeature<DoubleType> {

	@Parameter
	IterableIntervalTo3dArrayProvider imgProvider;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;

	private static final double[][][] filterH = {
			{ { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } },
			{ { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } },
			{ { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } } };
	private static final double[][][] filterV = {
			{ { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } },
			{ { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } },
			{ { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } } };
	private static final double[][][] filterZ = {
			{ { -1, -1, -1 }, { -1, -1, -1 }, { -1, -1, -1 } },
			{ { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } },
			{ { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } } };

	// array to store iterable interval
	private int[][][] img;

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
				for (int z = 1; z < img[x][y].length - 1; z++) {
					if (img[x][y][z] != Integer.MAX_VALUE) {
						bin = (int) ((Math.PI / 2 + Math.atan(this
								.calculateDeltaV(x, y, z)
								/ this.calculateDeltaH(x, y, z))) / binWindow);
						histogram[bin]++;
						bin = (int) ((Math.PI / 2 + Math.atan(this
								.calculateDeltaZ(x, y, z)
								/ this.calculateDeltaV(x, y, z))) / binWindow);
						histogram[bin]++;
						bin = (int) ((Math.PI / 2 + Math.atan(this
								.calculateDeltaH(x, y, z)
								/ this.calculateDeltaZ(x, y, z))) / binWindow);
						histogram[bin]++;
					}
				}
			}
		}

		double dir = 0.0;
		// get second moment of all histogram values
		for (int i = 0; i < histogram.length; i++) {
			dir += Math.pow(histogram[i], 2);
		}
		dir /= histogram.length;

		return dir;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public double calculateDeltaH(int x, int y, int z) {
		double result = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					if (img[x - 1 + i][y - 1 + j][z - 1 + k] != Integer.MAX_VALUE)
						result = result + img[x - 1 + i][y - 1 + j][z - 1 + k]
								* filterH[i][j][k];
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public double calculateDeltaV(int x, int y, int z) {
		double result = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					if (img[x - 1 + i][y - 1 + j][z - 1 + k] != Integer.MAX_VALUE)
						result = result + img[x - 1 + i][y - 1 + j][z - 1 + k]
								* filterV[i][j][k];
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public double calculateDeltaZ(int x, int y, int z) {
		double result = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					if (img[x - 1 + i][y - 1 + j][z - 1 + k] != Integer.MAX_VALUE)
						result = result + img[x - 1 + i][y - 1 + j][z - 1 + k]
								* filterZ[i][j][k];
				}
			}
		}
		return result;
	}
}
