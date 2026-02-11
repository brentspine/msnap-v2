package net.msnap.apiservice.persistence.entity.activity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AccountActivity {
    @Id
    @GeneratedValue
    private Long id;
    private Instant doneAt;
    private boolean own;
}
