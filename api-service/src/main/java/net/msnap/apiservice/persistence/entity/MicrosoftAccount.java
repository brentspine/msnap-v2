package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;
import net.msnap.apiservice.persistence.entity.activity.AccountActivity;
import net.msnap.contracts.datatypes.MicrosoftAccountActiveState;
import net.msnap.contracts.datatypes.MinecraftLicenseType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "microsoft_accounts")
public class MicrosoftAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID internalId;

    @Enumerated(EnumType.STRING)
    private MinecraftLicenseType licenseType;

    @OneToOne(cascade = CascadeType.ALL)
    private SecurityInformation securityInformation;

    @OneToOne
    private InitialSecurityInformation initialSecurityInformation;

    @OneToOne
    private OutlookInformation outlookInformation;

    @ManyToOne
    private Source source;

    @ManyToMany
    @JoinTable(
        name = "microsoft_account_proxies",
        joinColumns = @JoinColumn(name = "microsoft_account_id"),
        inverseJoinColumns = @JoinColumn(name = "proxy_id")
    )
    private Set<Proxy> proxies;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = true)
    private MinecraftAccount minecraftAccount;

    @Enumerated
    private MicrosoftAccountActiveState status;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<AccountActivity> activities;

    private Instant securedAt;

}

