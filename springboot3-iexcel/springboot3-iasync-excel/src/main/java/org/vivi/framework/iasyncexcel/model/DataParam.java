package org.vivi.framework.iasyncexcel.model;

import lombok.Data;

import java.util.Map;

@Data
public class DataParam {

    private Map<String, Object> parameters;

    private String createUserCode;

    private String businessCode;
}

