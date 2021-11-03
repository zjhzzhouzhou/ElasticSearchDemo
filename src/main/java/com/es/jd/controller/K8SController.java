package com.es.jd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@RestController
public class K8SController {

    String result="";

    @RequestMapping(value = "/k8s",method = RequestMethod.GET)
    public String k8s(){
        try {
            //用 getLocalHost() 方法创建的InetAddress的对象
            InetAddress address = InetAddress.getLocalHost();
            result="hostname: "+address.getHostName()+"hostaddress: "+address.getHostAddress();
            System.out.println(); //主机名
            System.out.println(); //主机别名
            System.out.println(); //
            System.out.println("哈啊哈哈");
        }catch(Exception e){
            e.printStackTrace();
        }
        return "hello这就是 K8s <br/> "+result;
    }

}
