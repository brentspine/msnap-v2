package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "security_type")
public class SecurityInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String recoveryCode;

    @Column(length = 255)
    private String primaryEmail;

    @ElementCollection
    @CollectionTable(
        name = "security_information_aliases",
        joinColumns = @JoinColumn(name = "security_information_id")
    )
    @Column(name = "alias", length = 255)
    private List<String> aliases = new ArrayList<>();

    @Column(length = 255)
    private String aliasPhone;

    @Column(length = 255)
    private String appPassword;

}

