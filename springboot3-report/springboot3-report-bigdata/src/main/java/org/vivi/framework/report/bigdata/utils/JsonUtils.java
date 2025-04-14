package org.vivi.framework.report.bigdata.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 简单的json工具类
 */
@Slf4j
public final class JsonUtils {
    private static ObjectMappingCustomer selfMapper = new ObjectMappingCustomer();
    private static ObjectMappingCustomer dynamicSelfMapper = new ObjectMappingCustomer();

    public static class ObjectMappingCustomer extends ObjectMapper {
    	
		private static final long serialVersionUID = 1L;

		private ObjectMappingCustomer() {
            super();
            this.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
    			@Override
    			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
    					throws IOException {
					gen.writeString("");
    			}
    		});
            this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
    			@Override
    			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
    					throws IOException {
					gen.writeString("");
    			}
    		});
        }
    }
    
    static {
    	dynamicSelfMapper.setSerializationInclusion(Include.NON_EMPTY);
    	dynamicSelfMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	dynamicSelfMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    private JsonUtils() {
    }

    public static String objToStr(Object obj) {
        return objToStr(obj, false);
    }

    public static String objToStr(Object obj, boolean writeNull) {
        return objToStr(obj,writeNull,false);
    }

    public static String objToStr(Object obj, boolean writeNull, boolean pretty) {
        try {
            if( writeNull ){
                if( pretty ){
                    return selfMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
                }
                return selfMapper.writeValueAsString(obj);
            }else{
                if( pretty ){
                    return dynamicSelfMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
                }
                return dynamicSelfMapper.writeValueAsString(obj);
            }
        } catch (Exception e) {
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }

    public static <T> T strToObj(String str, Class<T> claz) {
        return strToObj(str, claz, false);
    }

    public static <T> T strToObj(String str, Class<T> claz, boolean writeNull) {
        try {
            return writeNull ? selfMapper.readValue(str, claz) : dynamicSelfMapper.readValue(str, claz);
        } catch (Exception e) {
        	log.error("Error: {},{}",str,e.getMessage());
            return null;
        }
    }

}
