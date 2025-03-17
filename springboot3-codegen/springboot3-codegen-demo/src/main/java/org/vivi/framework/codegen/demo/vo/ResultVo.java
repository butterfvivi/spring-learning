package cn.molu.generator.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author 陌路
 * @apiNote 统一结果返回
 * @date 2024/1/17 13:09
 * @tool Created by IntelliJ IDEA
 */
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVo<T> extends HashMap<String, Object> implements Serializable {

    @Serial
    private static final long serialVersionUID = 2637614641937282252L;

    private Integer code;
    private String message;
    private T data;

    public ResultVo(Integer code, String message, T data, boolean flag) {
        this.code = code;
        this.message = message;
        this.data = data;
        put("code", code);
        put("msg", message);
        put("data", data);
    }

    public static <T> ResultVo<T> ok(Integer code, String msg, T data) {
        return new ResultVo<>(code, msg, data, true);
    }

    public static <T> ResultVo<T> ok(String msg, T data) {
        return new ResultVo<>(200, msg, data, true);
    }

    public static <T> ResultVo<T> ok(T data) {
        return new ResultVo<>(200, "操作成功！", data, true);
    }

    public static <T> ResultVo<T> ok() {
        return new ResultVo<>(200, "操作成功！", null, true);
    }

    public static <T> ResultVo<T> fail(Integer code, String msg) {
        return new ResultVo<>(code, msg, null, false);
    }

    public static <T> ResultVo<T> fail(String msg) {
        return new ResultVo<>(500, msg, null, false);
    }

    public static <T> ResultVo<T> set(String key, Object value) {
        return set(key, value, init());
    }

    public static <T> ResultVo<T> success(T data) {
        return ok(data);
    }

    public ResultVo<T> add(String key, Object value) {
        return set(key, value);
    }

    public static <T> ResultVo<T> init() {
        return new ResultVo<>(null, null, null, true);
    }

    private static <T> ResultVo<T> set(String key, Object value, ResultVo<T> resultVo) {
        resultVo.put(key, value);
        return resultVo;
    }
}
