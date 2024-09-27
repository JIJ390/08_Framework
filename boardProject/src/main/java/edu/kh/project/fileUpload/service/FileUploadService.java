package edu.kh.project.fileUpload.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.fileUpload.dto.FileDto;

public interface FileUploadService {

	/**
	 * 단일 파일 업로드
	 * @param uploadFile
	 * @return filePath
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	String test1(MultipartFile uploadFile) throws IllegalStateException, IOException;

	/**
	 * 파일 목록 조회
	 * @return
	 */
	List<FileDto> selectFileList();

	/**
	 * 단일 파일 업로드 + 원본 이름 바꾸기
	 * @param uploadFile
	 * @param fileName
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	String test2(MultipartFile uploadFile, String fileName) throws IllegalStateException, IOException;

	
	/**
	 * 단일 파일 업로드 + 사용자 정의 예외를 이용한 예외 처리
	 * @param uploadFile
	 * @return
	 */
	String test3(MultipartFile uploadFile);

}
