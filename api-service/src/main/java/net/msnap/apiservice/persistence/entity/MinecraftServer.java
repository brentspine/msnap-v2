package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "minecraft_server", indexes = {@Index(name = "ix_minecraft_server_internal_name", columnList = "internal_name"), @Index(name = "ix_minecraft_server_server_ip", columnList = "server_ip")})
public class MinecraftServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "server_ip", length = 255, nullable = false)
    private String serverIp;
    @Column(name = "server_port", length = 60, nullable = false)
    private String serverPort;
    @Column(name = "display_name", length = 255, nullable = false)
    private String displayName;
    @Column(name = "internal_name", length = 255, nullable = false)
    private String internalName;
    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MinecraftServerCurrency> currencies = new ArrayList<>();
    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MinecraftServerRank> ranks = new ArrayList<>();
    @OneToMany(mappedBy = "minecraftServer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MinecraftServerAccountInfo> accountInfos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
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

    public List<MinecraftServerCurrency> getCurrencies() {
        return currencies;
    }

    public List<MinecraftServerRank> getRanks() {
        return ranks;
    }

    public List<MinecraftServerAccountInfo> getAccountInfos() {
        return accountInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftServer other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
