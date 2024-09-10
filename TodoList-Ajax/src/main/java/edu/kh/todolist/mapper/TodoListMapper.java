package edu.kh.todolist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todolist.dto.Todo;

@Mapper// 마이바티스 어노테이션 // 상속 받은 클래스 를 생성 후 Bean 으로 등록
public interface TodoListMapper {

	/**
	 * 할 일 목록 조회
	 * @return result
	 */
	List<Todo> selectTodoList();

	/**
	 * 완료된 할 일 개수 조회
	 * @return result
	 */
	int selectCompleteCount();

	/**
	 * 할 일 추가
	 * @param todo
	 * @return result
	 */
	int addTodo(Todo todo);
	
	/**
	 * 할 일 제목 찾기
	 * @param todoNo
	 * @return
	 */
	String searchTitle(int todoNo);

	/**
	 * 할 일 상세 조회
	 * @param todoNo
	 * @return
	 */
	Todo detailTodo(int todoNo);

	/**
	 * 완료 여부 변경
	 * @param todoNo
	 * @return result
	 */
	int updateComplete(int todoNo);

	/**
	 * 할 일 수정
	 * @param todo
	 * @return result
	 */
	int updateTodo(Todo todo);

	/**
	 * 할 일 삭제
	 * @param todoNo
	 * @return result
	 */
	int deleteTodo(int todoNo);

	
	/**
	 * 전체 할 일 개수 조회
	 * @return
	 */
	int getTotalCount();


	
	

	
	

}
