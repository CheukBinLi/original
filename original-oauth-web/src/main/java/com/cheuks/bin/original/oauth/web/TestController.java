package com.cheuks.bin.original.oauth.web;

import com.cheuks.bin.original.cache.JedisStandAloneCacheFactory;
import com.cheuks.bin.original.cache.redis.RedisLuaSimple;
import com.cheuks.bin.original.common.util.web.Result;
import com.cheuks.bin.original.common.util.web.ResultFactory;
import com.cheuks.bin.original.oauth.model.User;
import com.cheuks.bin.original.oauth.security.OauthAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

//@Profile({ "dev", "rc" })
@Controller
//@RequestMapping(value = { "/", "/account" })
@RequestMapping(value = {"/"})
public class TestController {

    ResultFactory resultFactory = new ResultFactory();

    final JedisStandAloneCacheFactory factory = new JedisStandAloneCacheFactory();
//        factory.set("t1", "t1");

    final RedisLuaSimple redisLuaSimple = new RedisLuaSimple();

    {
        factory.setHost("192.168.0.251");
        factory.setPassword("e#l2jISe*d0AdEeS29w");
        factory.setMaxTotal(100000);
        factory.setMaxIdle(2);
        redisLuaSimple.setRedisFactory(factory);
        try {
            redisLuaSimple.initLoader();
        }
        catch (Throwable e){
            e.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping("/test/test")
    public Result<Object> p(HttpServletRequest request) throws Throwable {
        OauthAuthenticationToken auth = (OauthAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        System.err.println(((User) auth.getPrincipal()).getUserName());
        return resultFactory.createSuccess();
    }

    @ResponseBody
    @GetMapping("/t/test")
    public Result<Object> t(HttpServletRequest request) throws Throwable {
        OauthAuthenticationToken auth = (OauthAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        System.err.println(((User) auth.getPrincipal()).getUserName());
        return resultFactory.createSuccess();
    }

    @ResponseBody
    @GetMapping("/m/test")
    public Result<Object> m(HttpServletRequest request) throws Throwable {
        OauthAuthenticationToken auth = (OauthAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        System.err.println(((User) auth.getPrincipal()).getUserName());
        return resultFactory.createSuccess();
    }

    @ResponseBody
    @GetMapping("/test/next")
    public Result<Object> next(HttpServletRequest request) throws Throwable {

        return resultFactory.createSuccess(
                factory.evalSha(redisLuaSimple.getSha("time"), 1
                        , "ID_TEST"
                        , "10010"
                        , "APP"
                        , "ORDER"
                        , "1"
                )
        );
    }

}
