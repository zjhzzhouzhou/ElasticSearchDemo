package com.es.jd.utils;



import com.alibaba.nacos.common.util.Md5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/9/29$ 15:54$
 */
public class ISVSignUtil {

    private static final Logger log = LoggerFactory.getLogger(ISVSignUtil.class);

    private static final String SECRET = "txzj666superfans";

    /**
     * MD5加密
     * @param map
     * @param keys
     * @return
     */
    public static String createSign(Map<String, String> map, String keys){
        String sign = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            infoIds.sort(new Comparator<Map.Entry<String, String>>() {

                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).compareTo(o2.getKey());
                }
            });
            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (!(val == "" || val == null)) {
                        sb.append(key +  val);
                    }
                }
            }
            String msg =SECRET + sb.toString() + SECRET;
            //String result = (sb.toString().length()-1)+keys;
            log.info("================accsii排序==============="+msg);
            sign = Md5Utils.getMD5(msg,"UTF-8").toUpperCase();//MD5加密，toUpperCase()：大小写转换
            log.info("================signMD5加密==============="+sign);
        } catch (Exception e) {
            return null;
        }
        return sign;
    }

    public static void main(String[] args) {
        //foo:1, bar:2, foo_bar:3, foobar:4
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("foo","1");
        hashMap.put("bar","2");
        hashMap.put("foo_bar","3");
        hashMap.put("foobar","4");

        String sign = createSign(hashMap, null);
        System.out.println(sign.equals("F32655D5A3DAD5B2A0B8C05754F9A882"));
    }
}
