package ru.sberbank.homework10.util;

import java.util.UUID;

public class UUIDGenerator {
    public static int generateUniqueId() {
        UUID idOne = java.util.UUID.randomUUID();
        String str = "" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
}
