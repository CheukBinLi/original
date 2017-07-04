package com.cheuks.bin.original.common.web.common.controller;

import java.util.Map;

import com.cheuks.bin.original.common.web.common.ContentType;

/***
 * 
 * @Title: original-commonTODO
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年6月29日 上午11:15:52
 *
 */
public interface BaseController<Request, Response, RestfulResult, ModelAndView> extends ContentType {

    RestfulResult fail(String msg, Throwable e);

    RestfulResult fail(Throwable e);

    RestfulResult fail();

    RestfulResult success(String msg, Object data, Object attachment);

    RestfulResult success(Object data);

    RestfulResult success();

    ModelAndView exceptionPage(Throwable e);

    ModelAndView redirect(String url, Map<String, Object> params);

    ModelAndView forward(String url, Map<String, Object> params);

    // LoginInfoModel getLoginInfo(Request request);

}
