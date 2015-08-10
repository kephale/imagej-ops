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

import net.imglib2.type.numeric.integer.Unsigned128BitType;


/**
 * Utility methods for converting between primitive types.
 * 
 * @author Alison Walter
 */
public final class Types {

	private Types() { }

	public static boolean bit(final long value) { return (value != 0); }

	public static boolean bit(final double value) { return bit((long) value); }

	public static boolean bit(final boolean value) { return value; }

	public static boolean bit(final BigInteger value) { return (value.compareTo(BigInteger.ZERO) != 0) ? true : false; }

	public static long uint2(final long value) { return (value & 0x3);}

	public static long uint2(final double value) { return uint2((long) value); }

	public static long uint2(final BigInteger value) { return uint2(value.longValue()); }

	public static long uint2(final boolean value) { return uint8(value); }

	public static long uint4(final long value) { return (value & 0xf); }

	public static long uint4(final double value) { return uint4((long) value); }

	public static long uint4(final BigInteger value) { return uint4(value.longValue()); }

	public static long uint4(final boolean value) { return uint8(value); }

	public static byte int8(final long value) { return (byte) value; }

	public static byte int8(final double value) { return (byte) value; }

	public static byte int8(final BigInteger value) { return value.byteValue(); }

	public static byte int8(final boolean value) { return value ? (byte) 1 : (byte) 0; }

	public static int uint8(final long value) { return (int) (value & 0xff); }

	public static int uint8(final double value) { return uint8((long) value); }

	public static int uint8(final BigInteger value) { return uint8(value.longValue()); }

	public static int uint8(final boolean value) { return value ? 1 : 0; }

	public static long uint12(final long value) { return (value & 0xfff); }

	public static long uint12(final double value) { return uint12((long) value); }

	public static long uint12(final BigInteger value) { return uint12(value.longValue()); }

	public static long uint12(final boolean value) { return uint8(value); }

	public static short int16(final long value) { return (short) value; }

	public static short int16(final double value) { return (short) value; }

	public static short int16(final BigInteger value) { return value.shortValue(); }

	public static short int16(final boolean value) { return value ? (short) 1 : (short) 0; }

	public static int uint16(final long value) { return (int) (value & 0xffff); }

	public static int uint16(final double value) { return uint16((long) value); }

	public static int uint16(final BigInteger value) { return uint16(value.longValue()); }

	public static int uint16(final boolean value) { return value ? 1 : 0; }

	public static int int32(final long value) { return (int) value; }

	public static int int32(final double value) { return (int) value; }

	public static int int32(final BigInteger value) { return value.intValue(); }

	public static int int32(final boolean value) { return value ? 1  : 0; }

	public static long uint32(final long value) { return (value & 0xffffffffL); }

	public static long uint32(final double value) { return uint32((long) value); }

	public static long uint32(final BigInteger value) { return uint32(value.longValue()); }

	public static long uint32(final boolean value) { return uint8(value); }

	public static long int64(final long value) { return value; }

	public static long int64(final double value) { return (long) value; }

	public static long int64(final BigInteger value) { return value.longValue(); }

	public static long int64(final boolean value) { return uint8(value); }

	public static long uint64(final long value) { return (value & 0xffffffffffffffffL); }

	public static long uint64(final double value) { return uint64((long) value); }

	public static long uint64(final BigInteger value) { return uint64(value.longValue()); }

	public static long uint64(final boolean value) { return uint8(value); }

	public static BigInteger uint128(final long value) {
		final BigInteger bi = BigInteger.valueOf(value);
		if (bi.compareTo(BigInteger.ZERO) >= 0) return bi;

		final int validBytes = validBytes(value);
		return bi.add(new BigInteger("2").pow(8 * validBytes));
	}

	private static int validBytes(final long value) {
		for (int b=8; b>=0; b--) {
			final long v = value >>> 8 * (b - 1);
			boolean pattern = false;
			if(((value >>> 8 * b) & 0x01) == 1 && (v & 0x80) == 0){
				pattern = true;
			}
			if ((v & 0xff) != 0xff && pattern) return b+1;
			if ((v & 0xff) != 0xff) return b;
		}
		throw new IllegalStateException("unknown valid bytes: " + value);
	}

	public static BigInteger uint128(final double value) { return uint128((long) value); }

	public static BigInteger uint128(final BigInteger value) { return value; }

	public static BigInteger uint128(final boolean value) { return value ? BigInteger.ONE : BigInteger.ZERO; }

	public static float float32(final long value) { return value; }

	public static float float32(final double value) { return (float) value; }

	public static float float32(final BigInteger value) { return value.floatValue(); }

	public static float float32(final boolean value) { return value ? 1 : 0; }

	public static double float64(final long value) { return value; }

	public static double float64(final double value) { return value; }

	public static double float64(final BigInteger value) { return value.doubleValue(); }

	public static double float64(final boolean value) { return float32(value); }

}
