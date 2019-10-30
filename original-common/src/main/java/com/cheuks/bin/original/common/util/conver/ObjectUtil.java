package com.cheuks.bin.original.common.util.conver;

public class ObjectUtil {

    public static <T extends Object> T defaultNull(T t, T defValue) {
        return null == t ? defValue : t;
    }

    public static boolean anyNull(Object... params) {
        return !anyNotNull(params);
    }

    public static boolean anyNotNull(Object... params) {
        if (null == params || params.length < 1) {
            return false;
        }
        for (Object item : params) {
            if (null == item)
                return false;
        }
        return true;
    }

    public static boolean equals(Object target, Object... params) {
        if (params == null)
            return target == null;
        for (Object item : params) {
            if (null == item) {
                if (target == null) {
                    return true;
                }
            } else {
                if (item.equals(target))
                    return true;
            }

        }
        return false;
    }

    public static boolean equalsInt(int target, Integer... params) {
        if (null == params)
            return false;
        for (int item : params) {
            if (target == item)
                return true;
        }
        return false;
    }


}
