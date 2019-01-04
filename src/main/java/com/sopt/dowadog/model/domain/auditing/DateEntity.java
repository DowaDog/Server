package com.sopt.dowadog.model.domain.auditing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Data
public class DateEntity{


    //@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false, updatable=false)
    @CreatedDate
    private LocalDateTime createdAt;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    @LastModifiedDate
    private LocalDateTime updatedAt;


}
