package org.vivi.framework.iasyncexcel.core.model;

import lombok.Data;

import java.util.Map;

@Data
public class DataParam {

    private Map<String, Object> parameters;

    private String createUserCode;

    private String businessCode;
}

