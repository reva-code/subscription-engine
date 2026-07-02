package com.subscriptionengine.productusage.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;
import com.subscriptionengine.productusage.domain.model.UsageConsumptionProductRef;
import com.subscriptionengine.productusage.domain.model.RelatedParty;

@Entity
@Table(name = "usage_consumption")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class UsageConsumptionJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "href")
    private String href;

    @CreatedDate
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "description")
    private String description;

    @LastModifiedDate
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "product_ref", columnDefinition = "LONGTEXT")
    private List<UsageConsumptionProductRef> product;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "related_party", columnDefinition = "LONGTEXT")
    private List<RelatedParty> relatedParty;
}
