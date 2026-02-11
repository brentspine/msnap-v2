package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

import java.beans.Transient;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "minecraft_server_account_info", indexes = {@Index(name = "ix_msai_server_account", columnList = "minecraft_server_id,minecraft_account_id")})
public class MinecraftServerAccountInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "minecraft_server_id", nullable = false)
    private MinecraftServer minecraftServer;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "minecraft_account_id", nullable = false)
    private MinecraftAccount minecraftAccount;
    @Column(name = "playtime_minutes", nullable = false)
    private int playtimeMinutes;
    @Column(name = "unban_timestamp")
    private Instant unbanTimestamp;
    @OneToMany(mappedBy = "accountInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MinecraftServerAccountCurrency> currencies = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "highest_rank_id")
    private MinecraftServerRank highestRank;

    @Transient
    public boolean isBanned() {
        if (unbanTimestamp == null) {
            return false;
        }
        return unbanTimestamp.isAfter(Instant.now());
    }

    public Long getId() {
        return id;
    }

    public MinecraftServer getMinecraftServer() {
        return minecraftServer;
    }

    public void setMinecraftServer(MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    public MinecraftAccount getMinecraftAccount() {
        return minecraftAccount;
    }

    public void setMinecraftAccount(MinecraftAccount minecraftAccount) {
        this.minecraftAccount = minecraftAccount;
    }

    public int getPlaytimeMinutes() {
        return playtimeMinutes;
    }

    public void setPlaytimeMinutes(int playtimeMinutes) {
        this.playtimeMinutes = playtimeMinutes;
    }

    public Instant getUnbanTimestamp() {
        return unbanTimestamp;
    }

    public void setUnbanTimestamp(Instant unbanTimestamp) {
        this.unbanTimestamp = unbanTimestamp;
    }

    public List<MinecraftServerAccountCurrency> getCurrencies() {
        return currencies;
    }

    public MinecraftServerRank getHighestRank() {
        return highestRank;
    }

    public void setHighestRank(MinecraftServerRank highestRank) {
        this.highestRank = highestRank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftServerAccountInfo other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
