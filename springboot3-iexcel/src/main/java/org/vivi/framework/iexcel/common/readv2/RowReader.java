package org.vivi.framework.iexcel.common.readv2;

@FunctionalInterface
public interface RowReader<T> extends Reader {

    void read(RowContext<T> rowContext);

    @Override
    default String getType() {
        return Type.ROW.name();
    }
}
