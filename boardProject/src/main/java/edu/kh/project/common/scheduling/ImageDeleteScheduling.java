package edu.kh.project.common.scheduling;

import java.util.List;

import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.kh.project.common.scheduling.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component	// bean 등록
@Slf4j
@PropertySource("classpath:/config.properties")
@RequiredArgsConstructor
public class ImageDeleteScheduling {
	
	private final SchedulingService service;
	
	// 0 초 시작, 20가 지날때마다 수행 (0, 20, 40초 동작)
	@Scheduled(cron = "0 0 * * * *")	// 정각마다 수행
	public void imageDeletee() {
		
		// 1. DB 에 저장되어있는 파일명 모두 조회
		// - MEMBER.PROFILE_IMG 에서 파일명만 조회
		// - BOARD.FILE_RENAME 만 조회
		// - 두 결과를 UNION 해서 하나의 SELECT 결과로 반환 받기
		
		List<String> dbFileNameList = service.getDbFileNameList();
	}

}
