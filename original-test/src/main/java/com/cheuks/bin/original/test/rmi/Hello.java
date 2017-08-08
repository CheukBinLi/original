package com.cheuks.bin.original.test.rmi;

import com.cheuks.bin.original.common.annotation.web.InterfaceDescription;

public interface Hello {
    @InterfaceDescription(value = "", type = InterfaceDescription.TYPE_METHOD, enable = true)
    public String sayHi(String name);

}
