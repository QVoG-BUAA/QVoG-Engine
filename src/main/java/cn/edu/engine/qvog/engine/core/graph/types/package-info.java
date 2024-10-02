/**
 * All types of the unified AST.
 * <p>
 * Unlike LLVM, which uses singleton to represent equality of types,
 * here we override {@link java.lang.Object#equals(Object)} and
 * {@link java.lang.Object#hashCode()} to make sure that two types with
 * the same content are equal.
 */

package cn.edu.engine.qvog.engine.core.graph.types;