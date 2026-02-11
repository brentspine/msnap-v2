package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "proxy_provider")
public class ProxyProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String providerName;

}
