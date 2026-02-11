package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "microsoft_payment_cards")
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false)
    private String type;

    @Column(length = 255, nullable = false)
    private String display;

    @Column(length = 512, nullable = false)
    private String address;

}
