package com.cheuks.bin.original.weixin;

import java.lang.reflect.Field;

import org.junit.Test;

import com.cheuks.bin.original.weixin.mp.model.api.Scene;
import com.cheuks.bin.original.weixin.mp.model.api.request.QrCodeRequest;
import com.cheuks.bin.original.weixin.mp.model.customservice.message.BaseMessage;
import com.cheuks.bin.original.weixin.mp.model.customservice.message.NewsMessage;
import com.cheuks.bin.original.weixin.mp.model.customservice.message.TextMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;

/**
 * Unit test for simple App.
 */
public class Test2 {

    @Test
    public void qrCode() {
        ObjectMapper objectMapper = new ObjectMapper();

        QrCodeRequest qrCodeRequest = new QrCodeRequest();
        Scene scene = new Scene();
        scene.setSceneStr("winer=叼拿升");
        qrCodeRequest.setActionName("测试二维码").setActionInfo(scene);
        try {
            System.err.println(objectMapper.writeValueAsString(qrCodeRequest));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void mm() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Field field = BaseMessage.class.getDeclaredField("body");
            JsonProperty j = field.getDeclaredAnnotation(JsonProperty.class);

            BaseMessage b = new BaseMessage("aaaa") {};

            //            objectMapper.setfi
            FilterProvider filterProvider = new FilterProvider() {

                @Override
                public BeanPropertyFilter findFilter(Object filterId) {
                    return null;
                }
            };
            objectMapper.setFilters(filterProvider);
            objectMapper.setAnnotationIntrospector(new AAAFilter());
            System.err.println(objectMapper.writeValueAsString(b));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static class AAAFilter extends AnnotationIntrospector implements Versioned {

        @Override
        public Version version() {
            return null;
        }

        @Override
        public PropertyName findNameForSerialization(Annotated a) {
            System.err.println(a.getName());
            if ("body".equals(a.getName()))
                return new PropertyName("AAAAAAAAAAAAAAAAAAA");
            return super.findNameForSerialization(a);
        }
    }

    public void t3() {
        ObjectMapper objectMapper = new ObjectMapper();
        TextMessage text = new TextMessage().setContent("你好吗？");

        NewsMessage newsMessage = new NewsMessage();
        newsMessage.addArticles("小A", "OH", null, null);
        newsMessage.addArticles("小B", "OH", null, null);
        newsMessage.addArticles("小c", "OH", null, null);

        try {
            System.out.println(objectMapper.writeValueAsString(text));
            System.out.println(objectMapper.writeValueAsString(newsMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Test2().t3();
    }

}
