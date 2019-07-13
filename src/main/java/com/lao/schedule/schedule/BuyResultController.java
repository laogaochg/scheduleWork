package com.lao.schedule.schedule;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: LaoGaoChuang
 * @Date : 2019/7/13 13:46
 */
@RestController
public class BuyResultController {
    public static Map<String, String> map = new HashMap<>();

    @RequestMapping("/buyResult")
    public Map<String, String> result() {
        return map;
    }
    @RequestMapping("/buyResultKey")
    public Set<String> buyResultKey() {
        return map.keySet();
    }
}
