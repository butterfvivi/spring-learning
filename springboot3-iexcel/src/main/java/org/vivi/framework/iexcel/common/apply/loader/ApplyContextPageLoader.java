package org.vivi.framework.iexcel.common.apply.loader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ApplyContextPageLoader<T> {

    Page<T> load(Pageable pageable);
}
