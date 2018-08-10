package com.stip.net.common.service.utils;

import java.util.UUID;

public class KeyGenerator {
	/**
     * 获得一个UUID
     * @return String UUID
     */
    public static String getKey(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
