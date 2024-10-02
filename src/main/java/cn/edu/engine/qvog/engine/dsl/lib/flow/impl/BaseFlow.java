package cn.edu.engine.qvog.engine.dsl.lib.flow.impl;

import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.db.IDbContext;
import cn.edu.engine.qvog.engine.dsl.lib.flow.IFlow;
import cn.edu.engine.qvog.engine.dsl.lib.flow.strategy.IVertexFlowStrategy;
import cn.edu.engine.qvog.engine.helper.graph.IGraphHelper;

import java.util.*;

public abstract class BaseFlow implements IFlow {
    protected final IVertexFlowStrategy strategy;
    protected IDbContext dbContext = Environment.getInstance().getDbContext();
    protected IGraphHelper helper = Environment.getInstance().getGraphHelper();
    protected List<FlowStream> streams = new ArrayList<>();

    protected ArrayList<String> attrOverEdge;
    protected Set<Long> escapeFromBarrier = new HashSet<>();

    public BaseFlow(IVertexFlowStrategy strategy) {
        this.strategy = strategy;
        this.attrOverEdge = new ArrayList<>();
    }

    public BaseFlow(IVertexFlowStrategy strategy, List<String> attr) {
        this.strategy = strategy;
        this.attrOverEdge = new ArrayList<>(attr);
    }

    public BaseFlow(IVertexFlowStrategy strategy, List<String> attr, Set<Long> escapeFromBarrier) {
        this.strategy = strategy;
        this.attrOverEdge = new ArrayList<>(attr);
        this.escapeFromBarrier = escapeFromBarrier;
    }

    @Override
    public Iterator<FlowStream> iterator() {
        return streams.iterator();
    }

    @Override
    public Set<Long> getEscapeFromBarrier() {
        return this.escapeFromBarrier;
    }

    protected static abstract class BaseFlowBuilder implements IFlow.Builder {
        protected IVertexFlowStrategy strategy;
        protected String attrOverEdge;

        protected Set<Long> escapeFromBarrier = new HashSet<>();

        @Override
        public IFlow.Builder withStrategy(IVertexFlowStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        @Override
        public Builder withAttrOverEdge(String attrOverEdge) {
            this.attrOverEdge = attrOverEdge;
            return this;
        }

        @Override
        public Builder withEscapeFromBarrier(Set<Long> escapeFromBarrier) {
            this.escapeFromBarrier = escapeFromBarrier;
            return this;
        }
    }
}
