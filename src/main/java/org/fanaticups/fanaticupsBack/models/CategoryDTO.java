package org.fanaticups.fanaticupsBack.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    @NotNull
    private String name;

    private String image;
}
