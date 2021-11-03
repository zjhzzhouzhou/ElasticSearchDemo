package com.es.jd.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.es.jd.bean.ISVResultVO;
import com.es.jd.bean.isv.ISVResultCodeEnum;
import com.es.jd.bean.isv.ISVShopInfo;
import com.es.jd.utils.ActivitySignHelper;
import com.es.jd.utils.HttpUtils;
import com.es.jd.utils.ISVSignUtil;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述: 调用isv接口的service
 *
 * @Author: zhouzhou
 * @Date: 2020/9/29$ 16:33$
 */

public class ISVClient {

    private static final Logger levelLogger = LoggerFactory.getLogger(ISVClient.class);


    private static final String ISV_IP = "http://47.92.71.236/super_fans";



    @Test
    public void testGetNeedFollowShops() throws Exception {
        List<ISVShopInfo> isvResultVO = getNeedFollowShops("12345");
        System.out.println(isvResultVO);
    }

    @Test
    public void testWriteOff() throws Exception {
        Boolean aBoolean = writeOff("12345", 370119986L);
        Boolean bBoolean = writeOff("12345", 106763467L);
        Boolean cBoolean = writeOff("12345", 112421865L);
        System.out.println("" + aBoolean + bBoolean + cBoolean);
    }

    @Test
    public void testMeetSendFlowCondition() throws Exception {
        Boolean meetSendFlowCondition = isMeetSendFlowCondition("12345");
        System.out.println(meetSendFlowCondition);
    }


    public static List<ISVShopInfo> getNeedFollowShops(String userNick) throws Exception {


        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
        paramMap.put("mix_nick", ActivitySignHelper.getMixNickName(userNick));

        Map<String, String> tempMap = new HashMap<>(paramMap);
        String sign = getISVSign(tempMap);
        paramMap.put("sign", sign);

        String resultStr = HttpUtils.sendPost(ISV_IP + "/get_fav_shop", paramMap);

        ISVResultVO<JSONObject> resultVO = convertToObject(resultStr, JSONObject.class);
        if (ISVResultCodeEnum.SUCCESS.getCode().equals(resultVO.getCode())) {
            JSONObject listData = resultVO.getData();
            JSONArray jsonArray = (JSONArray) listData.get("shop_info");
            List<ISVShopInfo> shopInfos = Lists.newArrayList();

            for (Object o : jsonArray) {
                ISVShopInfo isvShopInfo = new ISVShopInfo();
                JSONObject object = (JSONObject) o;
                isvShopInfo.setAvatar((String)object.get("avatar"));
                isvShopInfo.setShopId((String)object.get("shop_id"));
                isvShopInfo.setUserId((String)object.get("user_id"));
                isvShopInfo.setShopNick((String)object.get("shop_nick"));
                shopInfos.add(isvShopInfo);
            }

            return shopInfos;
        } else {
            throw new RuntimeException(resultVO.getMsg());
        }

    }


    /**
     * 根据用户nick核销店铺
     *
     * @param userNick
     * @return
     * @throws Exception
     */
    public static Boolean writeOff(String userNick, Long shopId) throws Exception {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
        paramMap.put("shop_id", shopId + "");
        paramMap.put("mix_nick", ActivitySignHelper.getMixNickName(userNick));

        Map<String, String> tempMap = new HashMap<>(paramMap);
        String sign = getISVSign(tempMap);
        paramMap.put("sign", sign);


        String resultStr = HttpUtils.sendPost(ISV_IP + "/verification", paramMap);

        ISVResultVO resultVO = convertToObject(resultStr, null);
        if (ISVResultCodeEnum.SUCCESS.getCode().equals(resultVO.getCode())) {
            return true;
        } else {
            throw new RuntimeException(resultVO.getMsg());
        }
    }


    /**
     * 是否满足核销发送流量条件
     *
     * @param userNick
     * @return
     * @throws Exception
     */
    public static Boolean isMeetSendFlowCondition(String userNick) throws Exception {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
        paramMap.put("mix_nick", ActivitySignHelper.getMixNickName(userNick));

        Map<String, String> tempMap = new HashMap<>(paramMap);
        String sign = getISVSign(tempMap);
        paramMap.put("sign", sign);


        // 创建post请求

        String resultStr = HttpUtils.sendPost(ISV_IP + "/check_send_flow", paramMap);
        ISVResultVO resultVO = convertToObject(resultStr, null);
        if (ISVResultCodeEnum.SUCCESS.getCode().equals(resultVO.getCode())) {
            return true;
        } else {
            throw new RuntimeException(resultVO.getMsg());
        }

    }

    // 获取sign
    private static String getISVSign(Map<String, String> paramMap) {
        return ISVSignUtil.createSign(paramMap, null);
    }


    /**
     * 转换类
     *
     * @param content
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> ISVResultVO<T> convertToObject(String content, Class<T> clazz) {
        ISVResultVO ISVResultVO = JSON.parseObject(content, ISVResultVO.class);
        Object data = ISVResultVO.getData();
        if (data != null) {
            T convertData = (T) data;
            ISVResultVO.setData(convertData);
        }
        return ISVResultVO;
    }


}
