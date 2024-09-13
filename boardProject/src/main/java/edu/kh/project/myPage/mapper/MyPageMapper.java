package edu.kh.project.myPage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.dto.Member;

								// 트랜잭션 처리!! 
								// 서비스 내 메서드 수행 중 UnChecked 발생 시 rollback 수행
								// 아니면 메서드 종료 시 commit 수행 
@Transactional	//(commit은 해당 어노테이션이 아닌 트랜잭션 매니저가 수행)
@Mapper	// xml 과 연결
public interface MyPageMapper {

	/**
	 * 
	 * @param inputMember
	 * @return
	 */
	int updateInfo(Member inputMember);

	
	int checkNickname(String input);

}
