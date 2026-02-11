package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;
import net.msnap.contracts.datatypes.Money;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "microsoft_additional_info")
public class MicrosoftAdditionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String firstName;

    @Column(length = 255)
    private String lastName;

    @Column(length = 255)
    private String region;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(length = 1024)
    private String profileImageUrl;

    @Embedded
    @AttributeOverride(name = "currency", column = @Column(name = "ms_wallet_balance_currency", length = 3))
    @AttributeOverride(name = "minor", column = @Column(name = "ms_wallet_balance_minor"))
    private Money msWalletBalance;

    private int msPoints;

    @OneToMany
    @JoinColumn(name = "payment_card_id")
    private Set<PaymentCard> paymentCards;

    private List<String> gamesOwned;

    @Column(length = 4096)
    private String notes;
}
