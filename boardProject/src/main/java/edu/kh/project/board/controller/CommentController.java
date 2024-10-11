package edu.kh.project.board.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.board.dto.Comment;
import edu.kh.project.board.service.CommentService;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController // @Controller + @ResponseBody
								// 비동기 요청 처리 전용 컨틀롤러
								// return되는 모든 값을 있는 그대로 호출부로 반환
@RequiredArgsConstructor
@Slf4j
public class CommentController {

	private final CommentService service;
	
	
	
	/**
	 * 댓글 등록
	 * @param comment : 
	 *  요청 시 body 에 JSON 형태로 담겨져 제출된 데이터를 
	 *  HttpMessageConverter 가 DTO 로 변환한 객체
	 *  (boardNo, commentContent, parentCommentNo)
	 *  
	 * @param loginMember : 로그인한 회원 정보
	 * @return commentNo : 삽입된 댓글 번호
	 */
	@PostMapping("comment")	// POST == CREATE/INSERT 의미
	public int commentInsert(
			@RequestBody Comment comment,
			// HttpMessageConverter
			// JSON -> Map, DTO
			@SessionAttribute("loginMember") Member loginMember) {

		
		// 로그인 된 회원 번호를 comment 에 세팅
		comment.setMemberNo(loginMember.getMemberNo());
		
		return service.commentInsert(comment);
	}
	
	

	/**
	 * 댓글 수정 
	 * @param comment			: 
	 * @param loginMember
	 * @return
	 */
	@PutMapping("comment") // PUT == UPDATE 의미
	public int commentUpdate(
			@RequestBody Comment comment,
			@SessionAttribute("loginMember") Member loginMember
			) {
		
		
		comment.setMemberNo(loginMember.getMemberNo());
		
		log.debug("commentNo : {}",comment.getCommentNo());
		
		return service.commentUpdate(comment);
	}
	
	
	
	/**
	 * 댓글 삭제
	 * @param commentNo : 삭제하려는 댓글 번호
	 * @param loginMember : 로그인한 회원 정보
	 * @return
	 */
	@DeleteMapping("comment") // DELETE == DELETE 의미
	public int commentDelete(
			@RequestBody int commentNo,
			@SessionAttribute("loginMember") Member loginMember) {
		
		return service.commentDelete(commentNo, loginMember.getMemberNo());
	}
}
