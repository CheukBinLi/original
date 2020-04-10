package com.cheuks.bin.original.rmi.t;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.rmi.t
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-03-30 14:14
 *
 *
 */
public class TestX {

    @Test
    public void sout() {
        System.out.println((15 & 16) == 0);
        System.out.println((94786 & 16));
        ArrayList arrayList = new ArrayList();
        arrayList.add(1);
    }

    @Test
    public void recodeConcurrentHashMap() throws Throwable {
        final int HASH_BITS = 0x7fffffff;
//        return (h ^ (h >>> 16)) & HASH_BITS;

        Object[] tab = new String[32];
        tab[1] = "index_1";

        Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        sun.misc.Unsafe U = (Unsafe) unsafeField.get(null);


        Class<?> ak = String[].class;
        int ABASE = U.arrayBaseOffset(ak);
        int scale = U.arrayIndexScale(ak);
        if ((scale & (scale - 1)) != 0)
            throw new Error("data type scale not a power of two");
        int ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);

        Object o = U.getObjectVolatile(tab, ((long) 1 << ASHIFT) + ABASE);
        Object o1 = U.getObjectVolatile(tab, 1);

        int h, ch, z;
        System.out.println(-91 & 2147483647);
        System.out.println(16 - (16 >>> 2));
        System.out.println(Integer.MAX_VALUE);
        System.out.println(h = "x1".hashCode());
        System.out.println("位移16位" + (ch = "x1".hashCode() >>> 16));
        System.out.println("异或" + (z = (h ^ ch)));
        System.out.println("HASH_BITS:" + HASH_BITS);
        System.out.println("z & HASH_BITS:" + (z & HASH_BITS));
        ConcurrentHashMap a = new ConcurrentHashMap();
        System.out.println((36 << 2) + 16);
        a.put("x1", "x1");
        AtomicInteger atomicInteger = new AtomicInteger(99);
        System.out.println(atomicInteger.compareAndSet(11, 1));
        System.out.println(atomicInteger.compareAndSet(1, 21));
        System.out.println((128 - (128 >>> 2)));
        System.out.println((128 * 0.75));
    }

    @Test
    public synchronized void recodeHashMap() throws NoSuchFieldException, IllegalAccessException {
        Map a = new HashMap(13);
        a.put("a1", "a1");
        a.put("a2", "a3");
        a.put(null, "a-null");
        a.put("a3", "a3");
        a.put("a4", "a4");
        a.put("a5", "a5");
        a.put("a6", "a6");
        a.put("a7", "a7");
        a.put("a8", "a8");
        a.put("a9", "a9");
        a.put("a10", "a10");
        a.put("a11", "a11");
        a.put("a12", "a12");
        a.put("a13", "a13");
        Field table = a.getClass().getDeclaredField("table");
        table.setAccessible(true);
        Object[] node = (Object[]) table.get(a);
        System.out.println(Arrays.toString(node));
        a.get("a1");
        a.put("a13", "a13");
        a.put("a14", "a14");
        a.put("a15", "a15");
        a.put("a16", "a17");

        a = new ConcurrentHashMap();
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        new TestX().recodeHashMap();
    }

}
