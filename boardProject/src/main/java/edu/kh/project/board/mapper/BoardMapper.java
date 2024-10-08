package edu.kh.project.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import edu.kh.project.board.dto.Board;

@Mapper
public interface BoardMapper {

	int getListCount(int boardCode);

	/**
	 * 지정된 페이지 분향의 게시글 목록 조회
	 * @param boardCode
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectBoardList(int boardCode, RowBounds rowBounds);

	/**
	 * 게시글 상세 조회
	 * @param map
	 * @return board
	 */
	Board selectDetail(Map<String, Integer> map);

	
	/**
	 * 조회수 1 증가
	 * @param boardNo
	 * @return
	 */
	int updateReadCount(int boardNo);

	/**
	 * 좋아요 검사
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int checkBoardLike(
			@Param("boardNo") int boardNo,
			@Param("memberNo") int memberNo);

	/**
	 * 좋아요 테이블에 삽입
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int insertBoardLike(			
			@Param("boardNo") int boardNo,
			@Param("memberNo") int memberNo);

	
	/**
	 * 좋아요 테이블에서 삭제
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int deleteBoardLike(			
			@Param("boardNo") int boardNo,
			@Param("memberNo") int memberNo);

	
	/**
	 * 좋아요 개수 집계
	 * @param boardNo
	 * @return
	 */
	int getLikeCount(int boardNo);

	
	/**
	 * DB 에서 모든 게시판 종류를 조회
	 * @return
	 */
	List<Map<String, String>> selectBoardTypeList();

}
