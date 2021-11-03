package com.es.jd.bean.isv;

/**
 * Created by xlee on 17/6/7.
 *
 */
public enum ISVResultCodeEnum {
    /**
     * �������
     */
    SUCCESS("success", "�ɹ�"),

    FAILURE("fail", "ʧ��"),

    ARG_ERROR("arg_error", "ISV��������"),

    SIGN_ERROR("sign_error", "ISVǩ������"),


    ;

    /** ����� */
    private String code;

    /** ������� */
    private String desc;

    /**
     * ��ȡ�����
     *
     * @param code  ����ѯcode
     * @return  ��Ӧ�Ľ����
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

