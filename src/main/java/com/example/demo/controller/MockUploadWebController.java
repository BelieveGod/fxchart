package com.example.demo.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LTJ
 * @date 2021/8/16
 */
@RestController
public class MockUploadWebController {

    @RequestMapping("/tibdm/txieasyui")
    public Map<String,Object> mock(String taskFramePN,String command,String colname,String colname1,String table,String jsonList){
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("status", "success");
        list.add(item);
        result.put("result", list);
        return result;

    }
}
