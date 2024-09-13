package edu.kh.project.myPage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.project.member.dto.Member;
import edu.kh.project.myPage.mapper.MyPageMapper;

@Service
public class MyPageServiceImpl implements MyPageService{
	
	@Autowired
	private MyPageMapper mapper;

	
	@Override
	public int updateInfo(Member inputMember) {
		// 만약 주소가 입력되지 않은 경우 null 로 변경
		// (주소 입력창에서 배열 형태로 3 개의 값이 입력
		// 값이 입력 되지 않으면 (,,) 형태로 전달됨)
		
		if (inputMember.getMemberAddress().equals(",,")) {
			inputMember.setMemberAddress(null);
			// UPDATE 구문 수행 시 MEMBER_ADDRESS 컬럼 값이 NULL 이 됨
		}
		
		return mapper.updateInfo(inputMember);
	}
	
	
	@Override
	public int checkNickname(String input) {
		return mapper.checkNickname(input);
	}
}
