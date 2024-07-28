package org.fanaticups.fanaticupsBack.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fanaticups.fanaticupsBack.dao.entities.CategoryEntity;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CategoriesCupDTO {
    private Long cupId;
    private List<CategoryEntity> categories;
}
