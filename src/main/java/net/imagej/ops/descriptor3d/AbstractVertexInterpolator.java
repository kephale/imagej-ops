package net.imagej.ops.descriptor3d;


import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public abstract class AbstractVertexInterpolator implements VertexInterpolator {

	@Parameter(type = ItemIO.OUTPUT)
	double[] output;
	
	@Parameter(type = ItemIO.INPUT)
	int[] p1;
	
	@Parameter(type = ItemIO.INPUT)
	int[] p2;
	
	@Parameter(type = ItemIO.INPUT)
	double p1Value;
	
	@Parameter(type = ItemIO.INPUT)
	double p2Value;
	
	@Override
	public double[] getOutput() {
		return output;
	}

	@Override
	public void setOutput(double[] output) {
		this.output = output;
	}
}