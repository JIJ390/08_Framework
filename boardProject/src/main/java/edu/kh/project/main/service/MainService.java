package edu.kh.project.main.service;

import java.util.List;

import edu.kh.project.member.dto.Member;

public interface MainService {

	/**
	 * 모든 회원 정보 조회
	 * @return
	 */
	List<Member> selectMemberList();

}
