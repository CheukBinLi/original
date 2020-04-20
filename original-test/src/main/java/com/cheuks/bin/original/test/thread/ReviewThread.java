package com.cheuks.bin.original.test.thread;

import java.util.concurrent.Executors;

/***
 * *
 *
 * @Title: original
 * @Package com.cheuks.bin.original.test.thread
 * @Description:
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2020-02-08 12:35
 *
 *
 */
public class ReviewThread {

    public static void main(String[] args) {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

}
