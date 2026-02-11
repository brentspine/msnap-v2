package net.msnap.contracts.datatypes;

import java.util.Currency;

public record Money(Currency currency, long minor) {

    public static Money zero(Currency currency) {
        return new Money(currency, 0L);
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(currency, Math.addExact(minor, other.minor));
    }

    public Money minus(Money other) {
        requireSameCurrency(other);
        return new Money(currency, Math.subtractExact(minor, other.minor));
    }

    private void requireSameCurrency(Money other) {
        if (currency.equals(other.currency())) { return; }
        throw new IllegalArgumentException("Currency mismatch: " + currency + " vs " + other.currency());
    }
}
