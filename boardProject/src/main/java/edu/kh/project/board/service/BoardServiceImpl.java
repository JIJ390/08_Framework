package edu.kh.project.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.Comment;
import edu.kh.project.board.dto.Pagination;
import edu.kh.project.board.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor	// final 필드 생성자 자동 완성
													// + 의존성 주입이 자동으로 수행됨
public class BoardServiceImpl implements BoardService{
	
	private final BoardMapper mapper;

	// 게시글 목록 조회
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {
		// 1. boardCode 가 일치하는 게시글의 전체 개수 조회
		//		(조건 : 삭제되지 않은 글만 카운트!!)
		int listCount = mapper.getListCount(boardCode);
		
		// 2. listCount 와 cp 를 이용해서 
		//		조회될 목록 페이지,
		//		출력할 페이지네이션 의 값을 계산할 
		//		Pagination 객체 생성하기
		Pagination pagination = new Pagination(cp, listCount);
		
		// 3. DB 에서 cp (조회 하려는 페이지) 에 해당하는 행을 조회
		// ex) cp == 1,  전체 목록 중  1 ~  10 행 결과만 반환
		//     cp == 2,  전체 목록 중 11 ~  20 행 결과만 반환
		//     cp == 10, 전체 목록 중 91 ~ 100 행 결과만 반환
		
		/* [RowBounds 객체]
		 * - Mybtis 제공 객체
		 * 
		 * - 지정된 크기(offset) 만큼 행을 건너뛰고 제한된 크기(limit) 만큼의 행을 조회함
		 * 
		 * - 사용법 : Mapper 의 메서드 호출 시 2 번째 이후 매개변수로 전달
		 * 						(1 번은 SQL 에 전달할 파라미터가 기본값)
		 * */
		int limit = pagination.getLimit(); 	// 10
		int offset = (cp - 1) * limit;			// 0
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
		
		// 4. 목록 조회 결과 + Pagination 객체 Map 으로 묶어서 반환
		
		Map<String, Object> map = new HashMap<>();
		map.put(("boardList"), boardList);
		map.put(("pagination"), pagination);		// 3 줄
		
		// Map<String, Object> map2 = Map.of("boardList", boardList, "pagination", pagination); // 1 줄
		// 생성 + 바로 데이터 삽입
		
		return map;
	}
	
	
	
	// 게시글 상세조회
	@Override
	public Board selectDetail(Map<String, Integer> map) {
		
		/* [boardNo 처럼 하나의 값을 이용해
		 *  여러 번 SELECT 수행하는 경우]
		 * 
		 * 1. 하나의 Service 메서드에서
		 *    여러 Mapper 메서드 호출하기
		 *  
		 *  service -> mapper -> DB 
		 *  (필요한 SELECT 만큼 전체 반복)
		 *  
		 * 2. MyBatis 에서 제공하는 
		 * <resultMap>,<collection> 이용하기
		 * 
		 * sevice -> mapper(SELECT 연속 실행)
		 * 
		 * */
		
		
		
		return mapper.selectDetail(map);
	}
	
	
	@Override
	public int updateReadCount(int boardNo) {
		return mapper.updateReadCount(boardNo);
	}
	
	
	// 게시글 좋아요
	@Override
	public Map<String, Object> boardLike(int boardNo, int memberNo) {
		
		// 1. 좋아요 누른 적 있는지 검사
		int result = mapper.checkBoardLike(boardNo, memberNo);
		
		// result == 1, 누른 적 있음
		// result == 0, 누른 적 없음
		
		// 2. 좋아요 여부에 따라 INSERT/DELETE Mapper 호출
		int result2 = 0;
		
		if (result == 0) {
			result2 = mapper.insertBoardLike(boardNo, memberNo);
		} else {
			result2 = mapper.deleteBoardLike(boardNo, memberNo);
		}
		
		// 3. INSERT, DELETE 성공 시 해당 게시글의 좋아요 개수 조회
		int count = 0;
		
		if (result2 > 0) {
			count = mapper.getLikeCount(boardNo);
		} else {
			return null; // INSERT, DELETE 실패 시
		}
		
		// 4. 좋아요 결과를 Map 에 저장해서 반환
		Map<String, Object> map = new HashMap<>();
		
		map.put(("count"), count); // 좋아요 개수
		
		if (result == 0) map.put("check", "insert");
		else             map.put("check", "delete");
		
		
		return map;
	}
	
	
	// DB 에서 모든 게시판 종류를 조회
	@Override
	public List<Map<String, String>> selectBoardTypeList() {
		// TODO Auto-generated method stub
		return mapper.selectBoardTypeList();
	}
	
	
	
	@Override
	public List<Comment> selectCommentList(int boardNo) {
		return mapper.selectCommentList(boardNo);
	}
	
	
	// 검색 목록 조회
	@Override
	public Map<String, Object> selectSearchList(int boardCode, int cp, Map<String, Object> paramMap) {
		
		paramMap.put("boardCode", boardCode); 
		
		// 1. 지정된 게시판에서 검색 조건이 일치하는 게시글이 
		//    몇 개나 존재하는지 조회
		int searchCount = mapper.getSearchCount(paramMap);

		
		// 2. Pagination 객체 생성하기
		Pagination pagination = new Pagination(cp, searchCount);
		
		
		//3. DB에서 cp(조회 하려는 페이지)에 해당하는 행을 조회
		int limit = pagination.getLimit(); // 10
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		
		// 4. 검색 결과 + Pagination 객체 Map 으로 묶어서 반환
		List<Board> boardList = mapper.selectSearchList(paramMap, rowBounds);
		
		Map<String, Object> map = new HashMap<>();
		map.put(("boardList"), boardList);
		map.put(("pagination"), pagination);		// 3 줄
		
		return map;
	}
	
	// 현재 게시글이 포함된 목록의 페이지로 리다이렉트
	@Override
	public int getCurrentPage(Map<String, Object> paramMap) {
		return mapper.getCurrentPage(paramMap);
	}
}
