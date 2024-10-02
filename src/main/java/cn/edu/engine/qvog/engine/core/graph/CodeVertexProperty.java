package cn.edu.engine.qvog.engine.core.graph;

import java.io.Serializable;

/**
 * VertexProperty represents a "code" node in the final graph.
 *
 * @param id              The id of the Vertex in graph.
 * @param code            The original line of code.
 * @param lineNumber      The original line number of the code.
 * @param filename        The original file name of the code.
 * @param functionDefName If the code is a function definition, then this
 *                        field is the name of the function. Otherwise, it is null.
 * @param json            The original JSON string of the code.
 */
public record CodeVertexProperty(
        long id,
        String code,
        int lineNumber,
        String filename,
        String functionDefName,
        String json
) implements Serializable {
}
