package main.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {

    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        this.amount = Objects.requireNonNull(amount, "amount").setScale(2, RoundingMode.UNNECESSARY);
        this.currency = Objects.requireNonNull(currency, "currency").trim().toUpperCase();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money other = (Money) o;
        return amount.equals(other.amount) && currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

 /*

 Тут у нас hashCode не зависит от содержимого объекта, в отличие от equals, при данной реализации одинаковые объекты
 получат разный хэшкод и попадут в разные бакеты в мапе или сете, хотя по equals() будут равны

    @Override
    public int hashCode() {
        return System.identityHashCode(this);

    }*/

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}