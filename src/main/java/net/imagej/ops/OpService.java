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

package net.imagej.ops;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.imagej.ImageJService;
import net.imagej.ops.convert.ConvertPix;
import net.imagej.ops.create.CreateNamespace;
import net.imagej.ops.deconvolve.DeconvolveNamespace;
import net.imagej.ops.filter.FilterNamespace;
import net.imagej.ops.image.ImageNamespace;
import net.imagej.ops.labeling.LabelingNamespace;
import net.imagej.ops.logic.LogicNamespace;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imagej.ops.math.MathNamespace;
import net.imagej.ops.stats.StatsNamespace;
import net.imagej.ops.thread.ThreadNamespace;
import net.imagej.ops.threshold.ThresholdNamespace;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;

import org.scijava.command.CommandInfo;
import org.scijava.module.Module;
import org.scijava.plugin.PTService;

/**
 * Interface for services that manage and execute {@link Op}s.
 *
 * @author Curtis Rueden
 */
public interface OpService extends PTService<Op>, ImageJService {

	/**
	 * Executes the given operation with the specified arguments. The best
	 * {@link Op} implementation to use will be selected automatically from the
	 * operation name and arguments.
	 *
	 * @param name The operation to execute. If multiple {@link Op}s share this
	 *          name, then the best {@link Op} implementation to use will be
	 *          selected automatically from the name and arguments. If a name
	 *          without namespace is given, then ops from all namespaces with that
	 *          name will be included in the match; e.g., {@code "and"} will match
	 *          both {@code "logic.and"} and {@code "math.and"} ops.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	@OpMethod(op = net.imagej.ops.run.RunByName.class)
	Object run(String name, Object... args);

	/**
	 * Executes the operation of the given type with the specified arguments. The
	 * best {@link Op} implementation to use will be selected automatically from
	 * the operation type and arguments.
	 *
	 * @param type The {@link Class} of the operation to execute. If multiple
	 *          {@link Op}s share this type (e.g., the type is an interface which
	 *          multiple {@link Op}s implement), then the best {@link Op}
	 *          implementation to use will be selected automatically from the type
	 *          and arguments.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	@OpMethod(op = net.imagej.ops.run.RunByType.class)
	<OP extends Op> Object run(Class<OP> type, Object... args);

	/**
	 * Executes the given {@link Op} with the specified arguments.
	 *
	 * @param op The {@link Op} to execute.
	 * @param args The operation's arguments.
	 * @return The result of the execution. If the {@link Op} has no outputs, this
	 *         will return {@code null}. If exactly one output, it will be
	 *         returned verbatim. If more than one, a {@code List<Object>} of the
	 *         outputs will be given.
	 */
	@OpMethod(op = net.imagej.ops.run.RunByOp.class)
	Object run(Op op, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation and arguments,
	 * populating its inputs.
	 *
	 * @param name The name of the operation. If multiple {@link Op}s share this
	 *          name, then the best {@link Op} implementation to use will be
	 *          selected automatically from the name and arguments.
	 * @param args The operation's arguments.
	 * @return An {@link Op} with populated inputs, ready to run.
	 */
	@OpMethod(op = net.imagej.ops.lookup.LookupByName.class)
	Op op(String name, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation type and arguments,
	 * populating its inputs.
	 *
	 * @param type The {@link Class} of the operation. If multiple {@link Op}s
	 *          share this type (e.g., the type is an interface which multiple
	 *          {@link Op}s implement), then the best {@link Op} implementation to
	 *          use will be selected automatically from the type and arguments.
	 * @param args The operation's arguments.
	 * @return An {@link Op} with populated inputs, ready to run.
	 */
	@OpMethod(op = net.imagej.ops.lookup.LookupByType.class)
	<OP extends Op> OP op(Class<OP> type, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation and arguments,
	 * wrapping it as a {@link Module} with populated inputs.
	 *
	 * @param name The name of the operation.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	Module module(String name, Object... args);

	/**
	 * Gets the best {@link Op} to use for the given operation type and arguments,
	 * wrapping it as a {@link Module} with populated inputs.
	 *
	 * @param type The required type of the operation. If multiple {@link Op}s
	 *          share this type (e.g., the type is an interface which multiple
	 *          {@link Op}s implement), then the best {@link Op} implementation to
	 *          use will be selected automatically from the type and arguments.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the best {@link Op}, with populated
	 *         inputs, ready to run.
	 */
	<OP extends Op> Module module(Class<OP> type, Object... args);

	/**
	 * Wraps the given {@link Op} as a {@link Module}, populating its inputs.
	 *
	 * @param op The {@link Op} to wrap and populate.
	 * @param args The operation's arguments.
	 * @return A {@link Module} wrapping the {@link Op}, with populated inputs,
	 *         ready to run.
	 */
	Module module(Op op, Object... args);

	/** Gets the metadata for a given {@link Op}. */
	CommandInfo info(Op op);

	/** Gets the names of all available operations. */
	Collection<String> ops();

	/** Gets the namespace of the given class. */
	<NS extends Namespace> NS namespace(final Class<NS> nsClass);

	// -- Operation shortcuts - global namespace --

	/** Executes the "convert" operation on the given arguments. */
	@OpMethod(op = Ops.Convert.class)
	Object convert(Object... args);

	/** Executes the "convert" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.convert.ConvertPixClip.class,
		net.imagej.ops.convert.ConvertPixNormalizeScale.class,
		net.imagej.ops.convert.ConvertPixScale.class,
		net.imagej.ops.convert.ConvertPixCopy.class })
	<I extends RealType<I>, O extends RealType<O>> O convert(O out, I in);

	/** Executes the "convert" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.convert.ConvertIterableInterval.class)
	<I extends RealType<I>, O extends RealType<O>> IterableInterval<O>
		convert(IterableInterval<O> out, IterableInterval<I> in,
			ConvertPix<I, O> pixConvert);

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = Ops.Eval.class)
	Object eval(Object... args);

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.eval.DefaultEval.class)
	Object eval(String expression);

	/** Executes the "eval" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.eval.DefaultEval.class)
	Object eval(String expression, Map<String, Object> vars);

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = Ops.Help.class)
	Object help(Object... args);

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpForOp.class)
	String help(Op op);

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	String help();

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	String help(String name);

	/** Executes the "help" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.help.HelpCandidates.class)
	String help(String name, Class<? extends Op> opType);

	/** Executes the "identity" operation on the given arguments. */
	@OpMethod(op = Ops.Identity.class)
	Object identity(Object... args);

	/** Executes the "identity" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.identity.DefaultIdentity.class)
	<A> A identity(A arg);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = Ops.Join.class)
	Object join(Object... args);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinComputerAndComputer.class)
	<A, B, C> C join(C out, A in, ComputerOp<A, B> first, ComputerOp<B, C> second);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinComputerAndComputer.class)
	<A, B, C> C join(C out, A in, ComputerOp<A, B> first, ComputerOp<B, C> second,
		BufferFactory<A, B> bufferFactory);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinInplaceAndInplace.class)
	<A> A join(A arg, InplaceOp<A> first, InplaceOp<A> second);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinComputers.class)
	<A> A join(A out, A in, List<? extends ComputerOp<A, A>> ops,
		BufferFactory<A, A> bufferFactory);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinInplaces.class)
	<A> A join(A arg, List<InplaceOp<A>> ops);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinInplaceAndComputer.class)
	<A, B> B join(B out, A in, InplaceOp<A> first, ComputerOp<A, B> second);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinInplaceAndComputer.class)
	<A, B> B join(B out, A in, InplaceOp<A> first, ComputerOp<A, B> second,
		BufferFactory<A, A> bufferFactory);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinComputerAndInplace.class)
	<A, B> B join(B out, A in, ComputerOp<A, B> first, InplaceOp<B> second);

	/** Executes the "join" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.join.DefaultJoinComputerAndInplace.class)
	<A, B> B join(B out, A in, ComputerOp<A, B> first, InplaceOp<B> second,
		BufferFactory<A, B> bufferFactory);

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = Ops.Loop.class)
	Object loop(Object... args);

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.loop.DefaultLoopInplace.class)
	<I> I loop(I arg, ComputerOp<I, I> op, int n);

	/** Executes the "loop" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.loop.DefaultLoopComputer.class)
	<A> A loop(A out, A in, ComputerOp<A, A> op,
		BufferFactory<A, A> bufferFactory, int n);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = Ops.Map.class)
	Object map(Object... args);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapConvertRAIToRAI.class)
	<A, B extends Type<B>> RandomAccessibleInterval<B> map(
		RandomAccessibleInterval<A> input, ComputerOp<A, B> op, B type);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapConvertRandomAccessToRandomAccess.class)
	<A, B extends Type<B>> RandomAccessible<B> map(RandomAccessible<A> input,
		ComputerOp<A, B> op, B type);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapIterableIntervalToView.class)
	<A, B extends Type<B>> IterableInterval<B> map(IterableInterval<A> input,
		ComputerOp<A, B> op, B type);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapParallel.class)
	<A> IterableInterval<A> map(IterableInterval<A> arg, InplaceOp<A> op);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapIterableToIterableParallel.class,
		net.imagej.ops.map.MapIterableIntervalToIterableInterval.class })
	<A, B> IterableInterval<B> map(IterableInterval<B> out,
		IterableInterval<A> in, ComputerOp<A, B> op);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(ops = { net.imagej.ops.map.MapIterableToRAIParallel.class,
		net.imagej.ops.map.MapIterableIntervalToRAI.class })
	<A, B> RandomAccessibleInterval<B> map(RandomAccessibleInterval<B> out,
		IterableInterval<A> in, ComputerOp<A, B> op);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapIterableInplace.class)
	<A> Iterable<A> map(Iterable<A> arg, InplaceOp<A> op);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapRAIToIterableInterval.class)
	<A, B> IterableInterval<B> map(IterableInterval<B> out,
		RandomAccessibleInterval<A> in, ComputerOp<A, B> op);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.neighborhood.MapNeighborhood.class)
	<I, O> RandomAccessibleInterval<O> map(RandomAccessibleInterval<O> out,
		RandomAccessibleInterval<I> in, ComputerOp<Iterable<I>, O> op, Shape shape);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.map.neighborhood.MapNeighborhoodWithCenter.class)
	<I, O> RandomAccessibleInterval<O> map(RandomAccessibleInterval<O> out,
		RandomAccessibleInterval<I> in, CenterAwareComputerOp<Iterable<I>, O> op,
		Shape shape);

	/** Executes the "map" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.map.MapIterableToIterable.class)
	<A, B> Iterable<B> map(Iterable<B> out, Iterable<A> in, ComputerOp<A, B> op);

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = Ops.Slicewise.class)
	Object slicewise(Object... args);

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.slicewise.SlicewiseRAI2RAI.class)
	<I, O> RandomAccessibleInterval<O> slicewise(RandomAccessibleInterval<O> out,
		RandomAccessibleInterval<I> in, ComputerOp<I, O> op, int... axisIndices);

	/** Executes the "slicewise" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.slicewise.SlicewiseRAI2RAI.class)
	<I, O> RandomAccessibleInterval<O> slicewise(RandomAccessibleInterval<O> out,
		RandomAccessibleInterval<I> in, ComputerOp<I, O> op, int[] axisIndices,
		boolean dropSingleDimensions);

	// -- Operation shortcuts - other namespaces --

	/** Gateway into ops of the "create" namespace. */
	CreateNamespace create();

	/** Gateway into ops of the "deconvolve" namespace. */
	DeconvolveNamespace deconvolve();

	/** Gateway into ops of the "filter" namespace. */
	FilterNamespace filter();

	/** Gateway into ops of the "image" namespace. */
	ImageNamespace image();

	/** Gateway into ops of the "labeling" namespace. */
	LabelingNamespace labeling();

	/** Gateway into ops of the "logic" namespace. */
	LogicNamespace logic();

	/** Gateway into ops of the "math" namespace. */
	MathNamespace math();

	/** Gateway into ops of the "stats" namespace. */
	StatsNamespace stats();

	/** Gateway into ops of the "thread" namespace. */
	ThreadNamespace thread();

	/** Gateway into ops of the "threshold" namespace. */
	ThresholdNamespace threshold();

}
