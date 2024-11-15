package cn.edu.engine.qvog.engine.core.graph.values.statements.expressions;

import cn.edu.engine.qvog.engine.core.graph.types.TupleType;
import cn.edu.engine.qvog.engine.core.graph.values.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ArraySubExpression extends Expression {
    private final List<Expression> subList;
    private final Expression array;

    public ArraySubExpression(List<Expression> subList, Expression array) {
        this.subList = subList;
        this.array = array;

        ArrayList<Expression> references = new ArrayList<>(subList);
        references.add(array);
        this.setType(new TupleType(references.stream().map(Expression::getType).toList()));
        this.subList.forEach(t -> t.setParent(this));
        this.array.setParent(this);
    }

    @Override
    public void addChildren(Collection<Value> children) {
        children.addAll(this.subList);
        for (var reference : subList) {
            reference.addChildren(children);
        }

        children.add(this.array);
        this.array.addChildren(children);
    }
    //这个addChildren写的还真是确实没毛病
    //首先要明确Collection<Value> children是从外部传入的一个集合。
    //我们要向这个集合里面添加东西
    //children.addAll(this.subList);就是把subList全部移到children里面
    //然后下面的for循环是为了保证subList里面每个元素里面的类似subList这样嵌套的全都给拿出来。
    //有点类似于递归地把一棵语法树的所有部分全展开放到一个集合里
    //接下来两个也是同样的道理。
    //但是有个不太明白的地方是为什么要分成subList和array？可能和底层的数据结构有关
}
