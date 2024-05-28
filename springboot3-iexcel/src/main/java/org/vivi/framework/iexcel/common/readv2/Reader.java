package org.vivi.framework.iexcel.common.readv2;


public interface Reader {

    enum Type {
        ROW, PAGE
    }

    String getType();
}
