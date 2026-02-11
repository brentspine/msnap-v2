package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "outlook_information")
public class OutlookInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
        name = "outlook_information_mails_sent",
        joinColumns = @JoinColumn(name = "outlook_information_id"),
        inverseJoinColumns = @JoinColumn(name = "outlook_email_id")
    )
    @OrderBy("producedAt DESC")
    private List<OutlookEmail> mailsSent = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
        name = "outlook_information_inbox",
        joinColumns = @JoinColumn(name = "outlook_information_id"),
        inverseJoinColumns = @JoinColumn(name = "outlook_email_id")
    )
    @OrderBy("producedAt DESC")
    private List<OutlookEmail> inbox = new ArrayList<>();

    protected OutlookInformation() {
    }

    public Long getId() {
        return id;
    }

    public List<OutlookEmail> getMailsSent() {
        return mailsSent;
    }

    public void setMailsSent(List<OutlookEmail> mailsSent) {
        this.mailsSent = mailsSent;
    }

    public List<OutlookEmail> getInbox() {
        return inbox;
    }

    public void setInbox(List<OutlookEmail> inbox) {
        this.inbox = inbox;
    }

    public void addToSent(OutlookEmail email) {
        mailsSent.add(email);
    }

    public void addToInbox(OutlookEmail email) {
        inbox.add(email);
    }

    public void removeFromSent(OutlookEmail email) {
        mailsSent.remove(email);
    }

    public void removeFromInbox(OutlookEmail email) {
        inbox.remove(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutlookInformation other)) return false;
        if (id == null) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}