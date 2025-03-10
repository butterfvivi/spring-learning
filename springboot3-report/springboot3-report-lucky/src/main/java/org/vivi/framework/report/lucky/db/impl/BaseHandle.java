package org.vivi.framework.report.lucky.db.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.vivi.framework.report.lucky.utils.SnowFlake;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库连接
 * @author Administrator
 */
@Slf4j
@Component
public class BaseHandle {

    @Resource(name = "mysqlJdbcTemplate")
    protected JdbcTemplate luckySheetJdbcTemplate;


    @Resource(name = "snowFlake")
    protected SnowFlake snowFlake;

    /**
     * 字符串位置处理
     * x,y,z  -> x.y.z
     * x,1,2  -> x[1].z
     * @param str
     * @return
     */
    protected String positionHandle(String str){
        if(str==null||str.indexOf(",")==-1){
            return str;
        }

        String[] strs=str.split(",");
        String result="";
        for(String s:strs){
            if(result.length()==0){
                result=s;
            }else{
                if(isNumeric(s)){
                    result+="["+s+"]";
                }else{
                    result+="."+s;
                }
            }
        }
        return result;
    }


    private Pattern patternIsNumeric = Pattern.compile("[0-9]*");
    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    protected  boolean isNumeric(String str){
        Matcher isNum = patternIsNumeric.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
