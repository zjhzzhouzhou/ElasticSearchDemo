package com.es.jd.bean.isv;

import lombok.Data;

/**
 * 功能描述:商店信息
 *
 * @Author: zhouzhou
 * @Date: 2020/9/30$ 17:28$
 */
@Data
public class ISVShopInfo {

    public ISVShopInfo() {
    }

    //     shop_id;
    private String shopId;

    //    shop_nick;
    private String shopNick;

    //     user_id;
    private String userId;

    private String avatar;
}
