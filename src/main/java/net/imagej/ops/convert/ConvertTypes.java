/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.convert;

import java.math.BigInteger;

import net.imagej.ops.ConvertOps;
import net.imagej.ops.Op;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.complex.ComplexDoubleType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.Unsigned128BitType;
import net.imglib2.type.numeric.integer.Unsigned12BitType;
import net.imglib2.type.numeric.integer.Unsigned2BitType;
import net.imglib2.type.numeric.integer.Unsigned4BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.integer.UnsignedLongType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.ItemIO;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

public final class ConvertTypes {

	private ConvertTypes() {
		// NB: Prevent instantiation of utility class.
	}

	@Plugin(type = Op.class, name = ConvertOps.Bit.NAME, priority = 0.4)
	public static class ComplexToBit<C extends ComplexType<C>> implements
		ConvertOps.Bit
	{

		@Parameter(type = ItemIO.OUTPUT)
		private BitType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new BitType(in.getRealDouble() != 0);
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint2.NAME, priority = 0.4)
	public static class ComplexToUint2<C extends ComplexType<C>> implements
		ConvertOps.Uint2
	{

		@Parameter(type = ItemIO.OUTPUT)
		private Unsigned2BitType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new Unsigned2BitType((long) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint2.NAME, priority = 0.4)
	public static class IntegerToUint2<T extends IntegerType<T>> implements
		ConvertOps.Uint2
	{

		@Parameter(type = ItemIO.OUTPUT)
		private Unsigned2BitType result;

		@Parameter
		private T in;

		@Override
		public void run() {
			result = new Unsigned2BitType(in.getIntegerLong());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint4.NAME, priority = 0.4)
	public static class ComplexToUint4<C extends ComplexType<C>> implements
		ConvertOps.Uint4
	{

		@Parameter(type = ItemIO.OUTPUT)
		private Unsigned4BitType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new Unsigned4BitType((long) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint4.NAME, priority = 0.4)
	public static class IntegerToUint4<T extends IntegerType<T>> implements
		ConvertOps.Uint4
	{

		@Parameter(type = ItemIO.OUTPUT)
		private Unsigned4BitType result;

		@Parameter
		private T in;

		@Override
		public void run() {
			result = new Unsigned4BitType(in.getIntegerLong());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Int8.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Int8.ALIASES) })
	public static class ComplexToInt8<C extends ComplexType<C>> implements
		ConvertOps.Int8
	{

		@Parameter(type = ItemIO.OUTPUT)
		private ByteType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new ByteType((byte) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint8.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Uint8.ALIASES) })
	public static class ComplexToUint8<C extends ComplexType<C>> implements
		ConvertOps.Uint8
	{

		@Parameter(type = ItemIO.OUTPUT)
		private UnsignedByteType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new UnsignedByteType((int) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint8.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Uint8.ALIASES) })
	public static class IntegerToUint8<T extends IntegerType<T>> implements
		ConvertOps.Uint8
	{

		@Parameter(type = ItemIO.OUTPUT)
		private UnsignedByteType result;

		@Parameter
		private T in;

		@Override
		public void run() {
			result = new UnsignedByteType(in.getInteger());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint12.NAME, priority = 0.4)
	public static class ComplexToUint12<C extends ComplexType<C>> implements
		ConvertOps.Uint12
	{

		@Parameter(type = ItemIO.OUTPUT)
		private Unsigned12BitType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new Unsigned12BitType((long) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint12.NAME, priority = 0.4)
	public static class IntegerToUint12<T extends IntegerType<T>> implements
		ConvertOps.Uint12
	{

		@Parameter(type = ItemIO.OUTPUT)
		private Unsigned12BitType result;

		@Parameter
		private T in;

		@Override
		public void run() {
			result = new Unsigned12BitType(in.getIntegerLong());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Int16.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Int16.ALIASES) })
	public static class ComplexToInt16<C extends ComplexType<C>> implements
		ConvertOps.Int16
	{

		@Parameter(type = ItemIO.OUTPUT)
		private ShortType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new ShortType((short) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint16.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Uint16.ALIASES) })
	public static class ComplexToUint16<C extends ComplexType<C>> implements
		ConvertOps.Uint16
	{

		@Parameter(type = ItemIO.OUTPUT)
		private UnsignedShortType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new UnsignedShortType((int) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint16.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Uint16.ALIASES) })
	public static class IntegerToUint16<T extends IntegerType<T>> implements
		ConvertOps.Uint16
	{

		@Parameter(type = ItemIO.OUTPUT)
		private UnsignedShortType result;

		@Parameter
		private T in;

		@Override
		public void run() {
			result = new UnsignedShortType(in.getInteger());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Int32.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Int32.ALIASES) })
	public static class ComplexToInt32<C extends ComplexType<C>> implements
		ConvertOps.Int32
	{

		@Parameter(type = ItemIO.OUTPUT)
		private IntType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new IntType((int) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Int32.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Int32.ALIASES) })
	public static class IntegerToInt32<T extends IntegerType<T>> implements
		ConvertOps.Int32
	{

		@Parameter(type = ItemIO.OUTPUT)
		private IntType result;

		@Parameter
		private T in;

		@Override
		public void run() {
			result = new IntType(in.getInteger());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint32.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Uint32.ALIASES) })
	public static class ComplexToUint32<C extends ComplexType<C>> implements
		ConvertOps.Uint32
	{

		@Parameter(type = ItemIO.OUTPUT)
		private UnsignedIntType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new UnsignedIntType((long) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint32.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Uint32.ALIASES) })
	public static class IntegerToUint32<T extends IntegerType<T>> implements
		ConvertOps.Uint32
	{

		@Parameter(type = ItemIO.OUTPUT)
		private UnsignedIntType result;

		@Parameter
		private T in;

		@Override
		public void run() {
			result = new UnsignedIntType(in.getIntegerLong());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Int64.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Int64.ALIASES) })
	public static class ComplexToInt64<C extends ComplexType<C>> implements
		ConvertOps.Int64
	{

		@Parameter(type = ItemIO.OUTPUT)
		private LongType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new LongType((long) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Int64.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Int64.ALIASES) })
	public static class IntegerToInt64<T extends IntegerType<T>> implements
		ConvertOps.Int64
	{

		@Parameter(type = ItemIO.OUTPUT)
		private LongType result;

		@Parameter
		private T in;

		@Override
		public void run() {
			result = new LongType(in.getIntegerLong());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint64.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Uint64.ALIASES) })
	public static class ComplexToUint64<C extends ComplexType<C>> implements
		ConvertOps.Uint64
	{

		@Parameter(type = ItemIO.OUTPUT)
		private UnsignedLongType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new UnsignedLongType((long) in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint64.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Uint64.ALIASES) })
	public static class IntegerToUint64<T extends IntegerType<T>> implements
		ConvertOps.Uint64
	{

		@Parameter(type = ItemIO.OUTPUT)
		private UnsignedLongType result;

		@Parameter
		private T in;

		@Override
		public void run() {
			result = new UnsignedLongType(in.getIntegerLong());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint128.NAME, priority = 0.4)
	public static class ComplexToUint128<C extends ComplexType<C>> implements
		ConvertOps.Uint128
	{

		@Parameter(type = ItemIO.OUTPUT)
		private Unsigned128BitType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result =
				new Unsigned128BitType(BigInteger.valueOf((long) in.getRealDouble()));
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Uint128.NAME, priority = 0.4)
	public static class IntegerToUint128<T extends IntegerType<T>> implements
		ConvertOps.Uint128
	{

		@Parameter(type = ItemIO.OUTPUT)
		private Unsigned128BitType result;

		@Parameter
		private T in;

		@Override
		public void run() {
			result = new Unsigned128BitType(BigInteger.valueOf(in.getIntegerLong()));
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Float32.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Float32.ALIASES) })
	public static class ComplexToFloat32<C extends ComplexType<C>> implements
		ConvertOps.Float32
	{

		@Parameter(type = ItemIO.OUTPUT)
		private FloatType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new FloatType(in.getRealFloat());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Cfloat32.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Cfloat32.ALIASES) })
	public static class ComplexToCfloat32<C extends ComplexType<C>> implements
		ConvertOps.Cfloat32
	{

		@Parameter(type = ItemIO.OUTPUT)
		private ComplexFloatType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new ComplexFloatType(in.getRealFloat(), in.getImaginaryFloat());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Float64.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Float64.ALIASES) })
	public static class ComplexToFloat64<C extends ComplexType<C>> implements
		ConvertOps.Float64
	{

		@Parameter(type = ItemIO.OUTPUT)
		private DoubleType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result = new DoubleType(in.getRealDouble());
		}

	}

	@Plugin(type = Op.class, name = ConvertOps.Cfloat64.NAME, priority = 0.4,
		attrs = { @Attr(name = "aliases", value = ConvertOps.Cfloat64.ALIASES) })
	public static class ComplexToCfloat64<C extends ComplexType<C>> implements
		ConvertOps.Cfloat64
	{

		@Parameter(type = ItemIO.OUTPUT)
		private ComplexDoubleType result;

		@Parameter
		private C in;

		@Override
		public void run() {
			result =
				new ComplexDoubleType(in.getRealDouble(), in.getImaginaryDouble());
		}

	}
}
