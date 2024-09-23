package edu.kh.project.myPage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.dto.Member;
import edu.kh.project.myPage.mapper.MyPageMapper;

@Transactional
@Service
public class MyPageServiceImpl implements MyPageService{
	
	@Autowired
	private MyPageMapper mapper;
	
	@Autowired // BCrypt 암호화 객체 의존성 주입 받기
	private BCryptPasswordEncoder encoder;

	
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
	
	@Override
	public int chagePw(String currentPw, String newPw, Member loginMember) {
		// 1) 입력 받은 현재 비밀번호가
		//    로그인한 회원의 비밀번호와 일치하는지 검사
		//    (BCryptPasswordEncoder.match() 이용
		
		// 비밀번호가 일치하지 않으면
		if (!encoder.matches(currentPw, loginMember.getMemberPw())) {
			return 0;
		}
		
		// 2) 새 비밀번호 암호화
		String encPw = encoder.encode(newPw);
		
		// 3) loginMember에 저장된 비밀번호 갱신
		//    얕은 복사이기 때문에 세션 값 변경됨
		//    비밀번호 연속 변경 가능하게 함!
		loginMember.setMemberPw(encPw);

		// 4) DB 비밀번호 변경(회원 번호, 암호화된 새 비밀번호
		return mapper.changePw(loginMember.getMemberNo(), encPw);
	}
	
	
	@Override
	public int secession(String memberPw, Member loginMember) {
		// 1) 비밀번호 일치 검사
		if (!encoder.matches(memberPw, loginMember.getMemberPw())) {
			return 0;
		}
		
		return mapper.secession(loginMember.getMemberNo());
	}
}
