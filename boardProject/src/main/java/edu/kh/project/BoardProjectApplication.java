package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.stereotype.Controller;

// @SpringBootApplication : spring 설정들 적용
// Spring Security 자동 설정을 실행 시 포함하지 않음
// 서버 실행시 로그인 화면 동작 안하도록
// -> Security 제공 로그인 페이지 비활성화

//ComponentScan / Controller , service bean 찾아서 등록
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BoardProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectApplication.class, args);
	}

}
