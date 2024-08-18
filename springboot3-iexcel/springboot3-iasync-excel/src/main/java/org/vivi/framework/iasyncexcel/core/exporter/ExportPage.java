package org.vivi.framework.iasyncexcel.core.exporter;

import java.util.ArrayList;
import java.util.List;

public class ExportPage<T> {

    private Long total;

    private Long size;

    private Long current;

    List<T> records = new ArrayList<>();

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
