package com.cheuks.bin.original.weixin;

import com.cheuks.bin.original.common.util.ClassToXml;
import com.cheuks.bin.original.weixin.mp.model.Scene;
import com.cheuks.bin.original.weixin.mp.model.message.NewsMessageResponse;
import com.cheuks.bin.original.weixin.mp.model.message.NewsMessageResponse.NewsArticlesItem;
import com.cheuks.bin.original.weixin.mp.model.request.QrCodeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    public static void main(String[] args) throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        QrCodeRequest qrCodeRequest = new QrCodeRequest();
        Scene s = new Scene().setSceneId(10010).setSceneStr("小喇叭");
        qrCodeRequest.setActionName("name").setExpireSeconds(993L).setActionInfo(s);
        System.out.println(qrCodeRequest.getActionInfo());
        System.err.println(objectMapper.writeValueAsString(qrCodeRequest));

        System.out.println("#########");

        //        for (int i = 0; i < 10; i++) {
        //            Long now = System.currentTimeMillis();

        NewsMessageResponse newsMessageResponse = new NewsMessageResponse();
        newsMessageResponse.appendNewsArticlesItem(new NewsArticlesItem("a", "b", "c", "d"));
        newsMessageResponse.appendNewsArticlesItem(new NewsArticlesItem("z", "x", "c", "v"));

        ClassToXml classToXml = new ClassToXml();
        System.out.println(classToXml.toXml(newsMessageResponse));
        //            classToXml.toXml(newsMessageResponse);
//        System.out.println("i:" + i + ":用时:" + (System.currentTimeMillis() - now));
        //        }
        //        System.out.println(Integer.class.isLocalClass());
        //        System.out.println(Integer.class.isPrimitive());
        //        System.out.println(Integer.class.isMemberClass());
        //        System.out.println(int.class.isPrimitive());

        //        String a = "";
        //        Integer b = 0;
        //        Float aaaa=0F;
        //        Set<Class> axx = new HashSet<Class>();
        //        axx.add(String.class);
        //        axx.add(Integer.class);
        //        axx.add(Boolean.class);
        //        System.err.println(axx.contains(a.getClass()));
        //        System.err.println(axx.contains(aaaa.getClass()));
        //        System.err.println(Integer.class == Integer.class);
    }

}
