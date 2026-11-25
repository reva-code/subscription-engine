package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Request body for POST /product/{id}/pause.
 * resumeDate is mandatory; the 1-90 day window is a business rule
 * validated in ProductService#pauseProduct, not here.
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PauseProductRequestDto {

    @NotNull(message = "resumeDate is required")
    @JsonProperty("resumeDate")
    private LocalDate resumeDate;

    @JsonProperty("reason")
    private String reason;
}
