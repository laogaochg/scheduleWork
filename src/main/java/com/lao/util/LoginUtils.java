package com.lao.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import com.lao.schedule.MsisdnDto;
import com.lao.schedule.ScheduleConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import static com.lao.schedule.BuyResultController.map;

/**
 * @author: laogaochg
 * @date : 2019/7/18 17:58
 */
public class LoginUtils {
    //设置APPID/AK/SK
    public static final String APP_ID = "16837557";
    public static final String API_KEY = "yMLQCTjw7QodPclSEifAkcya";
    public static final String SECRET_KEY = "DGgIN9Q8dL363r0GedrTv4ju6Kq2QK6M";

    public static MsisdnDto login(String msisdn) throws Exception {
        String url = "http://hahauut.singaporeluckyzodiac.com/auth/login";
        Map<String, Object> map = getCode();
        map.put("name",msisdn);
        map.put("password","laogao520");
        map.put("rememberMe",false);
        ScheduleConfig s = new ScheduleConfig();
        String post = s.post(url, JSON.toJSONString(map), null, null);
        System.out.println(post);
        if(!post.contains("成功")){
            login(msisdn);
        }
        return null;
    }

    public static Map<String, Object> getCode() throws Exception {
        // 初始化一个AipImageClassifyClient
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        HashMap<String, String> param = new HashMap<>();
        param.put("with_face", "0");
        String url = "http://hahauut.singaporeluckyzodiac.com/api/verifyCode/get";
        RestTemplate rt = new RestTemplate();
        com.alibaba.fastjson.JSONObject result = rt.getForObject(url, com.alibaba.fastjson.JSONObject.class);
        String s = result.getJSONObject("data").getString("url");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("verifyCodeToken", result.getJSONObject("data").getString("token"));
        JSONObject res = client.numbers(generateImage(s), param);
        JSONArray array = res.getJSONArray("words_result");
        if (array != null && array.length() > 0) {
            String words = array.getJSONObject(0).getString("words");
            if (words.length() == 4) {
                resultMap.put("verifyCode", words);
                return resultMap;
            }
        }
        getCode();
        return null;
    }

    public static byte[] generateImage(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return new byte[]{};
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            return b;
        } catch (Exception e) {
            return new byte[]{};
        }
    }
}
