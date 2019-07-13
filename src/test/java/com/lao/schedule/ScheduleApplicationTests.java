package com.lao.schedule;

import com.alibaba.fastjson.JSON;
import com.lao.schedule.schedule.MsisdnDto;
import com.lao.schedule.schedule.ScheduleConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
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
		(new ScheduleConfig()).keepLive();
		(new ScheduleConfig()).buy5();
		Thread.sleep(10000L);
	}
	@Test
	public void contextLoads() throws IOException {
		File f = new File("d:/a.json  ");
		List<MsisdnDto> list = new ArrayList<>();
		MsisdnDto dto = new MsisdnDto();
		dto.setMsisdn("17722859084");
		dto.setBuyIds("");
		dto.setCookie("");
		dto.setLuckKey("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzcyMjg1OTA4NCIsImp3dF92YWx1ZSI6IntcImxvZ2luXCI6XCIxNzcyMjg1OTA4NFwiLFwidWlkXCI6XCIxMTQ2OTM2NDIxNzAzNjgwMDAxXCJ9IiwiZXhwIjoxNTYzMTIwNTc0fQ.wFVjCYR0QYy1ndbihcDGaworTwhyJEA8fvw3NQIB7hq8luwAY93vJgxDQZCU8fJ5Mw81eh5lWHdRdAukwqePog");
		MsisdnDto dto1 = new MsisdnDto();
		dto1.setMsisdn("17520089084");
		dto1.setBuyIds("");
		dto1.setCookie("");
		dto1.setLuckKey("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzUyMDA4OTA4NCIsImp3dF92YWx1ZSI6IntcImxvZ2luXCI6XCIxNzUyMDA4OTA4NFwiLFwidWlkXCI6XCIxMTQyMzk2OTkwOTY1NzE5MDQxXCJ9IiwiZXhwIjoxNTYzMTEwNDU2fQ.ErOYxM2DacfdP1-Ua9BffbCQpO7mooe2fnrgCEGHGorzb46cp6Zf9k1AdzGzuIct0OqN-v1M3qFEF1EjhcM3Ag");
		MsisdnDto dto12 = new MsisdnDto();
		dto12.setMsisdn("18148601205");
		dto12.setBuyIds("");
		dto12.setCookie("");
		dto12.setLuckKey("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODE0ODYwMTIwNSIsImp3dF92YWx1ZSI6IntcImxvZ2luXCI6XCIxODE0ODYwMTIwNVwiLFwidWlkXCI6XCIxMTM5MDA0NjU5MTk0MDg5NDczXCJ9IiwiZXhwIjoxNTYzMTE5Mzg1fQ.fPJRMl-nySuhwayqOE50fgC5fZa7ftgGzt3oRYgjBDpuZowpccflBfoQYZOUn81X3KXuo94jTQUh_v3XiB549g");

		list.add(dto);
//		list.add(dto1);
		list.add(dto12);
		FileWriter fw = new FileWriter(f);
		fw.write(JSON.toJSONString(list));
		fw.close();

	}

}
