package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "outlook_email")
public class OutlookEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "from_address", length = 255, nullable = false)
    private String from;
    @Column(name = "to_address", length = 255, nullable = false)
    private String to;
    @Column(name = "produced_at", nullable = false)
    private Instant producedAt;
    @Column(name = "subject", length = 255, nullable = false)
    private String subject;
    @Column(name = "body_preview", length = 105, nullable = false)
    private String bodyPreview;

    protected OutlookEmail() {
        // JPA
    }

    public OutlookEmail(String from, String to, Instant producedAt, String subject, String bodyPreview) {
        this.from = from;
        this.to = to;
        this.producedAt = producedAt;
        this.subject = subject;
        this.bodyPreview = bodyPreview;
    }

    public Long getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Instant getProducedAt() {
        return producedAt;
    }

    public void setProducedAt(Instant producedAt) {
        this.producedAt = producedAt;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyPreview() {
        return bodyPreview;
    }

    public void setBodyPreview(String bodyPreview) {
        this.bodyPreview = bodyPreview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutlookEmail other)) return false;
        if (id == null) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
