package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "minecraft_server_account_currency", uniqueConstraints = {@UniqueConstraint(name = "uq_msac_account_currency", columnNames = {"account_info_id", "currency_id"})})
public class MinecraftServerAccountCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_info_id", nullable = false)
    private MinecraftServerAccountInfo accountInfo;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private MinecraftServerCurrency currency;
    @Column(name = "amount", nullable = false)
    private int amount;

    public Long getId() {
        return id;
    }

    public MinecraftServerAccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(MinecraftServerAccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public MinecraftServerCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(MinecraftServerCurrency currency) {
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftServerAccountCurrency other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
