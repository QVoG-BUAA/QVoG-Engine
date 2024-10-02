package cn.edu.engine.qvog.engine.db.gremlin;

import cn.edu.engine.qvog.engine.helper.JsonHelper;
import cn.edu.engine.qvog.engine.helper.LogProvider;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.json.simple.JSONObject;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class GremlinDb implements IGremlinDb {
    private GraphTraversalSource source = null;
    private DriverRemoteConnection connection = null;

    private GremlinDb(GraphTraversalSource source, DriverRemoteConnection connection) {
        this.source = source;
        this.connection = connection;
    }

    public static GremlinDb connect(JSONObject config) {
        var logger = LogProvider.DEFAULT;
        GraphTraversalSource g;

        var host = JsonHelper.getValue(config, "host");
        var port = JsonHelper.getIntValue(config, "port");
        logger.info("Establishing connection to " + host + ":" + port + "...");
        DriverRemoteConnection connection;
        try {
            connection = DriverRemoteConnection.using(
                    host, port, "g");
            g = traversal().withRemote(connection);
        } catch (Exception e) {
            logger.severe("Failed to establish connection: " + e.getMessage());
            return null;
        }
        logger.info("Connection established.");

        return new GremlinDb(g, connection);
    }

    @Override
    public GraphTraversalSource g() {
        return source;
    }

    @Override
    public GraphTraversal<Vertex, Vertex> V() {
        return source.V();
    }

    @Override
    public GraphTraversal<Edge, Edge> E() {
        return source.E();
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}

