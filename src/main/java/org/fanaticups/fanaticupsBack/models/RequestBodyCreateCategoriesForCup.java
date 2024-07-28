package org.fanaticups.fanaticupsBack.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RequestBodyCreateCategoriesForCup {
    Long cupId;
    Long[] categories;

}
