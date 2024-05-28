package org.vivi.framework.iexcel.common.readv2;

import java.util.List;

public interface PageReader<T> extends Reader {

    void pageRead(List<RowContext<T>> rowContexts);

    @Override
    default String getType() {
        return Type.PAGE.name();
    }
}
