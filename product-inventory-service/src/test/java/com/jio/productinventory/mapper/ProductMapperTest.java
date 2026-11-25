package com.jio.productinventory.mapper;

import com.jio.productinventory.dto.ProductDto;
import com.jio.productinventory.entity.Product;
import com.jio.productinventory.entity.ProductStatusType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapperImpl();

    @Test
    void toDto_suspendedProduct_includesPauseReasonAndResumeDate() {
        LocalDate resumeDate = LocalDate.now().plusDays(10);
        Product product = Product.builder()
                .id("prod-1")
                .status(ProductStatusType.suspended)
                .pauseReason("Customer requested pause")
                .resumeDate(resumeDate)
                .build();

        ProductDto dto = mapper.toDto(product);

        assertThat(dto.getPauseReason()).isEqualTo("Customer requested pause");
        assertThat(dto.getResumeDate()).isEqualTo(resumeDate);
        assertThat(dto.getStatus()).isEqualTo("suspended");
    }

    @Test
    void toDto_activeProduct_pauseFieldsNull() {
        Product product = Product.builder()
                .id("prod-2")
                .status(ProductStatusType.active)
                .build();

        ProductDto dto = mapper.toDto(product);

        assertThat(dto.getPauseReason()).isNull();
        assertThat(dto.getResumeDate()).isNull();
    }
}
