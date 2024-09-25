package edu.kh.project.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.main.service.MainService;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;

@Controller // 요청/응답 제어하는 Controller 역할 명시 + bean 등록
@RequiredArgsConstructor
public class MainController {
	
	//@RequiredArgsConstructor 로 의존성 주입
	private final MainService service;
	
	@RequestMapping("/")	// "/" 요청 매핑(method 가리지 않음)
	public String mainPage() {
		// 접두사 : classpath:/templates/
		// 접미사 : .html
		// classpath:/templates/common/main.html 경로로 forward
		return "common/main";	
	}
	
	
	/**
	 * 비동기 회원 목록 조회
	 * @return
	 */
	@ResponseBody
	@GetMapping("selectMemberList")
	public List<Member> selectMemberList() {
		return service.selectMemberList();
	}
}
