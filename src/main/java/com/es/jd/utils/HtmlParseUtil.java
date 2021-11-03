package com.es.jd.utils;

import com.es.jd.bean.JdContent;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/7/31$ 17:30$
 */
public class HtmlParseUtil {
    public static void main(String[] args) {
        parseJdProduct("苹果");
    }

    public static List<JdContent> parseJdProduct(String keyword) {
        List<JdContent> contents = Lists.newArrayList();
        String url = "https://search.jd.com/Search?keyword=" + keyword;

        Document document = null;
        try {
            document = Jsoup.parse(new URL(url), 30000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 这边获取商品列表
        Element element = document.getElementById("J_goodsList");
        String data = element.toString();
        // 获取所有的li标签
        Elements els = element.getElementsByTag("li");
        for (Element el : els) {
//            System.out.println(el.html());
            // 遍历每个el
            String imgUrl = el.getElementsByTag("img").eq(0).attr("src");
            if (StringUtils.isEmpty(imgUrl)) {
                imgUrl = el.getElementsByClass("p-img").eq(0).attr("data-lazy-img");
            }
            // 获取商品标题
            String pName = el.getElementsByClass("p-name").eq(0).text();
            // 获取商品价格
            String price = el.getElementsByClass("p-price").eq(0).text();
            if (StringUtils.isEmpty(pName)) {
                continue;
            }
            System.out.println(String.format("图片地址{%s},商品标题{%s},商品价格{%s}", imgUrl, pName, price));
            JdContent jdContent = new JdContent();
            jdContent.setId(UUID.randomUUID().toString());
            jdContent.setPImg(imgUrl);
            jdContent.setPName(pName);
            jdContent.setPrice(price);
            contents.add(jdContent);
        }
        return contents;
    }
}
