package top.walterinkitchen.formula.token;

import lombok.Builder;
import lombok.Getter;
import top.walterinkitchen.formula.Context;
import top.walterinkitchen.formula.util.DecimalFormatter;

import java.math.BigDecimal;

/**
 * decimal token
 *
 * @author walter
 * @date 2022/3/7
 **/
public class DecimalToken implements Operand {
    @Getter
    private final BigDecimal decimal;

    @Builder(setterPrefix = "set", toBuilder = true)
    private DecimalToken(BigDecimal decimal) {
        this.decimal = decimal;
    }

    @Override
    public String toText() {
        return DecimalFormatter.formatDecimal(this.decimal);
    }

    @Override
    public BigDecimal decimalValue(Context context) {
        return this.decimal;
    }
}
