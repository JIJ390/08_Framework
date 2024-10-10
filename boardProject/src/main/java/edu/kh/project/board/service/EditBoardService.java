package edu.kh.project.board.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.dto.Board;

public interface EditBoardService {

	/**
	 * 게시글 작성
	 * @param inputBoard
	 * @param images
	 * @return boardNo
	 */
	int boardInsert(Board inputBoard, List<MultipartFile> images);

	
	/**
	 * 게시글 삭제 여부 변경
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int boardDelete(int boardNo, int memberNo);

	
	/**
	 * 게시글 수정
	 * @param inputBoard
	 * @param images
	 * @param deleteOrderList
	 * @return
	 */
	int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList);

}
