package com.cheuks.bin.original.rmi.t;

import com.cheuks.bin.original.common.annotation.rmi.RmiClient;

@RmiClient(serviceImplementation = "ABCDEFG")
public class test {

    public int a(String a, int b, char c, long d, Boolean e) {
        return 0;
    }

}
