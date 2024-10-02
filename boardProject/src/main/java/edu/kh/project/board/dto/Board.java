package edu.kh.project.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// DTO(Data Transfer Object) : 계층간 데이터 전달용 객체
// - 계층? Controller, Service, DB 등을 구분

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Board {

	// 행 번호 
	private int rnum;
	
	// BOARD 테이블 컬럼과 매핑되는 필드
	// 꼭 테이블과 동일하지 않아도 됨!!!
	// 필요한 정보만 써도 상관 X
	private int    boardNo;
	private String boardTitle;
	private String boardContent;
	private String boardWriteDate;
	private String boardUpdateDate;
	private int    readCount;
	private String boardDelFl;
	private int    memberNo;
	private int    boardCode;
	
	// MEMBER 테이블 JOIN 컬럼
	private String memberNickname;
	
	// 목록 조회 시 댓글/좋아요 수 상관 쿼리 결과
	private int commentCount;
	private int likeCount;
	
	// ---------------------------------
	// (추가 작성 예정)
	
}