package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.dto.Member;
import edu.kh.project.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@SessionAttributes({"loginMember"})
@Controller // 요청/응답 제어 역할 명시 + bean 등록(IOC)
@RequestMapping("member") // /member 로 시작하는 요청 매핑
@Slf4j	// log 필드 자동 생성 Lombok 어노테이션
public class MemberController {
	
	@Autowired // 의존성 주입(DI) + 등록된 bean 중 같은 타입의 bean 주입
	private MemberService service;
	
	
	/**
	 * 
	 * @param memberEmail	: 제출된 이메일
	 * @param memberPw		: 제출된 비밀번호
	 * @param saveEmail   : 이메일 저장 여부(체크 안하면 null)
	 * @param ra					: 리다이렉트 시 request scope 로 값 전달하는 객체
	 * @param model			 	: 데이터 전달용 객체 (기본값 request scope)
	 * @param resp        : 응답 방법을 제공하는 객체(Cookie 제공)
	 * @return
	 */
	@PostMapping("login")
	public String login(
			@RequestParam("memberEmail") String memberEmail,
			@RequestParam("memberPw")    String memberPw,
			@RequestParam(name="saveEmail", required=false) String saveEmail,	// null 이 들어오도 오류 안나도록
			RedirectAttributes ra,
			Model model,
			HttpServletResponse resp) {
		
//		log.debug("memberEmail : {}", memberEmail);
//		log.debug("memberPw : {}", memberPw);
		
		// 로그인 서비스 호출
		Member loginMember = service.login(memberEmail, memberPw);
		
		if (loginMember == null) {
			// 아이디 또는 비밀번호 불일치 로그인 실패!!
			ra.addFlashAttribute("message", "이메일 또는 비밀번호가 일치하지 않습니다");
		} else {	
			// 로그인 성공
			// loginMember 를 session scope 에 추가
			
			// 방법1) HttpSession 이용
			// 방법2) @SessionAttributes + Model 이용 방법
			
			/* Model 을 이용해서 Session scope 에 값 추가하는 방법 */
			// 1. model 에 값 추가(request)
			model.addAttribute("loginMember", loginMember);
			
			// 2. 클래스 선언부 위에 @SessionAttributes({"key"}) 추가
			// -> key 값은 model 에 추가된 key 값 "loginMember" 작성
			// (request -> session)
			
			// @SessionAttributes :
			// Model 추가된 값 중 session scope 로 올리고 싶은 값의 key 를 작성하는 어노테이션
			
			//--------------------------------------------------------------------------------
			/* 이메일 저장 코드(Cookie) 제일 아래 설명!! */
			
			// 1. Cookie 객체 생성(jakarta)
			Cookie cookie = new Cookie("saveEmail", memberEmail);
			
			// 2. 만들어진 Cookie 사용될 경로(url)
			cookie.setPath("/"); // localhost 또는 현재 ip 이하 모든 주소
			
			// 3. Cookie 가 유지되는 시간(수명) 설정
			if (saveEmail == null) { // 체크 X
				cookie.setMaxAge(0); // 만들어지자 마자 만료
														 // == 기존에 쿠키가 있으면 덮어씌우고 없어짐
														 // 쿠키를 saveEmail 에 따라 생성하지 않는 이유
														 // 체크 여부에 따라 기존 쿠키를 지울수도 있기 때문!
			} else {
				cookie.setMaxAge(60*60*24*30); // 30 일 초 단위로 작성
			}
			
			// 4. resp 객체에 추가해서 클라이언트에 전달
			resp.addCookie(cookie);
			
			
			//--------------------------------------------------------------------------------
		}
		
		return "redirect:/"; // 메인 페이지 리다이렉트
	}

	
	/**
	 * 로그 아웃
	 * @param status
	 * @return
	 */
	@GetMapping("logout")
	public String logout(
			SessionStatus status) {
			
		/* SessionStatus 
		 * - @SessionAttributes 를 이용해 등록된 객체(값)의 상태를
		 *   관리하는 객체
		 *   
		 * - SessionStatus.setComplete();
		 *   -> 세션 상태 완료 == 없앰(만료)
		 * 
		 */
		
		// model 에 session sope 로 담긴 "loginMember" 세션 만료
		status.setComplete();
		
		return "redirect:/"; // 메인 페이지
	}
	
	/* Cookie란?
	 * - 클라이언트 측(브라우저)에서 관리하는 데이터(파일 형식)
	 * 
	 * - Cookie에는 만료기간, 데이터(key=value), 사용하는 사이트(주소)
	 *   가 기록되어 있음
	 *  
	 * - 클라이언트가 쿠키에 기록된 사이트로 요청으로 보낼 때
	 *   요청에 쿠키가 담겨져서 서버로 넘어감
	 *   
	 * - Cookie의 생성, 수정, 삭제는 Server가 관리
	 *   저장은 Client가 함
	 *   
	 * - Cookie는 HttpServletResponse를 이용해서 생성,
	 *   클라이언트에게 전달(응답) 할 수 있다
	 */
	
	
	@GetMapping("signUp")
	public String signUp() {
		return "member/signUp";
	}
	
	
	/**
	 * 회원 가입 수행
	 * @param inputMember : 입력값이 저장된 Member 커맨드 객체
	 * @param ra : 리다이렉트 시 request scope 로 값 전달
	 * @return
	 */
	@PostMapping("signUp")
	public String signUp(
			Member inputMember,
			RedirectAttributes ra) {
		
		// 회원 가입 서비스 호출
		int result = service.signUp(inputMember);
		
		// 서비스 결과에 따라 응답 제어
		String message = null;
		String path    = null;
		
		if (result > 0) {
			message = inputMember.getMemberNickname() + " 님의 가입을 환영합니다";
			path    = "/";
			
		} else {
			message = "회원 가입 실패";
			path    = "signUp";
		}
		
		ra.addFlashAttribute("message", message);
		
		
		return "redirect:" + path;
	}
	
	
	/**
	 * 이메일 중복 검사(비동기)
	 * @param email : 입력된 이메일
	 * @return 0 : 중복 X, 1 : 중복 O
	 */
	@ResponseBody
	@GetMapping("emailCheck")
	public int emailCheck(
			@RequestParam("email") String email) {
		
		return service.emailCheck(email);
	}
	
	/**
	 * 닉네임 중복 검사(비동기)
	 * @param nickname : 입력된 닉네임
	 * @return 0 : 중복 X, 1 : 중복 O
	 */
	@ResponseBody
	@GetMapping("nicknameCheck")
	public int nicknameCheck(
			@RequestParam("nickname") String nickname) {
		
		return service.nicknameCheck(nickname);
	}
	
}












