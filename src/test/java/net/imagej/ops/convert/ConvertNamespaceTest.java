package net.imagej.ops.convert;

import net.imagej.ops.AbstractNamespaceTest;
import net.imagej.ops.ConvertOps.Bit;
import net.imagej.ops.ConvertOps.Cfloat32;
import net.imagej.ops.ConvertOps.Cfloat64;
import net.imagej.ops.ConvertOps.Float32;
import net.imagej.ops.ConvertOps.Float64;
import net.imagej.ops.ConvertOps.Int16;
import net.imagej.ops.ConvertOps.Int32;
import net.imagej.ops.ConvertOps.Int64;
import net.imagej.ops.ConvertOps.Int8;
import net.imagej.ops.ConvertOps.Uint12;
import net.imagej.ops.ConvertOps.Uint128;
import net.imagej.ops.ConvertOps.Uint16;
import net.imagej.ops.ConvertOps.Uint2;
import net.imagej.ops.ConvertOps.Uint32;
import net.imagej.ops.ConvertOps.Uint4;
import net.imagej.ops.ConvertOps.Uint64;
import net.imagej.ops.ConvertOps.Uint8;

import org.junit.Test;

/**
 * Tests that the ops of the {@code convert} namespace have corresponding type-safe
 * Java method signatures declared in the {@link ConvertNamespace} class.
 *
 * @author Alison Walter
 */
public class ConvertNamespaceTest extends AbstractNamespaceTest {

	/** Tests for {@link Bit} method convergence. */
	@Test
	public void testBit() {
		assertComplete("convert", ConvertNamespace.class, Bit.NAME);
	}

	/** Tests for {@link Uint2} method convergence. */
	@Test
	public void testUint2() {
		assertComplete("convert", ConvertNamespace.class, Uint2.NAME);
	}

	/** Tests for {@link Uint4} method convergence. */
	@Test
	public void testUint4() {
		assertComplete("convert", ConvertNamespace.class, Uint4.NAME);
	}

	/** Tests for {@link Int8} method convergence. */
	@Test
	public void testInt8() {
		assertComplete("convert", ConvertNamespace.class, Int8.NAME);
	}

	/** Tests for {@link Uint8} method convergence. */
	@Test
	public void testUint8() {
		assertComplete("convert", ConvertNamespace.class, Uint8.NAME);
	}

	/** Tests for {@link Uint12} method convergence. */
	@Test
	public void testUint12() {
		assertComplete("convert", ConvertNamespace.class, Uint12.NAME);
	}

	/** Tests for {@link Int16} method convergence. */
	@Test
	public void testInt16() {
		assertComplete("convert", ConvertNamespace.class, Int16.NAME);
	}

	/** Tests for {@link Uint16} method convergence. */
	@Test
	public void testUint16() {
		assertComplete("convert", ConvertNamespace.class, Uint16.NAME);
	}

	/** Tests for {@link Int32} method convergence. */
	@Test
	public void testInt32() {
		assertComplete("convert", ConvertNamespace.class, Int32.NAME);
	}

	/** Tests for {@link Uint32} method convergence. */
	@Test
	public void testUint32() {
		assertComplete("convert", ConvertNamespace.class, Uint32.NAME);
	}

	/** Tests for {@link Int64} method convergence. */
	@Test
	public void testInt64() {
		assertComplete("convert", ConvertNamespace.class, Int64.NAME);
	}

	/** Tests for {@link Uint64} method convergence. */
	@Test
	public void testUint64() {
		assertComplete("convert", ConvertNamespace.class, Uint64.NAME);
	}

	/** Tests for {@link Uint128} method convergence. */
	@Test
	public void testUint128() {
		assertComplete("convert", ConvertNamespace.class, Uint128.NAME);
	}

	/** Tests for {@link Float32} method convergence. */
	@Test
	public void testFloat32() {
		assertComplete("convert", ConvertNamespace.class, Float32.NAME);
	}

	/** Tests for {@link Cfloat32} method convergence. */
	@Test
	public void testCfloat32() {
		assertComplete("convert", ConvertNamespace.class, Cfloat32.NAME);
	}

	/** Tests for {@link Float64} method convergence. */
	@Test
	public void testFloat64() {
		assertComplete("convert", ConvertNamespace.class, Float64.NAME);
	}

	/** Tests for {@link Cfloat64} method convergence. */
	@Test
	public void testCfloat64() {
		assertComplete("convert", ConvertNamespace.class, Cfloat64.NAME);
	}
}
