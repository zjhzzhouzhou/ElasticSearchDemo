package com.es.jd.bean;

import lombok.Data;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/9/30$ 10:38$
 */
@Data
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


}
