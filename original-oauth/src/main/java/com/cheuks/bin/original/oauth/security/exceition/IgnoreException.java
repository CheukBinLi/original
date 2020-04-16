package com.cheuks.bin.original.oauth.security.exceition;

/***
 * *
 *
 * @Title: original-oauth
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2019年02月03日 下午2:51:50
 *
 *       忽略标记
 */
public class IgnoreException extends RuntimeException {

    private static final long serialVersionUID = 1771794427353415403L;

    static final IgnoreException DEFAULT_IGNORE_EXCEPTION = new IgnoreException();

    public static final IgnoreException getDefaultIgnoreException() {
        return DEFAULT_IGNORE_EXCEPTION;
    }

}
