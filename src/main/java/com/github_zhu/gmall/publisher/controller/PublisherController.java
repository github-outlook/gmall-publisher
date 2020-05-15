package com.github_zhu.gmall.publisher.controller;

import com.alibaba.fastjson.JSON;
import com.github_zhu.gmall.publisher.service.EsService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: github_zhu
 * @Describtion:
 * @Date:Created in 2020/5/14 10:05
 * @ModifiedBy:
 */
@RestController
public class PublisherController {

    @Autowired
    EsService esService;

    //@RequestMapping(value = "realtime-total",method = RequestMethod.GET)
    @GetMapping("realtime-total")
    public String resltimeTotal(@RequestParam("date") String dt) {
        //新增日活结果
        List<Map> totalList = new ArrayList<>();
        HashMap dauMap = new HashMap<>();
        dauMap.put("id", "dau");
        dauMap.put("name", "新增日活");
        Long dauTotal = esService.getDauTotal(dt);
        dauMap.put("value", dauTotal);

        totalList.add(dauMap);
        //新增设备
        HashMap newMidMap = new HashMap<>();
        newMidMap.put("id", "new_mid");
        newMidMap.put("name", "新增设备");
        newMidMap.put("value", 222);
        totalList.add(newMidMap);

        return JSON.toJSONString(totalList);
    }

    @GetMapping("realtime-hour")
    public String realtimeHour(@RequestParam("id") String id, @RequestParam("date") String dt) {

        if ("dau".equals(id)) {
            HashMap<String, Map> hourMap = new HashMap<>();
            Map dauHourMap = esService.getDauHour(dt);
            hourMap.put("today", dauHourMap);
            String yd = getYd(dt);
            Map dauHourYdMap = esService.getDauHour(yd);
            hourMap.put("yesterday", dauHourYdMap);
            return JSON.toJSONString(hourMap);
        }
        return null;
    }

    private String getYd(String td) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yd = null;
        try {
            Date tdDate = simpleDateFormat.parse(td);
            Date ydDate = DateUtils.addDays(tdDate, -1);
            yd = simpleDateFormat.format(ydDate);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("日期转换格式异常");
        }
        return yd;
    }


}
