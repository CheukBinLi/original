package com.cheuks.bin.original.web.common.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.util.CollectionUtil;
import com.cheuks.bin.original.common.util.JsonMsgModel;
import com.cheuks.bin.original.common.util.ObjectFill;
import com.cheuks.bin.original.common.web.common.controller.BaseController;

public abstract class AbstractController<ModelAndView> extends ObjectFill implements BaseController<HttpServletRequest, HttpServletResponse, JsonMsgModel, ModelAndView> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

    protected int errorCode = -1;

    protected int success = 0;

    protected String errorPageUrl = "/error";

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
