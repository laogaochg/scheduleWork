package com.lao.schedule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.util.Base64Util;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author: laogaochg
 * @date : 2019/7/18 9:48
 */
public class ImgTest {
    public static void main(String[] args) throws IOException {
        String url = "http://hahauut.singaporeluckyzodiac.com/api/verifyCode/get";
        RestTemplate rt = new RestTemplate();
        JSONObject result = rt.getForObject(url, JSONObject.class);
        String s = result.getJSONObject("data").getString("url");
        System.out.println(s);
//        String s = "/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAUAEEDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3GSVYsFwwU9WAyB9fT69Kwtb1i/0652w20So3EbySBvNIGcBAQQeozzk4HcV0FcPquq6Tc6ppxt2BhgcyOxDKg5BwBg9cc4Hcc9a5nzp3tdfj/wAH+tx1Oa2h2dxbpdQNDI0iq2MmKVo2654ZSCPwNcdca14j03XoNMWKO5WeXCG6ZWkdM43ZiC7FwM8oSPm64wOoh1CH7C11IXWBYvO3upz5eM598DHr278Vxdx4h0u48d2mpC62WltblDI0bfOSG4UAZ/jHUDofbLTUldBzqSTWh1cutPp8Ly6rYT20UalnuIP38Q7/AMI3gY6lkAGDz0zlaf4sudQ1mK3S3t/svls8rW8ouWAAJzmM464GMZ59xXS3l5BYWr3NzJ5cKY3NgnGTgcDnqa85SDSNS8Y3l7bSQWrTkJbl7ZmEkhIBbC4KnOWDhlYEg56inqU+Zbana3/iGxttOupY7uBbqOF2SCc7HLAEgFGw3PH1zxWdo2q6/qvkTJPoUkB2NOkTyGWNW5IIyQGxnr3FU9RvG0mBINVlktoJnEYS8T7fangtw/yylsgffPUnaCBlcWwj/tPxSuqWWktHpluNk76dK4G7YeVGI24yMqozx0O4ZuF+V3RKnL+tP8/0PT6K5zztJ9Nf/wC/d/8A4UVlzD55eX3/APAOjoooqzQgu0VrdiwyUBcfUClgZiZUZi3lvtDEDJ+UHnH1oornnpWjbqmYbVvkTUUUV0G5w8viq8nheGa1s5IpFKujxkhgeCCCeRVjw1cz3N3NaRytbW/l+YscR3CMg4wu/dtXBHyjAG0YA5yUV5PtJ92ea5yunc6X7JP/ANBG5/75j/8AiKKKKPaT7sfPLuf/2Q==";
//        byte[] bytes = Base64Util.decode(s);
//        FileOutputStream out = new FileOutputStream(new File("d:/bb.bmp"));
//        out.write(bytes);
//        out.close();
    }

    // 图片转化成base64字符串
    public static String GetImageStr() {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = "C:\\Users\\Administrator\\Desktop\\1.jpg";// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    // base64字符串转化成图片


}
