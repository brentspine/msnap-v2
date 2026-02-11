package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;
import net.msnap.contracts.datatypes.Money;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "microsoft_orders")
public class MicrosoftOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID internalId;

    @Column(length = 255)
    private String address;

    @Column(length = 512)
    private String displayName;

    @Embedded
    @AttributeOverride(name = "currency", column = @Column(name = "price_currency", length = 3))
    @AttributeOverride(name = "minor", column = @Column(name = "price_minor"))
    private Money price;

    private Instant orderDate;

    @Column(length = 255)
    private String orderNumber;

}
