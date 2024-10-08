package edu.kh.project.common.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BoardNameInterceptor implements HandlerInterceptor{

	// 후처리
	@Override
	public void postHandle(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Object handler,
			ModelAndView modelAndView) throws Exception {
		
		/* 게시판 관련된 요청 / 응답을 가로채서
		 * 현재 어떤 게시판을 조회/삽입/수정 하려는지 알 수 있게
		 * 게시판 이름 끼워 넣기
		 *  */
		
		// 1) application scope 에서 "boardTypeList" 얻어오기
		ServletContext application = request.getServletContext();
		
		List<Map<String, String>> boardTypeList
			= (List<Map<String, String>>)application.getAttribute("boardTypeList");
				
		// 2) "/board", "/editBoard" 로 시작하는 요청 중
		//   	 {boardCode} 부분인 2 번째 코드 값 얻어오기
		
		// Uniform Resource Identifier : 통합 자원 식별자
		// - 자원 이름(주소)만 봐도 무엇인지 구별할 수 있는 문자열
		// ex)  /editBoard/1/insert        							URI
		// ex)  http://localhost/editBoard/1/insert			URL
		
		String uri = request.getRequestURI();
		
		try {
			String code = uri.split("/")[2];		// "", "editBoard", "1", "insert"
			
			// boardTypeList 에서 boardCode 가 같은 경우의 boardName 찾기
			for (Map<String, String> boardType : boardTypeList) {
				
																				// BigDecimal 오류 big_int로 인식?? 
				String boardCode = String.valueOf(boardType.get("boardCode"));
				
				if (boardCode.equals(code)) {	// 같은 경우
					
					// request scope 에 boardName 세팅
					request.setAttribute("boardName", boardType.get("boardName"));
					break;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
}
