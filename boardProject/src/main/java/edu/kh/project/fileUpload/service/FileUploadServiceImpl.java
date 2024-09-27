package edu.kh.project.fileUpload.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.exception.FileUploadFailException;
import edu.kh.project.common.util.FileUtil;
import edu.kh.project.fileUpload.dto.FileDto;
import edu.kh.project.fileUpload.mapper.FileUploadMapper;
import lombok.RequiredArgsConstructor;

// @Transactional
// - Unchecked Exception 발생 시 Rollback 수행

// @Transactional(rollbackFor = Exception.class)
// - Exception 이하 예외 발생 시 Rollback 수행
//	 == Checked, Unchecked 가리지 않고 예외 발생 시 롤백
@Transactional
@Service
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class FileUploadServiceImpl implements FileUploadService{
	
	private final FileUploadMapper mapper;
	
	// 인터넷 요청 주소 			/images/test/
	@Value("${my.test.web-path}")
	private String testWebPath;
	
	// 파일 저장 폴더 경로		C:/uploadFiles/test/
	@Value("${my.test.folder-path}")
	private String testFolderPath;
	
	// 단일 파일 업로드
	@Override
	public String test1(MultipartFile uploadFile) throws IllegalStateException, IOException {
		
		// MultipartFile이 제공하는 메서드
		
		// - getSize() : 파일 크기
		// - isEmpty() : 업로드한 파일이 없을 경우 true
		// - getOriginalFileName() : 원본 파일 명
		// - transferTo(경로) : 
		//    메모리 또는 임시 저장 경로에 업로드된 파일을
		//    원하는 경로에 전송(서버 어떤 폴더에 저장할지 지정)
		
		// 1) 업로드된 파일이 있는지 검사
		if (uploadFile.isEmpty()) { // 업로드 X
			return null;
		}
		
		// 2) 지정된 경로에 파일 저장
		File folder = new File(testFolderPath);
		
		if (folder.exists() == false) { // "C:/uploadFiles/" 폴더가 없을 경우
			folder.mkdirs(); // 폴더 생성
		}
		
		/* DB 에 업로드 되는 파일 정보를 INSERT 
		 * - DB INSERT -> 파일 저장 순서로 동작
		 *   만약에 파일 저장 중 예외 발생 
		 *   -> @Transactional 어노테이션 Rollback 수행
		 *   -> INSERT 취소 /// DB 먼저 후 파일 저장!!
		 * */
		
		/* 원본 파일명을 중복되지 않는 이름으로 변경 */
		String rename = FileUtil.rename(uploadFile.getOriginalFilename());
		
		
//	FileDto file2 = new FileDto();
//	
//	file2.builder().fileNo(1).build();
//	
//	FileDto file3 = new FileDto().builder
		
		// FileDto 객체를 만들어 INSERT에 필요한 정보를 set
		FileDto file = FileDto.builder()		// @Builder 로 만들어지는 메서드는 스태틱 메서드!!
													.fileOriginalName(uploadFile.getOriginalFilename())
													.fileRename(rename)	
													.filePath(testWebPath)
													.build();
		
		
		int result = mapper.fileInsert(file);
		
		// C://uploadFiles/test 에 저장되는 파일명
		// 업로드 되어 메모리 또는 임시 저장 폴더에 저장된 파일을
		// 지정된 경로(path) 에 전달하는(옮기는) 코드
		uploadFile.transferTo(new File(testFolderPath + rename));
		
		// 웹에서 접근 가능한 파일 경로(URL) 반환
		return testWebPath + rename;
	}
	
	@Override
	public List<FileDto> selectFileList() {
		return mapper.selectFileList();
	}

	@Override
	public String test2(MultipartFile uploadFile, String fileName) throws IllegalStateException, IOException {
		
		// 1) 업로드된 파일이 있는지 검사
		// if (uploadFile.getSize() <= 0)
		// if (uploadFile.getSize() <= 0)
		// if (uploadFile.getOriginalFilename().equals(""))
		if (uploadFile.isEmpty()) {	
			return null;
		}
		
		// 2) 제출된 fileName 이 없다면 기존 파일명 유지
		int index = uploadFile.getOriginalFilename().lastIndexOf(".");
		
		String ext = uploadFile.getOriginalFilename().substring(index);
		
		String originalName = fileName.equals("") ? uploadFile.getOriginalFilename() : fileName + ext;
		
		
		// 3) 파일명 변경하기	
		// static 메서드라서 클래스명+메서드명으로 객체 생성 없이 호출 가능
		String rename = FileUtil.rename(originalName);
		
		

		
		// 4) DB 에 파일 정보부터 INSERT
		FileDto file 
		= FileDto.builder()		
			.fileOriginalName(originalName) // 원본명
			.fileRename(rename) // 변경명
			.filePath(testWebPath) // 웹 접근 주소
			.build();
	
		int result = mapper.fileInsert(file);
		
		// 5) 지정된 폴더로 임시저장된 업로드 파일을 옮기기
		uploadFile.transferTo(new File(testFolderPath + rename));
		
		return testWebPath + rename;
	}
	
	
	
	// 단일 파일 업로드 + 사용자 정의 예외를 이용한 예외 처리
	@Override
	public String test3(MultipartFile uploadFile) {
		
		// 1) 업로드된 파일이 있는지 검사
		if (uploadFile.isEmpty()) return null;
		
		// 파일 이름 바꾸기
		String rename = FileUtil.rename(uploadFile.getOriginalFilename());
		
		// 3) DB 에 파일 정보 INSERT
		FileDto file 
		= FileDto.builder()
			.fileOriginalName(uploadFile.getOriginalFilename()) // 원본명
			.fileRename(rename) // 변경명
			.filePath(testWebPath) // 웹 접근 주소
			.build();
		
		// 4) 지정된 폴더로 임시저장된 업로드 파일을 옮기기
		try {
			uploadFile.transferTo(new File(testFolderPath + rename));
			
			// 사용자 정의 예외 테스트
			int a = 1;
			if (a==1) throw new RuntimeException();
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// transferTo() 는 Checked Exception 을 던지기 때문에
			
			// 1) throws 또는 try-catch 를 무조건 작성
			
			// 2) throws 작성 시 호추라는 메서드에서 
			//    추가 예외처리 코드를 작성해야되는 번거로움이 있음
			
			// 3) try-catch 작성 시
			//    메서드 내부에서 예외가 처리되아
			//    메서드 종료 시 예외가 던져지지 않아
			//    @Transaction 이 rollBack 을 수행할 수 없게 된다
			
			// [추천되는 해결 방법]
			// * try - catch 를 작성해서 Checked Exception 을 처리
			// - 호출하는 메서드에 throws 구문 작성 X
			
			// * Unchecked Exception 형태의 사용자 정의 예외 강제 발생
			// - @Transactional 어노테이션에 rollvbackFor 속성 작성안해도 롤백 처리 가능
			
			// 예외 강제 발생(Unchecked Exception)
			throw new FileUploadFailException();
			// Unchecked Exception 은 컴파일러가 자동으로 throws 구문을 메서드명에 작성해줌
			// -> 예외 발생 시 호출부로 던져지게 됨
		}
		

		
		return testWebPath + rename;
	}
}


