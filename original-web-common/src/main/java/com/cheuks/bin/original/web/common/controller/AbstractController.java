package com.cheuks.bin.original.web.common.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.dbmanager.LogicStatus;
import com.cheuks.bin.original.common.util.conver.CollectionUtil;
import com.cheuks.bin.original.common.util.conver.JsonMsgModel;
import com.cheuks.bin.original.common.util.conver.ObjectFill;
import com.cheuks.bin.original.common.web.common.controller.BaseController;

public abstract class AbstractController<ModelAndView> extends ObjectFill implements BaseController<HttpServletRequest, HttpServletResponse, JsonMsgModel, ModelAndView> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

    protected int errorCode = -1;

    protected int success = 0;

    protected String errorPageUrl = "/error";

    private static volatile String pageNumber = "pageNumber";

    protected Map<String, Object> checkPageAndSize(HttpServletRequest request) {
        return checkPageAndSize(getParams(request));
    }

    protected Map<String, Object> checkPageAndSize(final Map<String, Object> params) {
        if (!params.containsKey(pageNumber)) {
            params.put(pageNumber, -1);
            params.put(pageSize, -1);
        }
        if (!params.containsKey("logicStatus")) {
            params.put("logicStatus", LogicStatus.NORMAL);
        }
        return params;
    }

    private static volatile String pageSize = "pageSize";

    protected String paramsToUrl(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("?");
        for (Entry<String, Object> en : params.entrySet()) {
            sb.append(en.getKey()).append("=").append(en.getValue()).append("&");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    protected final Map<String, Object> getParams(HttpServletRequest request) {
        Enumeration<String> en = request.getParameterNames();
        Map<String, Object> map = new HashMap<String, Object>();
        String name;
        while (en.hasMoreElements()) {
            name = en.nextElement();
            map.put(name, request.getParameter(name));
        }
        return map;
    }

    protected final Map<String, Object> getParams(HttpServletRequest request, boolean cleanEmpty, boolean cleanNull) {
        Enumeration<String> en = request.getParameterNames();
        Map<String, Object> map = new HashMap<String, Object>();
        String name;
        Object tempValue;
        while (en.hasMoreElements()) {
            name = en.nextElement();
            tempValue = request.getParameter(name);
            if (cleanNull && (null == tempValue || ("null".equals(tempValue))) || cleanEmpty && null != tempValue && tempValue.toString().isEmpty()) {
                continue;
            }
            map.put(name, tempValue);
        }
        return map;
    }

    public String getRealPath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/");
    }

    public JsonMsgModel fail(String msg, Throwable e) {
        if (null != e)
            LOG.error(null, e);
        return new JsonMsgModel(getErrorCode(), msg);
    }

    public JsonMsgModel fail(Throwable e) {
        if (null != e)
            LOG.error(null, e);
        return new JsonMsgModel(getErrorCode(), e.getMessage());
    }

    public JsonMsgModel fail() {
        return new JsonMsgModel(getErrorCode(), "fail");
    }

    public JsonMsgModel success(String msg, Object data, Object attachment) {
        return new JsonMsgModel(0, msg, data, attachment);
    }

    public JsonMsgModel success(Object data) {
        return success("success", data, null);
    }

    public JsonMsgModel success() {
        return success(null, null, null);
    }

    public ModelAndView exceptionPage(Throwable e) {
        Map<String, Object> error = null;
        if (null != e) {
            LOG.error(null, e);
            error = CollectionUtil.newInstance().toMap(true, new Object[] { "error", e.getMessage() });
        }
        return forward(errorPageUrl, error);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public AbstractController<ModelAndView> setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public int getSuccess() {
        return success;
    }

    public AbstractController<ModelAndView> setSuccess(int success) {
        this.success = success;
        return this;
    }

}
