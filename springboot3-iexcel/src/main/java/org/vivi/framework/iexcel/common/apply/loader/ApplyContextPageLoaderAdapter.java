package org.vivi.framework.iexcel.common.apply.loader;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.vivi.framework.iexcel.common.context.ApplyContext;
import org.vivi.framework.iexcel.common.context.holder.ContextHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class ApplyContextPageLoaderAdapter<T> implements ApplyContextLoader {

    private Page<T> page;

    private final int pageSize;
    private final ApplyContextPageLoader<T> pageContextLoader;

    private ContextHolder<String, ApplyContext> contextHolder;

    public static <T> ApplyContextPageLoaderAdapter<T> getLoader(ApplyContextPageLoader<T> pageContextLoader) {
        return new ApplyContextPageLoaderAdapter<>(50, pageContextLoader);
    }

    public static <T> ApplyContextPageLoaderAdapter<T> getLoader(int pageSize, ApplyContextPageLoader<T> pageContextLoader) {
        return new ApplyContextPageLoaderAdapter<>(pageSize, pageContextLoader);
    }

    private ApplyContextPageLoaderAdapter(int pageSize, ApplyContextPageLoader<T> pageContextLoader) {
        this.pageSize = pageSize;
        this.pageContextLoader = pageContextLoader;
    }

    public void setContextHolder(ContextHolder<String, ApplyContext> contextHolder) {
        this.contextHolder = contextHolder;
    }

    @Override
    public ApplyContext next(String key) {
        Objects.requireNonNull(contextHolder, "ContextHolder must not be null!");

        Optional<ApplyContext> contextOp = contextHolder.getContext(key);

        contextOp.ifPresent(context -> {
            Long index = context.getIndex();

            int indexOfElements = (int) (index - (page.getNumber() * page.getSize()));

            List<T> dataList = page.getContent();

            // load the next data on the slice
            if (indexOfElements < dataList.size() - 1) {

                context.setIndex(index + 1);
                context.setTotal(page.getTotalElements());
                context.setData(dataList.get(indexOfElements + 1));

                // load next page
            } else if (page.hasNext()) {

                page = pageContextLoader.load(page.nextPageable());

                context.setIndex(index + 1);
                context.setTotal(page.getTotalElements());
                context.setData(getFirstElement(page));
            }
        });
        return contextOp.orElseGet(this::firstLoad);
    }

    public T getFirstElement(Page<T> page) {
        return page.getContent().isEmpty() ? null : page.getContent().get(0);
    }

    public ApplyContext firstLoad() {
        page = pageContextLoader.load(PageRequest.of(0, pageSize));
        return ApplyContext.builder()
                .data(getFirstElement(page))
                .index(0L)
                .total(page.getTotalElements())
                .build();
    }
}
