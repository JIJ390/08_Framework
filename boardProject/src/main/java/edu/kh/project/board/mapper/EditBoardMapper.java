package edu.kh.project.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

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

}
