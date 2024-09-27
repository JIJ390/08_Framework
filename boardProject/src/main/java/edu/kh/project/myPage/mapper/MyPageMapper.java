package edu.kh.project.myPage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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


	int changePw(
			@Param("memberNo") int memberNo, 
			@Param("encPw") String encPw);


	
	/* 마이바티스 Mapper 인터페이스 메서드 호출 시 
	 * 별도의 어노테이션이 없다면
	 * 첫번째 매개변수만
	 * mapper.xml 파일에 전달되는 parameter 로 인식된다!!
	 * 
	 * [해결 방법]
	 * 1. DTO, 컬렉션(map) 을 이용해 묶어서 전달
	 * 2. @Param 어노테이션을 이용해 파라미터로 인식
	 * 
	 * @Param("key") 자료형 변수명
	 * - SQL 중 #{key} 자리에 들어갈 값을 지정
	 */
	
	/**
	 * 회원 탈퇴
	 * @param memberNo
	 * @return
	 */
	int secession(int memberNo);


	/**
	 * 프로필 이미지 수정
	 * @param url
	 * @param memberNo
	 * @return
	 */
	int profile(@Param("url") String url, @Param("memberNo") int memberNo);
	

}
