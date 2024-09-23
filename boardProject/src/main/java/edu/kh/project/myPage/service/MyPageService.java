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

	/**
	 * 비밀번호 변경
	 * @param currentPw
	 * @param newPw
	 * @param loginMember
	 * @return result
	 */
	int chagePw(String currentPw, String newPw, Member loginMember);

	
	int secession(String memberPw, Member loginMember);

}
