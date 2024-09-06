package edu.kh.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.demo.dto.UserDTO;
import edu.kh.demo.mapper.TestMapper;


//@Service
//- Service 역할(비즈니스 로직 처리)임 명시
//- Bean 등록 (== Spring이 관리하는 객체 == IOC )
@Service
public class UserServicerImpl implements UserService{

	
	/* @Autowired
	 * - 등록된 Bean 중에서
	 *   자료형이 같은 Bean을 얻어와 필드에 대입
	 *   == DI(의존성 주입) 개발자가 직접 객체를 생성하지 않고 만들어둔 객체를 주입
	 *   
	 * TestMapper 인터페이스 가 @Mapper 어노테이션을 사용해
	 * 해당 인터페이스를 상속한 클래스가 bean 으로 등록되어 있음!!
	 */
	@Autowired
	private TestMapper mapper;
//private TestDao    dao = new TestDaoImpl();

	// 사용자 이름 조회 
	@Override
	public String selectUserName(int userNo) {
		return mapper.selectUserName(userNo);
		// 일반 JDBC 에서는 CONN 열고 닫는 등 네 줄 코딩함
	}

	
	// 사용자 전체 조회
	@Override
	public List<UserDTO> selectAll() {
		// mapper : 의존성 주입(DI) 받은 TestMapper 를 상속받아
    //          상속 받아 구현된 클래스로 만들어진 bean (객체)
		return mapper.selectAll();
	}
	
	// userNo 가 일치하는 사용자 조회
	@Override
	public UserDTO selectUser(int userNo) {
		// TODO Auto-generated method stub
		return mapper.selectUser(userNo);
	}
	
	// 사용자 정보 수정(DML)
	// -> DML 수행하면 트랜잭션 제어 처리
	// @Transactional : 
	// - 해당 메서드 수행 중 RuntimeException 발생 시 rollback 수행
	// - 예외가 발생하지 않으면 메서드 종료 후 commit 수행
	
	@Transactional
	@Override
	public int updateUser(UserDTO user) {
	// spring 에서 sqlexceoption, ioexception 등
	// checked exception 들을 unchecked exception으로 처리해줌!!!
	// try catch 구문 안쓰는 이유!!!!!!!!
		return mapper.updateUser(user);
	}
	
	@Transactional
	@Override
	public int deleteUser(int userNo) {
		// TODO Auto-generated method stub
		return mapper.deleteUser(userNo);
	}
	
	@Transactional
	@Override
	public int insertUser(UserDTO user) {
		// TODO Auto-generated method stub
		return mapper.insertUser(user);
	}
}
