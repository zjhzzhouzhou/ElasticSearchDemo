package com.es.jd.utils;

/**
 * 功能描述:活动签到帮助类
 *
 * @Author: zhouzhou
 * @Date: 2020/10/9$ 17:34$
 */
public class ActivitySignHelper {

    // 用户加密秘钥
    private static final String USER_NICK_SECRET = "activity_sign___";

    /**
     * 获取用户mix_nick_name 主要用于收藏中心(isv)
     * @param buyerNick
     * @return
     */
    public static String getMixNickName(String buyerNick) throws Exception {
        String encrypt = AESUtil.Encrypt(buyerNick, USER_NICK_SECRET);
        if (encrypt == null){
            throw new RuntimeException("用户加密错误");
        }
        return encrypt;
    }
}
