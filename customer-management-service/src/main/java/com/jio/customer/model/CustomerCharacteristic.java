package com.jio.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

/**
 * A key/value characteristic attached to a Customer.
 * Examples:
 *   name="preferredLanguage"  value="Hindi"
 *   name="segment"            value="Youth"
 *   name="loyaltyPoints"      value="4500"
 *
 * TMF629 — stored as a collection table row per customer.
 */
@Embeddable
public class CustomerCharacteristic {

    @NotBlank
    @JsonProperty("name")
    private String name;

    /** Data type of the value: STRING, INTEGER, BOOLEAN, FLOAT */
    @JsonProperty("valueType")
    private String valueType;

    @JsonProperty("value")
    private String value;

    public CustomerCharacteristic() {}

    public CustomerCharacteristic(String name, String value) {
        this.name  = name;
        this.value = value;
        this.valueType = "STRING";
    }

    public String getName()      { return name; }
    public void setName(String name) { this.name = name; }

    public String getValueType()         { return valueType; }
    public void setValueType(String v)   { this.valueType = v; }

    public String getValue()             { return value; }
    public void setValue(String value)   { this.value = value; }
}
