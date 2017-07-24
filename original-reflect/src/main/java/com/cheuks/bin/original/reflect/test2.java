package com.cheuks.bin.original.reflect;

import com.cheuks.bin.original.common.annotation.rmi.RmiServer;

@RmiServer(serviceName = "OH_SHIT")
public class test2 implements test2I {

    /*
     * (non-Javadoc)
     * 
     * @see com.cheuks.bin.original.reflect.test2I#a(java.lang.String, int,
     * char, long, java.lang.Boolean)
     */
    public int a(String a, int b, char c, long d, Boolean e) {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cheuks.bin.original.reflect.test2I#a1(java.lang.String, int,
     * char, long)
     */
    public int a1(String a, int b, char c, long d) {
        return 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cheuks.bin.original.reflect.test2I#a2(java.lang.String, int,
     * char)
     */
    public int a2(String a, int b, char c) {
        return 2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cheuks.bin.original.reflect.test2I#a3(java.lang.String, int)
     */
    public int a3(String a, int b) {
        return 3;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cheuks.bin.original.reflect.test2I#a4(java.lang.String)
     */
    public StringBuilder a4(String a) {
        return new StringBuilder(a);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cheuks.bin.original.reflect.test2I#a5()
     */
    public int a5() {
        return 5;
    }

    public static void main(String[] args) {
        int i = 10;
        String a = null;
        while (i-- > 0 && a == null) {
            System.err.println(i);
        }
    }

}
