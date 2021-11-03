package com.es.jd.bean.isv;

/**
 * Created by xlee on 17/6/7.
 *
 */
public enum ISVResultCodeEnum {
    /**
     * 结果编码
     */
    SUCCESS("success", "成功"),

    FAILURE("fail", "失败"),

    ARG_ERROR("arg_error", "ISV参数错误"),

    SIGN_ERROR("sign_error", "ISV签名错误"),


    ;

    /** 结果码 */
    private String code;

    /** 结果描述 */
    private String desc;

    /**
     * 获取结果码
     *
     * @param code  待查询code
     * @return  对应的结果码
     */
    public static ISVResultCodeEnum getByCode(String code) {
        for (ISVResultCodeEnum resultCode : ISVResultCodeEnum.values()) {
            if (resultCode.getCode().equalsIgnoreCase(code)) {
                return resultCode;
            }
        }
        return null;
    }

    /**
     * @param code
     * @param desc
     */
    private ISVResultCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
}

