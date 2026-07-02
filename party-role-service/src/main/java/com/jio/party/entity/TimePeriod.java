package com.jio.party.entity;

import jakarta.persistence.Embeddable;
import java.time.OffsetDateTime;

@Embeddable
public class TimePeriod {

    private OffsetDateTime startDateTime;
    private OffsetDateTime endDateTime;

    public OffsetDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(OffsetDateTime startDateTime) { this.startDateTime = startDateTime; }

    public OffsetDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(OffsetDateTime endDateTime) { this.endDateTime = endDateTime; }
}
