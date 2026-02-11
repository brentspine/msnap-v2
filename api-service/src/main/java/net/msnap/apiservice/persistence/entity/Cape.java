package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cape")
public class Cape {
    @Id
    @Column(name = "alias", length = 80, nullable = false, updatable = false)
    private String alias;
    @Column(name = "display_name", length = 120, nullable = false)
    private String displayName;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "image", length = 1024)
    private String image;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    @Column(name = "allows_codes", nullable = false)
    private boolean allowsCodes;
    @OneToMany(mappedBy = "cape", fetch = FetchType.LAZY)
    private Set<CapeCode> codes = new HashSet<>();

    protected Cape() {
    }

    public Cape(String alias) {
        this.alias = alias;
    }

    @PrePersist
    public void prePersist() {
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isAllowsCodes() {
        return allowsCodes;
    }

    public void setAllowsCodes(boolean allowsCodes) {
        this.allowsCodes = allowsCodes;
    }

    public Set<CapeCode> getCodes() {
        return codes;
    }

    public void setCodes(Set<CapeCode> codes) {
        this.codes = codes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cape other)) return false;
        return Objects.equals(alias, other.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias);
    }
}
