package net.msnap.apiservice.persistence.entity.activity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "activity_id")
public class DetailsChangedActivity extends AccountActivity {
    private String field;
    private String oldValue;
    private String newValue;
}
