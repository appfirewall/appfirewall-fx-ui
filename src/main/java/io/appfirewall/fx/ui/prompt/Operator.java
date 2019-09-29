package io.appfirewall.fx.ui.prompt;

import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * Class that holds to _What_ the Rule should apply.
 */
public class Operator {
    private final Operand operand;
    private final String text;
    private final Type type;

    public Operator(Operand operand, String text, Type type) {
        this.operand = Preconditions.checkNotNull(operand);
        this.text = Preconditions.checkNotNull(text);
        this.type = Preconditions.checkNotNull(type);
    }

    public Operand getOperand() {
        return operand;
    }

    public String getText() {
        return text;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator = (Operator) o;
        return operand == operator.operand &&
                text.equals(operator.text) &&
                type == operator.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand, text, type);
    }
}
