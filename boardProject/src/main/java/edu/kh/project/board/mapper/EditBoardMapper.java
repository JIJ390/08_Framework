package edu.kh.project.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.BoardImg;

@Mapper
public interface EditBoardMapper {

	/**
	 * 게시글 insert (이미지 제외)
	 * @param inputBoard
	 * @return result
	 */
	int boardInsert(Board inputBoard);

	/**
	 * 여러 이미지 한 번에 INSERT
	 * @param uploadList
	 * @return
	 */
	int insertUploadList(List<BoardImg> uploadList);

	
	
	/**
	 * 게시글 삭제 여부 변경
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int boardDelete(
			@Param("boardNo") int boardNo, 
			@Param("memberNo") int memberNo);

	
	/**
	 * 게시글 (제목/ 내용) 수정
	 * @param inputBoard
	 * @return
	 */
	int boardUpdate(Board inputBoard);

	/**
	 * 기존에 존재하던 이미지 DB 에서 삭제
	 * @param deleteOrderList
	 * @param boardNo
	 * @return
	 */
	int deleteImage(
			@Param("orders") String deleteOrderList, 
			@Param("boardNo") int boardNo);

	/**
	 * 이미지 1 행 수정
	 * @param img
	 * @return
	 */
	int updateImage(BoardImg img);

	
	/**
	 * 새로운 이미지 1행 삽입
	 * @param img
	 * @return
	 */
	int insertImage(BoardImg img);

}
