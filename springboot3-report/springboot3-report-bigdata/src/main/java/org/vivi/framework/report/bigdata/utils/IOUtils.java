package org.vivi.framework.report.bigdata.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IOUtils {
    /**关闭文件流**/
    public static void closeQuietly(Closeable... closeables) {
        if( null != closeables && closeables.length > 0){
            for (Closeable closeable : closeables) {
                try{
                    if( null != closeable ){
                        closeable.close();
                    }
                } catch (Exception e) {
                    log.error("Error while closing", e);
                }
            }
        }
    }
}
