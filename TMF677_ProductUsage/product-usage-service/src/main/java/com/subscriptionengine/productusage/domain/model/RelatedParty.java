package com.subscriptionengine.productusage.domain.model;

import lombok.Data;

@Data
public class RelatedParty {
    private String id;
    private String href;
    private String name;
    private String role;
    private String referredType;
}
