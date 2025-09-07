package com.enotes.project.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseModel {
    private Boolean isActive;
    private Boolean isDeleted;
    private Integer createdBy;
    @Column(insertable = false, updatable = false)
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
}
