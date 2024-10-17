package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/* Filter : 요청, 응답 시 걸러내거나 추가할 수 있는 객체
 * 
 * [필터 생성 및 적용 방법]
 * 1. jakarta.servlet.Filter 인터페이스를 상속 받은 클래스 생성
 * 
 * 2. 상속 후 doFilter() 메서드 오버라이딩
 * 
 * 3. FilterConfig 클래스에 생성한 필터 클래스를 등록
 * 
 */


/**
 * SignUpFilter
 * 
 * 로그인이 되어있는 경우 회원 가입 요청을 막는 필터
 */
public class SignUpFilter implements Filter{
	
	//필터 동작을 정의하는 메서드
	@Override
	public void doFilter(
			ServletRequest request,		// HttpServletRequest 의 부모 타입
			ServletResponse response, // HttpServletResponse 의 부모 타입
			FilterChain chain					// 연결된 필터로 넘어가는 용도
			)
			throws IOException, ServletException {
		
		// request, response -> 요청, 응답 객체가 전달되어 저장됨
		
		// 로그인된 회원의 정보(loginMember) 가 session 에 있는지 확인
		// -> session 객체를 얻어와 확인
		
		// -> session 객체를 얻어오려면 HttpServletRequest 가 필요
		
		// HTTP 통신이 가능한 형태로 request, response 다운 캐스팅
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// 세션에 로그인한 회원의 정보가 있을 경우
		HttpSession session = req.getSession();
		
		// 로그인이 되어있는 경우
		if (session.getAttribute("loginMember") != null) {
			
			// 메인 페이지로 리다이렉트
			resp.sendRedirect("/");
			// 이 구문 아래에 있는 코드도 그대로 동작
			// 해당 구문은 모든 코드가 실행된 후 마지막으로 동작함
			
			
		} else { // 로그인이 안된 경우
			
			// 다음 필터로 이동
			// (만약 다음 필터가 없다면 Dispatcher Servlet 으로 이동)
			chain.doFilter(request, response);
			
		}
		
		
	}
}
