package org.fanaticups.fanaticupsBack.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBodyCreateCup {

    private String userId;
    private String cup;
}
