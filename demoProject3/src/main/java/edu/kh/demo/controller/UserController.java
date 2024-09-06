package edu.kh.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.demo.dto.UserDTO;
import edu.kh.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller // Controller 역할 명시 + bean 등록
@RequestMapping("user") // /user로 시작하는 요청 매핑
public class UserController {

	
	// 필드
	
	// 서비스 객체 DI(의존성 주입)
	@Autowired
	private UserService service;
	
	
	/** 사용자 번호를 입력 받아 일치하는 사용자의 이름 조회
	 * @param userNo : 제출된 파라미터 중 key값이 "userNo"인 값
	 * @param model  : Spring에서 사용하는 데이터 전달용 객체(request)
	 * @return
	 */
	@GetMapping("test1")
	public String selectUserName(
		@RequestParam("userNo") int userNo,
		Model model
		) {
		
		// 사용자 이름 조회 서비스 호출 후 결과 반환 받기
		String userName = service.selectUserName(userNo);
		
		System.out.println(userName);
		
		// 조회 결과를 model에 추가
		model.addAttribute("userName", userName);
		
		// classpath:/templates/user/searchName.html 요청 위임
		return "user/searchName";
	}
	
	/**
	 * 사용자 전체 조회
	 * @param model : 데이터 전달용 객체(request)
	 * @return
	 */
	@GetMapping("selectAll")
	public String selectAll(Model model) {
		// service : 의존성 주입(DI) 받은 UserServicerImpl bean
		// new 생성자 사용하지 않아도 존재!
		List<UserDTO> userList = service.selectAll();
		
		model.addAttribute("userList", userList);
		
		return "user/selectAll";		
	}
	
	
	/* RedirectAttributes
	 * - 리다이렉트 시 request scope 로 값을 전달할 수 있는 
	 * 	 스프링 제공 객체
	 * - Model 을 상속 받음
	 * 
	 * [원리]
	 * 
	 * 1) 값 세팅(request scope)
	 * 
	 * 2) 리다이렉트 수행되려고 할 때
	 * 	  1) 에서 세팅된 값을 session scope 로 잠깐(flash) 대피
	 * 
	 * 3) 리다이렉트 수행 후
	 * 		session 에 대피했던 값을 다시
	 *    request scope 로 가져옴
	 */
	
	
	/**
	 * userNo  가 일치하는 사용자 조회
	 * @param userNo : 주소에 작성된 사용자 번호
	 * @param model : 데이터 전달용 객체
	 * @param ra
	 * @return
	 */ // /user/select/12
	@GetMapping("select/{userNo}")
	public String selectUser(
			@PathVariable("userNo") int userNo,
			Model model,
			RedirectAttributes ra) {
		
		UserDTO user = service.selectUser(userNo);
		
		if(user != null) { // 조회 결과가 있을 경우
			model.addAttribute("user", user);
			return "user/selectUser";
		}
		
		// 조회 결과가 없을 경우
		
		// 목록으로 redirect
		
		// 리다이렉트 시 잠깐 session 으로 대피할 값 추가
		ra.addFlashAttribute("message", "사용자가 존재하지 않습니다");
		// request scope 라 1회성, 세션 비우기 안해도 됨!!!
		
		// redirect(재요청) 기존 resp, req 폐기
		return "redirect:/user/selectAll";
		
	}
	
	/* @ModelAttribute
	 * - 전달된 파라미터의 key(name 속성) 값이
	 *   작성된 DTO의 필드명과 일치하면
	 *   DTO 객체의 필드에 자동으로 세팅하는 어노테이션
	 *   --> 이렇게 만들어진 객체를 "커맨드 객체" 라고 함
	 * 
	 * 	생략 가능
	 */
	
	/** 사용자 정보 수정
	 * @param userNo : 주소에 포함된 userNo
	 * @param user : userNo, userPw, userName 포함된 커맨드 객체
	 * @param ra : 리다이렉트 시 request scope로 값 전달하는 객체
	 * @return
	 */
	@PostMapping("update/{userNo}")
	public String updateUser(
			@PathVariable("userNo") int userNo,	// 적지 않아도 user 에 저장됨?
			UserDTO user,
			RedirectAttributes ra
			) {
		
		log.debug("userNo : {}", userNo);
		log.debug("user   : {}", user);
		// UserDto user 
		//  == 제출된 userPw, userName + @PathVariable("userNo")
		
		// DML 수행 결과 == 결과 행의 개수 == int
		
		int result = service.updateUser(user);
		
		String message = null;
		if (result > 0) message = "수정 성공";
		else            message = "수정 실패";
		
		// ra 에 값 추가
		ra.addFlashAttribute("message", message);
		
		// 상세 조회 페이지 redirect
		return "redirect:/user/select/" + userNo;
//		return "redirect:/user/select/1";
	}
	
	
	/**
	 * 
	 * @param userNo : 
	 * @param ra
	 * @return
	 */
	@PostMapping("delete/{userNo}")
	public String deleteUser(
			@PathVariable("userNo") int userNo,
			RedirectAttributes ra) {
		
		int result = service.deleteUser(userNo);
		
		String path    = null;
		String message = null;
		
		if (result > 0) {
			path    = "redirect:/user/selectAll";	// 목록
			message = "삭제 성공";
		}
		else {
			path    = "redirect:/user/select/" + userNo; // 상세
			message = "삭제 실패";
		}
		
		// ra 에 값 추가
		ra.addFlashAttribute("message", message);
		
		return path;
		
	}
	
	/**
	 * 사용자 추가 화면으로 전환
	 * @return
	 */
	@GetMapping("insert")
	public String insertUser() {
		return "user/insertUser";
	}
	
	
	/**
	 * 사용자 추가
	 * @param user : 제출된 값이 필드에 담겨진 "커맨드 객체"
	 * @param ra
	 * @return
	 */
	@PostMapping("insert")
	public String insertUser(
			UserDTO user,
			RedirectAttributes ra) {
		
		int result = service.insertUser(user);
		
		String path    = null;
		String message = null;
		
		if (result > 0) {
			path = "redirect:/user/selectAll";
			message = user.getUserId() + "님이 추가되었습니다";
			
		} else {
			path = "redirect:/user/insert";
			message = "추가 실패";
			
		}
		// ra 에 값 추가
		ra.addFlashAttribute("message", message);
		
		return path;
	}
}


















































