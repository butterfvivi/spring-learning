package org.vivi.framework.iexcel.common.read;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class BasedExcelReadModel {

    public static final String DATA_REPEAT = "DATA_REPEAT";

    @JsonIgnore
    private Boolean available;

    @JsonIgnore
    private String msg;
}
