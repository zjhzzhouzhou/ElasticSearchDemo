package com.es.jd.bean.isv;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/9/30$ 10:38$
 */
public class ISVResultVO<T> {

    private String code;
    private String msg;
    private T data;

    public ISVResultVO() {
        super();
    }

    public ISVResultVO(T result) {
        this("0", "成功", result);
    }

    public ISVResultVO(String code,
                       String message, T result) {
        super();
        this.code = code;
        this.msg = message;
        this.data = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
