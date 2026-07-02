package com.subscriptionengine.productusage.domain.model;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class UsageConsumption {
    private String id;
    private String href;
    private OffsetDateTime creationDate;
    private String description;
    private OffsetDateTime lastUpdate;
    private String name;
    private String status;
    private List<UsageConsumptionProductRef> product;
    private List<RelatedParty> relatedParty;
}
