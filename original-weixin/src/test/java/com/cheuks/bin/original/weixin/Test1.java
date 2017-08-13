package com.cheuks.bin.original.weixin;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cheuks.bin.original.weixin.mp.model.api.Button;
import com.cheuks.bin.original.weixin.mp.model.api.request.CreateMenuRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

/**
 * Unit test for simple App.
 */
public class Test1 {

    @Test
    public void menu() {
        CreateMenuRequest request = new CreateMenuRequest();
        List<Button> buttons = new ArrayList<Button>();
        Button b1 = new Button();
        b1.setName("第一个").setType("click").setKey("1st_click");

        Button b2 = new Button();
        List<Button> subButton = new ArrayList<Button>();
        
        
        b2.setName("第 二个").setType("click").setKey("2nd_click");
        Button b2_1 = new Button();
        b2_1.setName("第 二个一级").setType("click").setKey("2nd_1_click");
        Button b2_2 = new Button();
        b2_2.setName("第 二个二级").setType("click").setKey("2nd_2_click");
        
        
        subButton.add(b2_1);
        subButton.add(b2_2);
        b2.setSubButton(subButton);
        
        buttons.add(b1);
        buttons.add(b2);

        request.setButton(buttons);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            System.err.println(objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
