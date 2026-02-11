package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "minecraft_accounts")
public class MinecraftAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internalId;

    @Column(nullable = false)
    private UUID uuid;

    @Column(length = 60, nullable = false)
    private String username;

    @ManyToMany
    @JoinTable(
        name = "minecraft_account_proxies",
        joinColumns = @JoinColumn(name = "minecraft_account_id"),
        inverseJoinColumns = @JoinColumn(name = "cape_id")
    )
    private Set<Cape> capes;

    private Instant createdAt;

    // Server Infos
    @OneToMany
    private Set<MinecraftServerAccountInfo> serverInfos;

    private int minecoins;


}
