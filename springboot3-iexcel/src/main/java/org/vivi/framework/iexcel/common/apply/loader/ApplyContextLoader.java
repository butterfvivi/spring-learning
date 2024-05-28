package org.vivi.framework.iexcel.common.apply.loader;


import org.vivi.framework.iexcel.common.context.ApplyContext;

public interface ApplyContextLoader {

    ApplyContext next(String key);
}
