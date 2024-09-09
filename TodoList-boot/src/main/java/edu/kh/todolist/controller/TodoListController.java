package edu.kh.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todolist.dto.Todo;
import edu.kh.todolist.service.TodoListService;
import lombok.extern.slf4j.Slf4j;

@Slf4j					//로그
@Controller			// (IOC, 객체 생성 및 관리를 Spring 이 함)controller 명시 bean 등록
@RequestMapping("todo")
public class TodoListController {
	
	@Autowired // 의존성 주입 등록된 bean 중에서 같은 타입 객체 주입
	private TodoListService service;
	
	/**
	 * todo 생성에서 저장
	 * @param todo : 커맨드 객체(제출된 파라미터를 필드에 저장한 객체
	 * @param ra   : 리다이렉트 시 사용 가능 (세션으로 대피 가능한 request scope 객체, 1회용)
	 * @return			 삭제 처리할 필요 없음
	 */
	@PostMapping("add")
	public String	addTodo(
			@ModelAttribute Todo todo, // @ModelAttribute 생략 가능
			RedirectAttributes ra
			) {
		
		int result = service.addTodo(todo);
		
		String message = null;
		
		if (result > 0) message = "할 일 추가 성공"; 
		else 						message = "추가 실패";
		
		ra.addFlashAttribute("message", message);		// flash 항상 확인!!!
		
		return "redirect:/";
	}
	
	
	/** 상세 조회
	 * @param todoNo : 조회할 할 일의 PK 번호(@PathVariable 이용)
	 * @param model  : 데이터 전달용 객체(forward 시 request scope 값 전달)
	 * @param ra     : 
	 * @return
	 */
	@GetMapping("detail/{todoNo}")
	public String detailTodo(
			@PathVariable("todoNo") int todoNo,
			Model model,
			RedirectAttributes ra
			) {
		
		Todo todo = service.detailTodo(todoNo);
		
		// 조회 결과 없을 때 메시지를 전달하지 않으므로 path, message 변수 따로 선언X
		if (todo == null) {
			ra.addFlashAttribute("message", "할 일이 존재하지 않습니다");
			return "redirect:/";	// 리다이렉트
		}
		
		model.addAttribute("todo", todo);
		
		return "todo/detail";		// 요청 위임
	}

	
	/**
	 * 완료 여부 변셩
	 * @param todoNo : 쿼리 스트링으로 전달된 todoNo 값
	 * @param ra
	 * @return
	 */
	@GetMapping("complete")
	public String updateComplete(
			@RequestParam("todoNo") int todoNo, // 파라미터(쿼리스트링) 에 담긴 값 가져오기
			RedirectAttributes ra
			) {
		int result = service.updateComplete(todoNo);
		
		String message = null;
		String path    = null;
		
		if (result > 0) {
			message = "변경 되었습니다";
			path    = "redirect:/todo/detail/" + todoNo;
		}
		else {
			message = "할 일이 존재하지 않습니다";
			path    = "redirect:/";
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
	
	
	
	@GetMapping("update")
	public String updateTodo(
			@RequestParam("todoNo") int todoNo,
			Model model,
			RedirectAttributes ra) {
		
		// 상세 조회 서비스 호출
		Todo todo = service.detailTodo(todoNo);
		
		if (todo == null) {
			ra.addFlashAttribute("message", "할 일이 존재하지 않습니다");
			return "redirect:/";	// 리다이렉트
		}
		
		model.addAttribute("todo", todo);
		
		return "todo/update";
	}
	
	/**
	 * 
	 * @param todo : todoTitle, todoDetail, todoNo
	 * @param ra
	 * @return
	 */
	@PostMapping("update")
	public String updateTodo(
			@ModelAttribute Todo todo,	// 제출되는 파라미터의 name 속성과 필드명이 일치하면 커맨드 객체 생성
			RedirectAttributes ra
			) {
		
		int result = service.updateTodo(todo);
		
		String message = null;
		String path    = null;
		
		if (result > 0) {
			message = "수정 성공";
			path    = "redirect:/todo/detail/" + todo.getTodoNo();
		} else {
			message = "수정 실패";
			path    = "redirect:/todo/update?todoNo=" + todo.getTodoNo();
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
	
	
	@GetMapping("delete")
	public String deleteTodo(
			@RequestParam("todoNo") int todoNo,
			RedirectAttributes ra) {
		
		// 상세 조회 서비스 호출
		int result = service.deleteTodo(todoNo);
		
		String message = null;
		String path    = null;
		
		if (result > 0) {
			message = "삭제 성공";
			path    = "redirect:/";
			
		} else {
			message = "삭제 실패";
			path    = "redirect:/todo/detail/" + todoNo;
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
	
	
}
