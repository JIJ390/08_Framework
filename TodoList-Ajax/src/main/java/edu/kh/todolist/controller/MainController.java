package edu.kh.todolist.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todolist.dto.Todo;
import edu.kh.todolist.service.TodoListService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller	// Controller 임을 명시 + Bean 등록
public class MainController {
	
	@Autowired	// 의존성 주입(DI)
	private TodoListService service;
	
	/**
	 * 
	 * @param model 데이터 전달용 객체(request scope)
	 * @return
	 */
	@RequestMapping("/")	// 최상위 주소 매핑(GET, POST 가리지 않음)
	public String mainPage(
			Model model) {
		
		Map<String, Object> map = service.selectTodoList();
		
		// map에 담긴 값 꺼내놓기	Map 에 들어있는 Object 를 List<Todo> 로 다운 캐스팅
		List<Todo> todoList = (List<Todo>)map.get("todoList");
		int completeCount = (int)map.get("completeCount");
		
//		log.debug("todoList : {}", todoList);
		
		// 조회 결과 request scope 에 추가
		model.addAttribute("todoList", todoList);
		model.addAttribute("completeCount", completeCount);
		
		return "common/main";
	}
}
