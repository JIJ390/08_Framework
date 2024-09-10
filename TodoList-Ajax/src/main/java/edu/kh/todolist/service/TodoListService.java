package edu.kh.todolist.service;

import java.util.List;
import java.util.Map;

import edu.kh.todolist.dto.Todo;

public interface TodoListService {

	/**
	 * 할 일 목록 조회 + 완료된 할 일 개수
	 * @return map
	 */
	Map<String, Object> selectTodoList();

	
	/**
	 * 할 일 추가
	 * @param todo
	 * @return result
	 */
	int addTodo(Todo todo);

	
	/**
	 * 번호로 제목 찾기
	 * @param todoNo
	 * @return
	 */
	String searchTitle(int todoNo);
	
	/**
	 * 할 일 상세 조회
	 * @param todoNo
	 * @return result
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


	int deleteTodo(int todoNo);


	int getTotalCount();


	int getCompleteCount();


	List<Todo> getTodoList();





}
