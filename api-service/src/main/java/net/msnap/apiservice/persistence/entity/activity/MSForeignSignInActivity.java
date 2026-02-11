package net.msnap.apiservice.persistence.entity.activity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "activity_id")
public class MSForeignSignInActivity extends AccountActivity {
    private String aliasUsed;
    private String ipAddress;
    private String deviceOs;
    private String deviceBrowser;
    private String locationInfo;
    private Float locationLatitude;
    private Float locationLongitude;
    @Column(length = 2048)
    private String actionList;
}
