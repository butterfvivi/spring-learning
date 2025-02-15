package org.vivi.framework.generator.ddl.translate.translator;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
class TransOutVO {
    private String from;

    private String to;

    private List<TransResultDTO> transResult;

}