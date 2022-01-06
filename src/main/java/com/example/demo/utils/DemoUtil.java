package com.example.demo.utils;

import java.util.Map;
import java.util.UUID;

/**
 * @author zhouyw
 * @date 2021-06-29
 */
public class DemoUtil {

    public static void main(String[] args) {
        Long userId=1000L;
        int u = userId.intValue();
        System.out.println(u);
        int uId = (int)(long)userId;
        System.out.println(uId);

        StringBuilder deviceIds = new StringBuilder();
        for (int i=0; i<10; i++) {
            deviceIds.append(i).append(",");
        }
        System.out.println(deviceIds);
        deviceIds.deleteCharAt(deviceIds.length()-1);
        System.out.println(deviceIds);

        System.out.println(UUID.randomUUID().toString());
        System.out.println(UUID.randomUUID().toString());
    }

}
