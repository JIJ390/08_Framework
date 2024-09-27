package edu.kh.project.myPage.service;

import org.springframework.web.multipart.MultipartFile;

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

	
	/**
	 * 
	 * @param memberPw
	 * @param loginMember
	 * @return
	 */
	int secession(String memberPw, Member loginMember);

	
	/**
	 * 회원 프로필 이미지 수정
	 * @param profileImg
	 * @param memberNo
	 * @return
	 */
	String profile(MultipartFile profileImg, int memberNo);

}
