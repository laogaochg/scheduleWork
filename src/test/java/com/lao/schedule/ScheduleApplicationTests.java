package com.lao.schedule;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class ScheduleApplicationTests {

	@Test
	public void test() throws IOException, InterruptedException {

	}
	@Test
	public void contextLoads() throws IOException {
		File f = new File("d:/a.json  ");
		List<MsisdnDto> list = new ArrayList<>();
		MsisdnDto dto = new MsisdnDto();
		dto.setMsisdn("17722859084");
		dto.setBuyIds("");
		dto.setCookie("");
		dto.setLuckKey("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzcyMjg1OTA4NCIsImp3dF92YWx1ZSI6IntcImxvZ2luXCI6XCIxNzcyMjg1OTA4NFwiLFwidWlkXCI6XCIxMTQ2OTM2NDIxNzAzNjgwMDAxXCJ9IiwiZXhwIjoxNTYzMTYyMDcyfQ.w_A7AJnRf7SBG52Pjpsw3Uve3AnFtNmzLUHOCdvlW_2KpJyD1gBhv7UEFAYInvL7Eijwn02vGdPLPLHcSDviHg");
		MsisdnDto dto1 = new MsisdnDto();
		dto1.setMsisdn("17520089084");
		dto1.setBuyIds("");
		dto1.setCookie("");
		dto1.setLuckKey("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzUyMDA4OTA4NCIsImp3dF92YWx1ZSI6IntcImxvZ2luXCI6XCIxNzUyMDA4OTA4NFwiLFwidWlkXCI6XCIxMTQyMzk2OTkwOTY1NzE5MDQxXCJ9IiwiZXhwIjoxNTYzMTYyMTMyfQ.ofldtzthmaLRl0ZFJ7NXXbmiqXInNxuMFSG8zUYCXe3JztAG7N2S8TyfBaD3HpL5HIXFSnkqpgDfACa2YNCzkw");
		MsisdnDto dto12 = new MsisdnDto();
		dto12.setMsisdn("18148601205");
		dto12.setBuyIds("");
		dto12.setCookie("");
		dto12.setLuckKey("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODE0ODYwMTIwNSIsImp3dF92YWx1ZSI6IntcImxvZ2luXCI6XCIxODE0ODYwMTIwNVwiLFwidWlkXCI6XCIxMTM5MDA0NjU5MTk0MDg5NDczXCJ9IiwiZXhwIjoxNTYzMTYyMDI3fQ.ccsEfbllROceIhStUJVU4K7YpmExFew1345O28I0AR5Zwc8iZJmMyIYCQEkagnxfxAhSA0StZr4nzGcblzYFWw");
		list.add(dto);
		list.add(dto1);
		list.add(dto12);
		FileWriter fw = new FileWriter(f);
		fw.write(JSON.toJSONString(list));
		fw.close();

	}

}
