package edu.kh.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.todolist.dto.Todo;
import edu.kh.todolist.service.TodoListService;
import lombok.extern.slf4j.Slf4j;


/* @RequestBody
 * - 비동기 요청(ajax) 시 전달되는 데이터 중
 *   body 부분에 포함된 요청 데이터를
 *   알맞은 Java 객체 타입으로 바인딩하는 어노테이션
 * 
 * (쉬운 설명)
 * - 비동기 요청 시 body에 담긴 값을
 *   알맞은 타입으로 변환해서 매개 변수에 저장
 * */

/* @ResponseBody
 * - 컨트롤러 메서드의 반환 값을
 *   HTTP 응답 본문에 직접 바인딩하는 역할임을 명시
 *   (js의 fetch())
 *  
 * (쉬운 해석)  
 * -> 컨트롤러 메서드의 반환 값을
 *  비동기 (ajax)요청했던 
 *  HTML/JS 파일 부분에 값을 돌려 보낼 것이다를 명시
 *  
 *  - forward/redirect 로 인식 X
 * */

/* [HttpMessageConverter]
 *  Spring에서 비동기 통신 시
 * - 전달되는 데이터의 자료형
 * - 응답하는 데이터의 자료형
 * 위 두가지 알맞은 형태로 가공(변환)해주는 객체
 * 
 * 		    JAVA         JS
 * - 문자열, 숫자 <-> TEXT
 * - Map          <-> JSON
 * - DTO          <-> JSON
 * 
 * (참고)
 * HttpMessageConverter가 동작하기 위해서는
 * Jackson-data-bind 라이브러리가 필요한데
 * Spring Boot 모듈에 내장되어 있음
 * (Jackson : 자바에서 JSON 다루는 방법 제공하는 라이브러리)
 */

@Slf4j
@Controller	// controller 명시 + bean 등록
@RequestMapping("todo")
public class TodoAjaxController {

	@Autowired // 의존성 주입 + 타입이 같은 bean 객체 대입
	private TodoListService service;
	
	
	/**
	 * 비동기로 할 일 추가
	 * @param todo : @RequestBody 이용해서 
	 * 							 전달 받은 JSON 형태(String)의 데이터를 
	 * 							 Todo dto 로 변환
	 * @return
	 */
	@ResponseBody
	@PostMapping("add")
	public int addTodo(@RequestBody Todo todo) {	// main.js 에 작성한 요청에서 body 부분
		// 반환형을 int(DML 결과) 로 변경!
		
		log.debug("todo : {}", todo);
		
		// 서비스 호출 후 결과 반환 받기
		int result = service.addTodo(todo);
		
		/* 비동기 통신의 목적 : "값" 또는 화면 일부만 갱신 없이
		 * 서버로 부터 응답 받고 싶을 때 사용
		 */
		
		// 값만 보내기 때문에 화면 갱신 메시지 필요 X
		return result; // service 수행 결과 그대로 반환, 여기선 1
	}
	
	
	/**
	 * 
	 * @param todoNo : GET 방식 요청은 body 가 아닌
	 * 	주소에 담겨 전달된 "파라미터"(쿼리스트링) -> @RequestParam 사용
	 * @return
	 */
	@ResponseBody	// 비동기 요청한 js 본문으로 값 반환
	@GetMapping("searchTitle")
	public String searchTitle(
			// get 방식 fetch 내부에 body 부분 존재하지 않음
			@RequestParam("todoNo") int todoNo
			) {
		
		String todoTitle = service.searchTitle(todoNo);
		
		// 서비스 결과를 "값" 형태 그대로 js 본문으로 반환
		// @ResponseBody 어노테이션 사용!!!!!!!!!!!
		return todoTitle;
	}
	
	/**
	 * 전체 할 일 개수 조회
	 * @return 전체 할 일 개수
	 */
	@ResponseBody // 비동기 요청한 js 본문으로 값 반환
	@GetMapping("totalCount")
	public int getTotalCount() {
		return service.getTotalCount();
	}
	
	
	/**
	 * 완료된 할 일 개수 조회
	 * @return
	 */						// 호출한 ajax 코드로 값 자체를 반환 (forward X)
	@ResponseBody // 비동기 요청한 js 본문으로 값 반환
	@GetMapping("completeCount")
	public int getCompleteCount() {
		return service.getCompleteCount();
	}
	
	
	/**
	 * 할 일 상세 조회 
	 * @param todoNo
	 * @return todo
	 */
	@ResponseBody
	@GetMapping("detailTodo")
	public /*String*/ Todo detailTodo(@RequestParam("todoNo") int todoNo) {
		// 반환형 String 인 경우
		// - Java 객체는 JS에서 호환 X
		// -> Java 에서 JS 에 호환될 수 있도록 JSON 형태데이터를 반환
		
		// JS 객체 : {"K" : V, "K" : V}
//		return "{\"todoNo\":10, \"todoTitle\":\"제목 테스트\"}";	// JSON
		
		/* 반환형 Todo(String 이 아닌 Object) 인 경우*/
		// -> Java 객체가 반환되면 JS 에서 쓸 수 없는 걸
		//    당연히 Spring 이 알 고 있으니까
		//    이를 자동으로
		//    HttpMessageConverter 객체가 변환해준다!
		
		return service.detailTodo(todoNo);
	}
	
	/**
	 * 할 일 전체 목록 비동기 요청 처리
	 * @return
	 */
	@ResponseBody
	@GetMapping("todoList")
	public List<Todo> getTodoList() {
		return service.getTodoList();
		// 비동기 요청에 대한 응답으로 객체 반환 시
		// "HttpMessageConverter" 가 
		//  JSON(단일 객체) 또는 JSONArray(배열, 컬렉션) 형태로 변환
		
		// "[{"K" : V}, {"K" : V}, {"K" : V}]" == JSONArray
	}
	
	
	/**
	 * 할 일 상세 조회
	 * @param todoNo
	 * @return
	 */
	@ResponseBody
	@GetMapping("detail/{todoNo}")
	public Todo selectTodo(
			@PathVariable("todoNo") int todoNo
			) {
		
		return service.detailTodo(todoNo);	//HttpMessageConverter
	}
	
	
	/**
	 * 할 일 완료 여부 수정
	 * @param todoNo
	 * @return
	 */
	@ResponseBody
	@PutMapping("updateComplete")
	public int updateComplete(
			@RequestBody int todoNo		// fetch의 body 에 담음
			) {
		return service.updateComplete(todoNo);
	}
	
	
	/**
	 * 할 일 삭제
	 * @param todoNo
	 * @return
	 */
	@ResponseBody
	@DeleteMapping("delete")
	public int deleteTodo(
			@RequestBody int todoNo
			) {
		return service.deleteTodo(todoNo);
	}
	
	/**
	 * 할 일 삭제
	 * @param todoNo
	 * @return
	 */
	@ResponseBody
	@PutMapping("update")	
	public int updateTodo(
			@RequestBody Todo todo	//HttpMessageConverter 비동기 통신 파싱
			) {
		return service.updateTodo(todo);
	}
	
	
}
