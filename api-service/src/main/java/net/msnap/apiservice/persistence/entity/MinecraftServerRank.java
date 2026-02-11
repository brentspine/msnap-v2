package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "minecraft_server_rank", indexes = {@Index(name = "ix_msr_server_internal_name", columnList = "server_id,internal_name")})
public class MinecraftServerRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", nullable = false)
    private MinecraftServer server;
    @Column(name = "display_name", length = 255, nullable = false)
    private String displayName;
    @Column(name = "internal_name", length = 255, nullable = false)
    private String internalName;

    public Long getId() {
        return id;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftServerRank other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
