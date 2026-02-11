package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;
import net.msnap.contracts.datatypes.ProxyRegion;

@Entity
@Table(name = "proxies")
public class Proxy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String ip;

    @Column(nullable = false)
    private int port;

    @Enumerated
    private ProxyRegion region;

    @Column(length = 100)
    private String username;

    @Column(length = 120)
    private String password;

    @Column(nullable = false)
    private boolean isActive;

}
