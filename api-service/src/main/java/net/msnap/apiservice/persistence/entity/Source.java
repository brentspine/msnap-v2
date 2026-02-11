package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;
import net.msnap.contracts.datatypes.Money;

@Entity
@Table(name = "sources")
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Embedded
    @AttributeOverride(name = "currency", column = @Column(name = "price_currency", length = 3))
    @AttributeOverride(name = "minor", column = @Column(name = "price_minor"))
    private Money price;

    @Column(nullable = false)
    private Long timestamp;

    @Column(length = 4096)
    private String note;

}
