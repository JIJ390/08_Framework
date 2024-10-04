package edu.kh.project.board.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.BoardImg;
import edu.kh.project.board.mapper.EditBoardMapper;
import edu.kh.project.common.exception.FileUploadFailException;
import edu.kh.project.common.util.FileUtil;
import lombok.RequiredArgsConstructor;

@PropertySource("classpath:/config.properties")
@Service
@Transactional
@RequiredArgsConstructor
public class EditBoardServiceImpl implements EditBoardService{

	private final EditBoardMapper mapper;
	
	@Value("${my.board.web-path}") // springframework 로 import
	private String webPath; 
	
	@Value("${my.board.folder-path}") 
	private String folderPath; 
	
	// 게시글 등록
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) {
		
		// 1) 게시글 부분 (제목, 내용, 작성자 번호, 게시판 종류) INSERT
		// inputBoard 얕은 복사 게시글 번호 세팅됨
		int result = mapper.boardInsert(inputBoard);
		
		// 삽입 실패 시
		if (result == 0) return 0;
		
		/* 삽입된 게시글 번호 */
		int boardNo = inputBoard.getBoardCode();
		
		// -----------------------------------------------------
		
		// 2) 실제로 업로드된 이미지만 모아두기
		
		// 실제 업로드된 파일 정보만 모아두는 List
		List<BoardImg> uploadList = new ArrayList<>();
		
		for (int i = 0; i < images.size(); i++) {
			
			// 해당 인덱스번째 리스트에 제출된 파일이 없을 경우
			// 다음 분기 인덱스로 넘어감
			if (images.get(i).isEmpty()) continue;
			
			// 존재할 경우
			String originalName = images.get(i).getOriginalFilename();
			
			// 변경된 파일명 현재시간 + 10만 자리 + 확장자로 변경함
			String rename = FileUtil.rename(originalName);
			
			// DB INSERT 를 위한 BoardImg 객체 생성
			// builder() 스태틱 메서드임
			BoardImg img = BoardImg.builder()
										 .imgOriginalName(originalName)
										 .imgRename(rename)
										 .imgPath(webPath)
										 .boardNo(boardNo)
										 .imgOrder(i)									// 제출된 칸의 순서, 여기까지 5 개는 DB 에 삽입되는 정보
										 .uploadFile(images.get(i))		// MultipartFile 실제 업로드된 이미지 데이터
										 .build();
			
			// uploadList 에 추가
			uploadList.add(img);
		}	// for end
		
		// 제출된 이미지가 없는 경우
		if (uploadList.isEmpty()) return boardNo;
		
		
		
		
		// 3) DB 에 uploadList 에 저장된 값 모두 INSERT
		//    + transferTo() 수행해서 파일 저장
		
		/* [List 에 저장된 내용 INSERT 하는 방법]
		 * 
		 * 1. 1행을 삽입하는 mapper 메서드를 여러 번 호출
		 * 2. 여러 행을 삽입하는 mapper 메서드를 1회 호출
		 * 	  (복잡한 SQL + 동적 SQL)
		 * 
		 * */
		
		// 여러 행 한 번에 삽입 후 삽입된 행의 개수 반환
		int insertRows = mapper.insertUploadList(uploadList);
		
		// INSERT 된 행의 개수와 uploadList 의 개수가 같지 않은 경우
		if (insertRows != uploadList.size()) {
			throw new RuntimeException("이미지 INSERT 실패");
			// 예외 강제 발생으로 ROLLBACK 처리하기 위함(위쪽 BOARD 삽입도 ROLLBACK)
			// 함수가 정상적으로 종료되었을때만 COMMIT 이 됨!!
			// (사용자 정의 예외로 교체 예정)
		}
		
		// 모두 삽입 성공 시
		// 임시 저장된 파일을 서버에 지정된 폴더 + 변경명으로 저장
		try {
			
			File folder = new File(folderPath);
			
			if (folder.exists() == false) {
				// 폴더가 없을 경우
				folder.mkdirs(); // 폴더 생성
			}
			
			for (BoardImg img : uploadList) {
				img.getUploadFile()
					.transferTo(new File(folderPath + img.getImgRename()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileUploadFailException(); // 사용자 정의 예외
		}

		
		return boardNo;
	}
}
