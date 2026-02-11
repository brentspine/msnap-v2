package net.msnap.apiservice.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@DiscriminatorValue("INITIAL")
@PrimaryKeyJoinColumn(name = "security_information_id")
public class InitialSecurityInformation extends SecurityInformation {

    @Column(length = 255)
    private String mailUrl;

    @Column(length = 255)
    private String mailPassword;

}
