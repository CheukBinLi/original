package com.cheuks.bin.original.oauth.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.cheuks.bin.original.common.util.conver.JsonMapper;

public class ExceptionPrinterUtil {

    private static final ExceptionPrinterUtil INSTANCE = new ExceptionPrinterUtil();

    public static ExceptionPrinterUtil instance() {
        return INSTANCE;
    }

    private JsonMapper jsonMapper = JsonMapper.newInstance(true);

    public void write(HttpServletResponse response, Object o, String encode) throws Exception {
        response.addHeader("Content-type", "application/json");
        response.setContentType("application/json");
        response.setCharacterEncoding(null == encode ? "utf-8" : encode);
        PrintWriter writer = response.getWriter();
        writer.write(jsonMapper.writer(o, null, true, true, true));
        writer.flush();
    }

    public void writeString(HttpServletResponse response, String o, String encode) throws IOException {
        response.addHeader("Content-type", "application/json");
        response.setContentType("application/json");
        response.setCharacterEncoding(null == encode ? "utf-8" : encode);
        PrintWriter writer = response.getWriter();
        writer.write(o);
        writer.flush();
    }

}
