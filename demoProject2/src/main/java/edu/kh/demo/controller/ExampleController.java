package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.demo.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j // log 필드 생성 및 초기화 자동오나성 lombok 어노테이션
@RequestMapping("example") // /example 로 시작하는 요청을 매핑
@Controller // 컨트롤러임을 명시 + Bean 등록 (spring container 가 만들어서 관리하는 객체)
public class ExampleController {

	
	
	// Servlet/JSP 내장 객체 4종류의 데이터 유지 범위(scope)
	
	// 1) page        : 현재 페이지
	// 2) request     : 요청 위임한 페이지 + 위임 받은 페이지
	// 3) session     : 클라이언트가 서버 최초 연결 시 생성
	//									세션 만료, 브라우저 종료까지
	// 4) application : 하나만 존재, 서버 종료될 때까지
	
	
	/* Model 
	 * - org.springframework.ui 패키지
	 * 
	 * - Spring 에서 데이터를 전달하는 역할의 객체
	 * 
	 * - 데이터 유지 범위(scope) : 기본 request
	 * 
	 * - @SessionAttributes 와 함께 사용하면 session scope 로 변경
	 * 
	 * [Model 을 이용해서 값을 세팅하는 방법]
	 * 
	 * Model.addAttribute("key", value); 
	 * 
	 * scope 변환을 위해 사용?? 
	 */
	@GetMapping("ex1") // 왜 get인가? 요청하면 html 얻어와진다??
	public String example1(HttpServletRequest req, Model model) {
		// request scope 에 값 세팅
		req.setAttribute("test1", "HttpServletRequest 로 세팅한 값");
		
		// model 을 이용해서 request scope 에 값 세팅
		model.addAttribute("test2", "Model 로 세팅한 값");
		
		// 단일 값 세팅(숫자, 문자열
		model.addAttribute("productName", "아이스 아메리카노");
		model.addAttribute("price", 2000);
		
		List<String> fruitList = new ArrayList<>();
		
		fruitList.add("복숭아");
		fruitList.add("딸기");
		fruitList.add("수박");
		fruitList.add("바나나");
		
		model.addAttribute("fruitList", fruitList);
		
		// DTO 객체를 만들어 Model 에 세팅 + 빌더 패턴 사용
		Student std = Student.builder()
												 .studentNo("1111")
												 .name("짱구")
												 .age(15)	 
												 .build(); // 메서드 체이닝
		// 필드 여러 개 중 2, 3개 정도 값을 세팅할 때 유용(일부 초기화)
		
		log.debug("student : {}", std);
		
		model.addAttribute("std", std);
		
		//--------------------------------------------------
		
		/* DTO 필드 중 List 가 포함되어 있는 경우 */
		
		List<String> hobbyList = new ArrayList<>();
		hobbyList.add("축구");
		hobbyList.add("독서");
		hobbyList.add("코딩 공부");
		
		Student std2 = Student.builder()
				 .studentNo("2222")
				 .name("철수")
				 .age(16)
				 .hobbyList(hobbyList)
				 .build();
		
		model.addAttribute("std2", std2);
		
		
		// classpath:/templates/ex/result1.html 파일로 
		// forward(요청 위임)
		return "ex/result1";
	}
	
	
	/**
	 * 
	 * @param model : Spring 에서 데이터를 전달하는 용도의 객체
	 * 								(기본 scope : request)
	 * @return : forward 경로
	 */
	@PostMapping("ex2") // /example/ex2 POST 방식 요청 매핑
	public String example2(Model model) {
		
		model.addAttribute("str", "<h1>테스트 중입니다!!! &times; </h1>");
		
		
		return "ex/result2";
	}
	
}















