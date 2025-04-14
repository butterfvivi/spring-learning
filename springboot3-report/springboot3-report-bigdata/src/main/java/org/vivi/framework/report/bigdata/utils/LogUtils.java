/*
 *
 * Copyright 2017-2018 549477611@qq.com(xiaoyu)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.vivi.framework.report.bigdata.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Objects;


/**
 * 后续,所有的日志打印都需要使用这个工具类
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogUtils {

    public static void debug(Logger logger, String format, Object message) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, message);
        }
    }
    public static void debug(Logger logger, String format, Object...message) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, message);
        }
    }
    public static void debug(Logger logger, Object message) {
        if (logger.isDebugEnabled()) {
            logger.debug(Objects.toString(message));
        }
    }
    public static void info(Logger logger, String format, Object message) {
        if (logger.isInfoEnabled()) {
            logger.info(format, message);
        }
    }
    public static void info(Logger logger, String format, Object...message) {
        if (logger.isInfoEnabled()) {
            logger.info(format, message);
        }
    }
    public static void info(Logger logger, Object message) {
        if (logger.isInfoEnabled()) {
            logger.info(Objects.toString(message));
        }
    }
    public static void error(Logger logger, String format, Object message) {
        if (logger.isErrorEnabled()) {
            logger.error(format, message);
        }
    }
    public static void error(Logger logger, String format, Object...message) {
        if (logger.isErrorEnabled()) {
            logger.error(format, message);
        }
    }
    public static void error(Logger logger, Object message) {
        if (logger.isErrorEnabled()) {
            logger.error(Objects.toString(message));
        }
    }
    public static void warn(Logger logger, String format, Object message) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, message);
        }
    }
    public static void warn(Logger logger, Object message) {
        if (logger.isWarnEnabled()) {
            logger.warn(Objects.toString(message));
        }
    }

    public static void debug(String format, Object message) {
        if (log.isDebugEnabled()) {
            log.debug(format, message);
        }
    }
    public static void debug(String format, Object...message) {
        if (log.isDebugEnabled()) {
            log.debug(format, message);
        }
    }
    public static void debug(Object message) {
        if (log.isDebugEnabled()) {
            log.debug(Objects.toString(message));
        }
    }
    public static void info(String format, Object message) {
        if (log.isInfoEnabled()) {
            log.info(format, message);
        }
    }
    public static void info(String format, Object...message) {
        if (log.isInfoEnabled()) {
            log.info(format, message);
        }
    }
    public static void info(Object message) {
        if (log.isInfoEnabled()) {
            log.info(Objects.toString(message));
        }
    }
    public static void error(String format, Object message) {
        if (log.isErrorEnabled()) {
            log.error(format, message);
        }
    }
    public static void error(String format, Object...message) {
        if (log.isErrorEnabled()) {
            log.error(format, message);
        }
    }
    public static void error(Object message) {
        if (log.isErrorEnabled()) {
            log.error(Objects.toString(message));
        }
    }
    public static void warn(String format, Object message) {
        if (log.isWarnEnabled()) {
            log.warn(format, message);
        }
    }
    public static void warn(Object message) {
        if (log.isWarnEnabled()) {
            log.warn(Objects.toString(message));
        }
    }

}
