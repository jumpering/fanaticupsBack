package org.fanaticups.fanaticupsBack.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBodyCreateCup {

    private String userId;
    //private MultipartFile file;
    private String cup;
}
