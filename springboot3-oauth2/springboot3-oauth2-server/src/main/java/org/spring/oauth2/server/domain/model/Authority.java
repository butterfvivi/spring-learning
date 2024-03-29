package org.spring.oauth2.server.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    private Integer id;

    @NonNull
    private String authority;

}
