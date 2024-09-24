package edu.kh.project.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.project.member.dto.Member;
import edu.kh.project.member.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

// 왜 Service 인터페이스 상속 받을까?
// List<String> list = new ArrayList<>();
//                   = new LinkedList<>();
// 다형성 업캐스팅 + 유지 보수
// - 팀 프로젝트, 유지 보수에 굉장히 도움이 많이 되기 때문에!!
// + AOP Proxy 적용을 위해서 (아직 안배움)

@Slf4j
@Service	// 비즈니스 로직을 처리하는 역할 명시 + Bean 등록
public class MemberServiceImpl implements MemberService{
	
	@Autowired // 의존성 주입(DI) + 등록된 bean 중 같은 타입의 bean 을 대입
	private MemberMapper mapper;
	
	@Autowired // BCrypt 암호화 객체 의존성 주입 받기
	private BCryptPasswordEncoder encoder;
	
	/* 비밀번호 암호화
	 * - 하는 이유 : 평문 상태로 비밀번호 저장하면 안됨!!
	 * 
	 * - 아주 옛날 방식 : 데이터 -> 암호화, 
	 *     	     암호화된 데이터 -> 복호화 -> 원본데이터 
	 *     
	 * - 약간 과거 or 현재 : 데이터를 암호화만 가능(SHA 방식) 
	 * 										 -> 복호화 방법 제공 X (비밀번호 찾기 할때 새로 만드는 이유?)
	 *	
	 *   -> 마구 잡이로 대입해서 만들어진 암호화 데이터 테이블에 뚫림
	 * 
	 * - 요즘 많이 사용하는 방식 : BCrypt 암호화 (Spring Security 에 내장)
	 * 
	 * - 입력된 문자열(비밀번호) 에 salt 를 추가한 후 암호화
	 * 	-> 암호화할 때마다 결과가 다름
	 * 	-> DB 에 입력 받은 비밀번호를 암호화해서 넘겨줘도 비교 불가능
	 *  -> BCrypt 가 함께 제공하는 평문 암호화 데이터 비교 메서드인
	 *     matches() 를 이용하면 된다!(같으면 true, 다르면 false)
	 *     
	 *     --> matches() 메서드는 자바에서 동작하는 메서드
	 *      -> DB 에 저장된 암호환된 비밀번호를 조회해서 가져와야 한다!
	 * 	
	 */
	
	// 로그인 서비스
	@Override
	public Member login(String memberEmail, String memberPw) {
		// 비밀번호 암호화 필요!!
		// 암호화 테스트
//		log.debug("memberPw : {}", memberPw);
//		log.debug("암호화_memberPw : {}", encoder.encode(memberPw));
		
		// 1. memberEmail이 일치하는 회원의 정보를 DB 에서 조회
		//    (비밀번호 포함!)
		Member loginMember = mapper.login(memberEmail);
		
		// 2. 이메일(id) 이 일치하는 회원 정보가 없을 경우
		if (loginMember == null) {
			return null;
		}
		
		// 3. DB 에서 조회된 비밀번호와 입력받은 비밀번호가 같은지 확인
		
    // log.debug("비밀번호 일치? : {}", 
    //		 encoder.matches(memberPw, loginMember.getMemberPw()));
		
							// 입력한 비밀번호, 이메일이 일치하는 암호화 비밀번호
		if (!encoder.matches(memberPw, loginMember.getMemberPw())) {
			// 비밀 번호 불일치 시
			return null;
		}
		
		// 4 로그인 성공 / 결과 반환
		return loginMember;
	}
	
	@Override
	public int signUp(Member inputMember) {
		
		// 1) 비밀번호 암호화(BCrypt)
		String encPw = encoder.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
		
		// 2) 주소 미입력 시 null 로 변경
		// 만약 주소가 입력되지 않은 경우 null 로 변경
		// (주소 입력창에서 배열 형태로 3 개의 값이 입력
		// 값이 입력 되지 않으면 (,,) 형태로 전달됨)
		
		if (inputMember.getMemberAddress().equals(",,")) {
			inputMember.setMemberAddress(null);
			// UPDATE 구문 수행 시 MEMBER_ADDRESS 컬럼 값이 NULL 이 됨
		}	
		// text 타입의 input 은 값이 작성이 안되면 "" (빈칸)
		// checkbox, radio 가 체크가 안되면 null
		
		return mapper.signUp(inputMember);
	}
	
	
	@Override
	public int emailCheck(String email) {
		return mapper.emailCheck(email);
	}
	
	@Override
	public int nicknameCheck(String nickname) {
		return mapper.nicknameCheck(nickname);
	}
}
