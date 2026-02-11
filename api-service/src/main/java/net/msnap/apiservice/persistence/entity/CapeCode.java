package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "cape_code")
public class CapeCode {
    @Id
    @Column(name = "code", length = 120, nullable = false, updatable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cape_alias", referencedColumnName = "alias", nullable = false)
    private Cape cape;

    @ManyToOne()
    private Source source;

    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "redeemed_at")

    private Instant redeemedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "redeemed_by_id")
    private MinecraftAccount redeemedBy;

    protected CapeCode() {
    }

    public CapeCode(String code, Cape cape, Source source) {
        this.code = code;
        this.cape = cape;
        this.source = source;
    }

    @PrePersist
    public void prePersist() {
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Cape getCape() {
        return cape;
    }

    public void setCape(Cape cape) {
        this.cape = cape;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(Instant redeemedAt) {
        this.redeemedAt = redeemedAt;
    }

    public MinecraftAccount getRedeemedBy() {
        return redeemedBy;
    }

    public void setRedeemedBy(MinecraftAccount redeemedBy) {
        this.redeemedBy = redeemedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CapeCode other)) return false;
        return Objects.equals(code, other.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
