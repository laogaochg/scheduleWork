package com.lao.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lao.util.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: LaoGaoChuang
 * @Date : 2019/7/13 13:46
 */
@RestController
public class BuyResultController {
    public static Map<String, String> map = new HashMap<>();
    @Autowired
    private ScheduleConfig scheduleConfig;
    @Autowired
    private Environment environment;
    private static String br = "<br/>";

    @RequestMapping("/logout")
    public List<MsisdnDto> logout() {
        List<MsisdnDto> msisdnList = ScheduleConfig.msisdnList;
        ScheduleConfig.msisdnList = new ArrayList<>();
        return msisdnList;
    }
    //http://aktt68.sybcsd.cn/api/pigInvitation/invitation/18

    @RequestMapping("/invitation")
    public String invitation(String id) {
        String s = "";
        String url = environment.getProperty("invitationUrl") + id;
        for (MsisdnDto dto : ScheduleConfig.msisdnList) {
            s += scheduleConfig.get(url, dto.getCookie(), dto.getLuckKey()) + br;
        }
        return s;
    }


    @RequestMapping("/buy")
    public List<MsisdnDto> buy() {
        for (String s : ScheduleConfig.animal.keySet()) {
            scheduleConfig.buyList(s);
        }
        return ScheduleConfig.msisdnList;
    }

    @RequestMapping("/loginB")
    public List<MsisdnDto> loginB() {
        readFile();
        scheduleConfig.keepLive();
        return ScheduleConfig.msisdnList;
    }

    public void readFile() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(new File(environment.getProperty("filePath"))));
            List<MsisdnDto> now = new ArrayList<>();
            while (bf.ready()) {
                String line = bf.readLine().trim();
                if (StringUtils.hasText(line)) {
                    String[] split = line.split("\\|");
                    MsisdnDto dto1 = new MsisdnDto();
                    dto1.setMsisdn(split[0]);
                    dto1.setBuyIds("");
                    dto1.setCookie("");
                    dto1.setLuckKey(split[1]);
                    now.add(dto1);
                }

            }
            ScheduleConfig.msisdnList = now;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/login")
    public String login() {
        List<String> list = new ArrayList<>();
        list.add("18148601205");
        list.add("17722859084");
        list.add("17520089084");
        list.add("16605110602");
        list.add("18124276537");
        List<MsisdnDto> result = new ArrayList<>();
        scheduleConfig.keepLive();
        Set<String> set = new HashSet<>();
        for (MsisdnDto dto : ScheduleConfig.msisdnList) {
            set.add(dto.getMsisdn());
        }
        for (String s : list) {
            try {
                if (set.contains(s)) continue;
                result.add(LoginUtils.login(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ScheduleConfig.msisdnList.addAll(result);
        String s = "";
        for (MsisdnDto dto : ScheduleConfig.msisdnList) {
            s += dto.getMsisdn() + "|" + dto.getLuckKey() + br;
        }
        return s;
    }

    //已经领养的纪录
    @RequestMapping("/findAdopedtList")
    public String findAdopedtList() {
        StringBuilder sb = new StringBuilder();
        for (MsisdnDto dto : ScheduleConfig.msisdnList) {
            String s = scheduleConfig.post(environment.getProperty("findAdopedtList"), "{\"pageNo\":1,\"pageSize\":10}", dto.getCookie(), dto.getLuckKey());
            if (s.contains("\"code\":200")) {
                JSONObject jsonObject = JSON.parseObject(s);
                JSONObject data = jsonObject.getJSONObject("data");
                if (data != null && data.getJSONArray("records") != null) {
                    for (Object r : data.getJSONArray("records")) {
                        JSONObject j = (JSONObject) r;
                        String goodsName = j.getString("goodsName").replace("幸运", "");
                        Integer contractDays = j.getInteger("contractDays");
//                        2019/07/26 12:44:57
                        String createTime = j.getString("buyTime").substring(0,10);
                        String endTime = j.getString("endTime").substring(0,10);
                        BigDecimal price = j.getBigDecimal("price");
                        sb.append(dto.getMsisdn())
                                .append("|").append(goodsName)
                                .append("|").append(price)
                                .append("|").append(createTime)
                                .append("|").append(contractDays)
                                .append("|").append(endTime)
                                .append(br);
                    }
                }

            }
        }
        return sb.toString();
    }


    @RequestMapping("/findAdoptList")
    public String findAdoptList() {
        StringBuilder ss = new StringBuilder();
        for (MsisdnDto dto : ScheduleConfig.msisdnList) {
            String s = scheduleConfig.post(environment.getProperty("findAdoptList"), "{\"pageNo\":1,\"pageSize\":10}", dto.getCookie(), dto.getLuckKey());
            if (s.contains("\"code\":200")) {
                JSONObject jsonObject = JSON.parseObject(s);
                JSONObject data = jsonObject.getJSONObject("data");
                if (data != null && data.getJSONArray("records") != null) {
                    ss.append(dto.getMsisdn()).append("-->").append(data.getJSONArray("records")).append(br);
                }

            }
        }
        return ss.toString();
    }

    @RequestMapping("/buyResultKey")
    public String buyResultKey() {
        StringBuilder ss = new StringBuilder();
        for (String dto : map.keySet()) {
            ss.append(dto).append("------").append("\n");
        }
        return ss.toString();
    }

    @RequestMapping("/toSell")
    public String toSell() {
        StringBuilder sb = new StringBuilder();
        BigDecimal total = new BigDecimal(0);
        for (MsisdnDto m : ScheduleConfig.msisdnList) {
            String s = scheduleConfig.post(environment.getProperty("toBeTransferredList"), "{\"pageNo\":1,\"pageSize\":10}", m.getCookie(), m.getLuckKey());
            if (s.contains("\"code\":200")) {
                JSONObject jsonObject = JSON.parseObject(s);
                JSONObject data = jsonObject.getJSONObject("data");
                if (data != null && data.getJSONArray("records").size() > 0) {
                    for (Object r : data.getJSONArray("records")) {
                        JSONObject j = (JSONObject) r;
                        String goodsName = j.getString("goodsName").replace("幸运", "");
                        BigDecimal price = j.getBigDecimal("price");
                        sb.append(m.getMsisdn()).append(" | ").append(goodsName).append(" | ").append(price).append(br);
                        total = total.add(price);
                    }
                }

            }
        }
        sb.append("总量:" + total);
        return sb.toString();
    }

    @RequestMapping("/get")
    public String transferredStatusList() {
        StringBuilder sb = new StringBuilder();
        for (MsisdnDto m : ScheduleConfig.msisdnList) {
            String s = scheduleConfig.post(environment.getProperty("transferredStatusList"), "{\"pageNo\":1,\"pageSize\":10}", m.getCookie(), m.getLuckKey());
            if (s.contains("\"code\":200")) {
                JSONObject jsonObject = JSON.parseObject(s);
                JSONObject data = jsonObject.getJSONObject("data");
                if (data != null && data.getJSONArray("records").size() > 0) {
                    for (Object r : data.getJSONArray("records")) {
                        JSONObject j = (JSONObject) r;
                        String goodsName = j.getString("goodsName").replace("幸运", "");
                        BigDecimal price = j.getBigDecimal("pigPrice");
                        sb.append(m.getMsisdn()).append(" | ").append(goodsName).append(" | ").append(price).append(br);
                    }
                }

            }
        }
        return sb.toString();
    }

    @RequestMapping("/totalMoney")
    public String totalMoney() {
        BigDecimal totalIncome = new BigDecimal(0);
        BigDecimal totalAsset = new BigDecimal(0);
        for (MsisdnDto m : ScheduleConfig.msisdnList) {
            String s = scheduleConfig.post(environment.getProperty("findUserAssets"), "{}", m.getCookie(), m.getLuckKey());
            JSONObject jsonObject = JSON.parseObject(s);
            //收益
            totalIncome = totalIncome.add(jsonObject.getJSONObject("data").getBigDecimal("totalIncome"));
            totalAsset = totalAsset.add(jsonObject.getJSONObject("data").getBigDecimal("totalAsset"));
        }
        BigDecimal a = totalAsset.subtract(totalIncome);
        return "资产:" + totalAsset + "，收益:" + totalIncome + "，本金:" + a;
    }
}
