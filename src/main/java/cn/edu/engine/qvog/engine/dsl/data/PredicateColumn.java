package cn.edu.engine.qvog.engine.dsl.data;

import cn.edu.engine.qvog.engine.core.graph.values.Value;
import cn.edu.engine.qvog.engine.dsl.lib.predicate.IValuePredicate;

import java.util.Iterator;
import java.util.function.Predicate;

public class PredicateColumn extends BaseColumn {
    private final IValuePredicate predicate;

    private PredicateColumn(String name, IValuePredicate predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    @Override
    public void clear() {

    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public void addValue(Object value) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support addValue");
    }

    @Override
    public Object getValue(int index) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support getValue");
    }

    @Override
    public Object getValue(Object key) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support getValue");
    }

    @Override
    public boolean hasIndex() {
        return false;
    }

    @Override
    public boolean containsKey(Predicate<Object> predicate) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support containsKey");
    }

    @Override
    public boolean containsValue(Predicate<Object> predicate) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support containsValue");
    }

    @Override
    public boolean containsValue(Object value) {
        if (value instanceof Value v) {
            return predicate.test(v);
        }
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support value other than Value");
    }

    @Override
    public IColumn duplicate(boolean schemaOnly) {
        return PredicateColumn.builder().withName(name).withPredicate(predicate).build();
    }

    @Override
    public Iterator<Object> iterator() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't support iterator");
    }

    @Override
    public void amend(String newName) {
        name = newName;
    }

    public static Builder builder() {
        return new PredicateColumnBuilder();
    }

    private static class PredicateColumnBuilder implements Builder {
        private String name;
        private IValuePredicate predicate;

        @Override
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder withPredicate(IValuePredicate predicate) {
            this.predicate = predicate;
            return this;
        }

        @Override
        public IColumn build() {
            if (name == null || predicate == null) {
                throw new IllegalStateException("Must set name and predicate for " + this.getClass().getSimpleName());
            }
            return new PredicateColumn(name, predicate);
        }
    }
}
