package com.lao.util;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import com.lao.schedule.MsisdnDto;
import com.lao.schedule.ScheduleConfig;
import com.lao.schedule.SpringRefreshListener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: laogaochg
 * @date : 2019/7/18 17:58
 */
public class LoginUtils {
    //设置APPID/AK/SK
    public static  Environment ENVIRONMENT ;
    public static final String APP_ID = "16837557";

    private static Logger logger = LoggerFactory.getLogger(LoginUtils.class);

    public static MsisdnDto login(String msisdn) throws Exception {
        String url = LoginUtils.ENVIRONMENT.getProperty("loginUrl");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("count", 0);
        Map<String, Object> map = getCode(resultMap);
        map.put("name", msisdn);
        map.put("password", "laogao520");
        map.put("rememberMe", false);
        logger.info("调用百度图片识别接口次数:{}", resultMap.get("count"));
        ScheduleConfig s = new ScheduleConfig();
        String post = s.post(url, JSON.toJSONString(map), null, null);
        if (!post.contains("成功")) {
            return login(msisdn);
        }
        MsisdnDto result = new MsisdnDto();
        result.setMsisdn(msisdn);
        result.setLuckKey(JSON.parseObject(post).getString("data"));
        return result;
    }

    public static Map<String, Object> getCode(Map<String, Object> resultMap) throws Exception {
        // 初始化一个AipImageClassifyClient
        String API_KEY = ENVIRONMENT.getProperty("API_KEY");
        String SECRET_KEY = ENVIRONMENT.getProperty("SECRET_KEY");
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        HashMap<String, String> param = new HashMap<>();
        param.put("with_face", "0");
        String url = LoginUtils.ENVIRONMENT.getProperty("verifyCodeUrl");
        RestTemplate rt = new RestTemplate();
        com.alibaba.fastjson.JSONObject result = rt.getForObject(url, com.alibaba.fastjson.JSONObject.class);
        String s = result.getJSONObject("data").getString("url");
        resultMap.put("verifyCodeToken", result.getJSONObject("data").getString("token"));
        Integer count = (Integer) resultMap.get("count");
        if (count > 20) throw new RuntimeException("调用接口超过二十次还是不对");
        resultMap.put("count", ++count);
        JSONObject res = client.numbers(generateImage(s), param);
        System.out.println(res);
        if (res.has("words_result")) {
            JSONArray array = res.getJSONArray("words_result");
            if (array != null && array.length() > 0) {
                String words = array.getJSONObject(0).getString("words");
                if (words.length() == 4) {
                    resultMap.put("verifyCode", words);
                    return resultMap;
                }
            }
        }
        return getCode(resultMap);
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
