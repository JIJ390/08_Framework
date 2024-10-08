package edu.kh.project.myPage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.dto.Member;
import edu.kh.project.myPage.service.MyPageService;

// MemberContoller 확인 session scope 에 올린 loginMember 존재(model)

// @SessionAttribute's' 용도
// 1. Model 을 이용하여 값을 requuest -> session 으로 scope 변경
// 2. @SessionAttribute 를 이용해 
//		@SessionAttribute's' 에 의해서 session 에 등록된 값을 얻어올 수 있음

@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("myPage")
public class MyPageController {
	
	@Autowired
	private MyPageService service;
	
	
	/**
	 * 마이페이지(내 정보) 전환
	 * @param loginMember : 세션에 저장된 로그인한 회원 정보
	 * @param model : 데이터 전달용 객체(request)
	 * @return
	 */
	@GetMapping("info")
	public String info(
			@SessionAttribute("loginMember") Member loginMember,
			Model model
			) {
		
		// 로그인 회원 정보에 주소가 담겨 있을 경우
		if (loginMember.getMemberAddress() != null) {
			// 주소를 ',' 단위로 잘라 String[] 형태로 반환
			String[] arr = loginMember.getMemberAddress().split(",");
			
			// "04540,서울 중구 남대문로 120,2층"
			// -> {"04540", "서울 중구 남대문로 120", "2층"}
			model.addAttribute("postcode"     , arr[0]);
			model.addAttribute("address"      , arr[1]);
			model.addAttribute("detailAddress", arr[2]);
			
		}
		
		
		return "myPage/myPage-info";
	}
	
	/**
	 * 내 정보 수정
	 * @param inputMember : 수정할 닉네임, 전화번호, 주소
	 * @param loginMember	: 현재 로그인된 회원 정보(session)
	 * 			  session 에 저장된 Member 객체의 '주소'가 반환됨 (얕은 복사)
	 * 				== session 에 저장된 Member 객체의 데이터를 수정할 수 있음!
	 * 				ex) 여기서 loginMember 필드값을 바꿀 경우 session 내부 정보도 변화!!!
	 * @param ra : 리다이렉트 시 request scope 로 값 전달
	 * @return
	 */
	@PostMapping("info")
	public String updateInfo(
			@ModelAttribute Member inputMember,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra) {
		
		// @SessionAttribute("key") 
		// @SessionAttribute's' 를 통해 session 에 올라간 값을 얻어오는 어노테이션
		
		// - 사용 방법
		// 1) 클래스 위에 @SessionAttribute"s" 어노테이션을 작성하고
		//    해당 클래스에서 꺼내서 사용할 값의 key 를 작성
		//    --> 그럼 세션에서 값을 미리 얻어와 놓음
		
		// 2) 필요한 메서드 매개 변수에
		//    @SessionAttribute("key") 를 작성하면
		//    해당 key 와 일치하는 session 값을 얻어와서 대입
		
		// 1. inputMember 에 로그인된 회원 번호를 추가
		inputMember.setMemberNo(loginMember.getMemberNo());
		
		// 2. 회원 정보 수정 서비스 메서드 호출
		int result = service.updateInfo(inputMember);
		
		// 3. 수정 결과에 따라 message 지정
		String message = null;
		
		if (result > 0) {
			message = "수정 되었습니다";
			// 4. session(loginMember) 도 업데이트된 DB 에 맞춰 갱신해야 함
			//    session 저장된 로그인 회원 정보를
			//    수정 값으로 변경해서 DB와 같은 데이터를 가지게함
			//    == 동기화
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());
			
		} else message = "수정 실패";
		
		ra.addFlashAttribute("message", message);
		// -> footer.html 에서 alert() 수행
		
		// 4. session(loginMember) 도 업데이트된 DB 에 맞춰 갱신해야 함
		
		loginMember.setMemberNickname(inputMember.getMemberNickname());
		loginMember.setMemberTel(inputMember.getMemberTel());
		loginMember.setMemberAddress(inputMember.getMemberAddress());
		
		return "redirect:info"; //상대경로! /myPage/info
	}
	
	
	/** (비동기) 닉네임 중복 검사
	 * @param input
	 * @return 0 : 중복X / 1 : 중복O
	 */
	@ResponseBody // 응답 본문(ajax 코드) 에 값 그대로 반환
	@GetMapping("checkNickname")
	public int checkNickname(
			@RequestParam("input") String input) {
		return service.checkNickname(input);
	}
	
	/**
	 * 비밀 번호 변경 화면 전환
	 * @return
	 */
	@GetMapping("changePw")
	public String changePw() {
		return "myPage/myPage-changePw";
	}
	
	/**
	 * 실제 비밀 번호 변경 수행
	 * @param currentPw : 현재 비밀번호
	 * @param newPw     :   새 비밀번호
	 * @param loginMember : 현재 로그인된 회원 정보(session)
	 * @param ra : 리다이렉트 시 request scope 로 데이터 전달하는 객체
	 * 						 리다이렉트 시 일회성으로 전달할 데이터가 있을 경우 사용
	 * @return
	 */
	@PostMapping("changePw")
	public String changePw(
			@RequestParam("currentPw") String currentPw,
			@RequestParam("newPw")     String newPw,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra
			) {
		
		// 서비스 호출 후 결과 반환 받기
		int result = service.chagePw(currentPw, newPw, loginMember);
		
		String message = null;
		String path    = null;
		
		// 결과에 따른 응답 제어
		if (result > 0) {
			message = "비밀번호가 변경 되었습니다";
			path = "info"; // 내 정보 페이지로 리다이렉트
			
		} else {
			message = "현재 비밀번호가 일치하지 않습니다";
			path = "changePw";	// 비밀번호 변경 페이지로 리다이렉트
		}
		
		ra.addFlashAttribute("message", message);
		
		// 현재 컨트롤러 메서드 매핑 주소 : /myPage/changePw(POST)
		// 리다이렉트 주소 : /myPage/info || /myPage/changePw(GET) 
		// 상대경로(앞에 '/' 없음) 으로 작성, redirect는 무조건 GET 방식!!
		return "redirect:" + path;
	}
	
	
	/**
	 * 탈퇴 화면 전환
	 * @return
	 */
	@GetMapping("secession")
	public String secession() {
		return "myPage/myPage-secession";
	}

	
	/**
	 * 회원 탈퇴 수행
	 * @param memberPw : 입력된 비밀번호 (일치하는 지 확인 목적)
	 * @param loginMember : 현재 로그인한 회원 정보(세션)
	 * @param ra	: redirect 시 request scope 일회용 데이터 전달
	 * @param status : @sessionAttributes 로 관리되는 세션 데이터의 상태 제어(세션 만료)
	 * @return
	 */
	@PostMapping("secession")
	public String secession(
			@RequestParam("memberPw") String memberPw,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra,
			SessionStatus status) {
		// 서비스 호출 후 결과 반환 받기
		int result = service.secession(memberPw, loginMember);
		
		String message = null;
		String path = null;
		
		if (result > 0) {
			message = "탈퇴 처리 되었습니다";
			path = "/";	// 메인 페이지 리다이렉트
			// 로그아웃 (세션 만료)
			status.setComplete();
			
		} else {
			message = "비밀번호가 일치하지 않습니다";
			path = "secession"; // 탈퇴 페이지
		}
		
		ra.addFlashAttribute("message", message);

		
		return "redirect:" + path;
	}
	
	
	@GetMapping("profile")
	public String profile() {
		return "myPage/myPage-profile";
	}
	
	
	/**
	 * 로그인한 회원의 프로필 이미지 수정
	 * @param profileImg  제출된 이미지
	 * @param loginMember 로그인한 회원 정보
	 * @param ra					리다이렉트 시 request scope 로 값 전달
	 * @return
	 */
	@PostMapping("profile")
	public String profile(
			@RequestParam("profileImg") MultipartFile profileImg,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra)	{
		
		// 1) 로그인한 회원의 회원 번호 얻어오기
		int memberNo = loginMember.getMemberNo();
		
		// 2) 업로드된 이미지로 프로필 이미지 변경
		String filePath = service.profile(profileImg, memberNo);
		
		// 3) 응답 처리
		String message = null;
		
		message = "프로필 이미지가 변경되었습니다";
			
		// DB, Session 에 저장된 프로필 이미지 정보 동기화, 갱신
		// loginMember가 주소를 참조하고 있기 때문(얕은 복사)
		loginMember.setProfileImg(filePath);
			
		// if 문 필요 없어짐

		
		ra.addFlashAttribute("message", message);
		
		return "redirect:profile"; // /myPage/profile (GET)
	}
}
