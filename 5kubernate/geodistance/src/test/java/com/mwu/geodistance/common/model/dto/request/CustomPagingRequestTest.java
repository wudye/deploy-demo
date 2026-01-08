package com.mwu.geodistance.common.model.dto.request;

import com.mwu.geodistance.common.model.CustomPaging;
import com.mwu.geodistance.common.model.CustomSorting;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CustomPagingRequestTest {

    @Test
    void toPageable_withSorting_returnsPageableWithSort() {

        // Given
        CustomPaging pagination = CustomPaging.builder()
                .pageNumber(3)
                .pageSize(50)
                .build();

        Sort expectedSort = Sort.by(Sort.Order.desc("updatedAt"))
                .and(Sort.by(Sort.Order.asc("id")));
        CustomSorting sorting = mock(CustomSorting.class);
        when(sorting.toSort()).thenReturn(expectedSort);

        CustomPagingRequest req = CustomPagingRequest.builder()
                .pagination(pagination)
                .sorting(sorting)
                .build();

        // When
        Pageable pageable = req.toPageable();

        // Then
        assertThat(pageable).isInstanceOf(PageRequest.class);
        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(50);

        // Sort equivalence
        assertThat(pageable.getSort()).isEqualTo(expectedSort);
        assertThat(pageable.getSort().isSorted()).isTrue();
        verify(sorting, atLeastOnce()).toSort();

    }

    @Test
    void toPageable_withoutSorting_returnsUnsorted() {

        // Given
        CustomPaging pagination = CustomPaging.builder()
                .pageNumber(1)
                .pageSize(20)
                .build();

        CustomPagingRequest req = CustomPagingRequest.builder()
                .pagination(pagination)
                .sorting(null) // explicitly absent
                .build();

        // When
        Pageable pageable = req.toPageable();

        // Then
        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(20);
        assertThat(pageable.getSort().isUnsorted()).isTrue();

    }

    @Test
    void validation_fails_whenPaginationIsNull() {
        // Given
        CustomPagingRequest req = CustomPagingRequest.builder()
                .pagination(null)
                .sorting(null)
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // When
        Set<ConstraintViolation<CustomPagingRequest>> violations = validator.validate(req);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anySatisfy(v -> {
            // field path is correct
            assertThat(v.getPropertyPath().toString()).isEqualTo("pagination");
            // locale-independent check: template for @NotNull
            assertThat(v.getMessageTemplate())
                    .isEqualTo("{jakarta.validation.constraints.NotNull.message}");
            // message itself may be localized, but should not be blank
            assertThat(v.getMessage()).isNotBlank();
        });
    }

}