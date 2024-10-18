package edu.kh.project.common.scheduling.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.common.scheduling.mapper.SchedulingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService{

	private final SchedulingMapper mapper;
	

  @Value("${my.profile.folder-path}")
  private String profilePath;		// 회원 이미지 서버 저장 경로
  
  @Value("${my.board.folder-path}")
  private String boardPath;		// 게시글 이미지 서버 저장 경로
  
	
	
	// DB 에 기록된 모든 파일명 조회
	@Override
	public List<String> getDbFileNameList() {
		
		List<String> dbFileNameList = mapper.getDbFileNameList();
		
		
//		if (dbFileNameList)
		
		// 2. 서버에 저장된 이미지 목록 모두 조회
		
		// 서버 저장 폴더를 참조(연결)
		File profileFolder = new File(profilePath); 
		File boardFolder = new File(boardPath); 
		
		// 폴더에 저장된 파일 목록을 File[] 형태로 반환 받아
		// List<File> 로 변환
		List<File> profileList = Arrays.asList(profileFolder.listFiles());
		List<File> boardList = Arrays.asList(boardFolder.listFiles());
		
		// 두 리스트를 하나로 합치기
		List<File> serverList = new ArrayList<>();
		
		serverList.addAll(profileList);
		serverList.addAll(boardList);
		
		
		// 3.dbFileNameList 와 serverList 의 파일명 비교
		// -> serverList 에는 존재하는데
		//    dbFileNameList 에 없으면
		//    서버에 저장된 이미지 삭제
		
		for(File serverFile : serverList) {
			
			// 서버 파일명이 DB 파일 목록에 없을 경우
			if (!dbFileNameList.contains(serverFile.getName())) {
				log.info("{} 삭제", serverFile.getName());
				serverFile.delete();
			}
		}
		
		log.info("-------- 이미지 삭제 스케쥴러 동작 -------");
		
		return null;
	}
}
