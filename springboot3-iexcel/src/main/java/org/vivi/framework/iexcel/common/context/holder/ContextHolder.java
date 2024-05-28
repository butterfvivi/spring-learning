package org.vivi.framework.iexcel.common.context.holder;


import org.vivi.framework.iexcel.common.context.Context;

import java.util.Optional;


public interface ContextHolder<String, V extends Context> {

    Optional<V> getContext(String key);

    void setContext(String key, V context);

    void clearContext(String key);
}
