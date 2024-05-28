package org.vivi.framework.iexcel.common.context;

import lombok.*;


@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ApplyContext implements Context {

    private Object data;

    private Long index;

    private Long total;
}
