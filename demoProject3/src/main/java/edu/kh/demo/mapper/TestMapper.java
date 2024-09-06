package edu.kh.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.demo.dto.UserDTO;

/* @Mapper
 * - 마이바티스 mapper와 연결된 인터페이스임을 명시
 * - 자동으로 해당 인터페이스를 상속 받은 클래스를 만들어
 *   Bean으로 등록함
 *   
 *   
 *   여기서 Bean 으로 등록되어 UserServiceImpl 필드에 대입
 */
@Mapper
public interface TestMapper {

	/** 사용자 이름 조회
	 * @param userNo
	 * @return userName
	 */
	String selectUserName(int userNo);
	// -> 해당 메서드가 호출된 경우
	//    연결되어져 있는 mapper.xml파일에서
	//    id 속성 값이
	//    메서드명과 같은 sql 태그가 수행된다!

	/**
	 * 사용자 전체 조회
	 * mapper.xml 파일에서 resultType 이 DTO 로 설정되엉 있는데
	 * 반환형은 List<UserDTO> 형태라서 다르다고 생각할 수 있지만
	 * 한 행이 조회될 때마다 List 에 DTO 가 add(추가) 되는 것이다!
	 * @return
	 */
	List<UserDTO> selectAll();

	
	/**
	 * userNo 가 일치하는 사용자 조회
	 * @param userNo
	 * @return
	 */
	UserDTO selectUser(int userNo);

	/**
	 * 사용자 정보 수정
	 * @param user
	 * @return
	 */
	int updateUser(UserDTO user);

	/**
	 * 사용자 삭제
	 * @param userNo
	 * @return
	 */
	int deleteUser(int userNo);

	/**
	 * 사용자 추가
	 * @param user
	 * @return
	 */
	int insertUser(UserDTO user);

}
