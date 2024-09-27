package edu.kh.project.myPage.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.exception.FileUploadFailException;
import edu.kh.project.common.util.FileUtil;
import edu.kh.project.member.dto.Member;
import edu.kh.project.myPage.mapper.MyPageMapper;

@Transactional
@PropertySource("classpath:/config.properties")
@Service
public class MyPageServiceImpl implements MyPageService{
	
	@Autowired
	private MyPageMapper mapper;
	
	@Autowired // BCrypt 암호화 객체 의존성 주입 받기
	private BCryptPasswordEncoder encoder;
	
	@Value("${my.profile.web-path}")
	private String profileWebPath; // 웹 접근 경로
	
	@Value("${my.profile.folder-path}")
	private String profileFolderPath; // 이미지 저장 서버 경로

	
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
	
	
	// 회원 프로필 이미지 수정
	@Override
	public String profile(MultipartFile profileImg, int memberNo) {
		// 파일 업로드 확인!!!!
		if (profileImg.isEmpty()) {
			
			// 제출된 파일이 없음 == X 버튼 눌러 기본 이미지로 변경
			// == DB 에 저장된 이미지 경로가 NULL
			int result = mapper.profile(null, memberNo);
			
			return null;
		}
		
		// 파일명 변경
		String rename = FileUtil.rename(profileImg.getOriginalFilename());
		
		// 3) 웹 접근 경로(config.properties) + 변경된 파일명 준비
		String url = profileWebPath + rename;
		
		
		// 4) DB UPDATE
		int result = mapper.profile(url, memberNo);
		
		if (result == 0) return null; // 업데이트 실패 시 null 반환
		
		try {
			// C:/uploadFiles/profile/  폴더가 없으면 생성
			File folder = new File(profileFolderPath);
			if(!folder.exists()) folder.mkdirs();
			
			// 업로드되어 임시저장된 이미지를 지정된 경로에 옮기기
			profileImg.transferTo(
					new File(profileFolderPath + rename));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileUploadFailException("프로필 이미지 수정 실패");
		}
		
		return profileWebPath + rename;
	}
}
