package org.vivi.framework.iexcel.common.readv2;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.vivi.framework.iexcel.common.core.DataValidator;


@Getter
@Setter
@Builder
public class ReadParam<T> {

    private boolean checkHead;
    private boolean checkCacheRepeat;
    private DataValidator<T> validator;
}
