package org.vivi.framework.iasync.sample.utils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.List;

public class ExportListUtil {

    public static <S,T> List<T> transform(List<S> source, Class<T> clazz){
        if(CollectionUtils.isEmpty(source)){
            return Lists.newArrayList();
        }
        BeanCopier beanCopier = BeanCopier.create(source.get(0).getClass(), clazz, false);
        Function<S, T> function= s -> {
            T t = null;
            try {
                t = clazz.newInstance();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            beanCopier.copy(s, t, null);
            return t;
        };
        List<T> list = Lists.transform(source,function);
        return list;
    }
}
