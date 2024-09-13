package edu.kh.project.myPage.service;

import edu.kh.project.member.dto.Member;

public interface MyPageService {

	/**
	 * 회원 정보 수정
	 * @param inputMember
	 * @return
	 */
	int updateInfo(Member inputMember);

	/**
	 * 
	 * @param input
	 * @return
	 */
	int checkNickname(String input);

}
