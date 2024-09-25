package edu.kh.project.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.dto.Member;

@Mapper	// 인터페이스 상속 클래스 생성 + 클래스를 Bean 등록
public interface MainMapper {

	/**
	 * 모든 회원 정보 조회
	 * @return
	 */
	List<Member> selectMemberList();

}
