package edu.kh.todolist.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todolist.dto.Todo;
import edu.kh.todolist.mapper.TodoListMapper;

@Transactional	// 내부 메서드 수행 후 트랜잭션 처리 수행
								// - 예외 발생 시 rollback, 아니면 commit!!
								// SELECT(DQL) 시에도 동작하여 조금 느려짐!
@Service	// Service 역할임을 명시 + bean 으로 등록
public class TodoListServiceImpl implements TodoListService{
	
	@Autowired // 등록된 bean 중에서 같은 타입(자료형[상속관계]) 을 얻어와 대입
						 // 의존성 주입
	private TodoListMapper mapper;
	
	
	@Override
	public Map<String, Object> selectTodoList() {
		
		// 1) 할 일 목록 조회
		List<Todo> todoList = mapper.selectTodoList();
		
		// 2) 완료된 할 일 개수 조회
		int completeCount = mapper.selectCompleteCount();
		
		// 3) Map 객체 생성 후 조회 결과 담기
		Map<String, Object> map = new HashMap<>();
		
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);
		
		// 4) Map 객체 반환
		return map;
		
	}
	
//	@TransactionalInsert(DML) commit rollback 처리 필요 클래스에 작성함
	@Override
	public int addTodo(Todo todo) {
		return mapper.addTodo(todo);
	}
	
	@Override
	public String searchTitle(int todoNo) {
		// TODO Auto-generated method stub
		return mapper.searchTitle(todoNo);
	}
	
	@Override
	public Todo detailTodo(int todoNo) {
		return mapper.detailTodo(todoNo);
	}
	
	@Override
	public int updateComplete(int todoNo) {
		// TODO Auto-generated method stub
		return mapper.updateComplete(todoNo);
	}
	
	@Override
	public int updateTodo(Todo todo) {
		// TODO Auto-generated method stub
		return mapper.updateTodo(todo);
	}
	
	@Override
	public int deleteTodo(int todoNo) {
		// TODO Auto-generated method stub
		return mapper.deleteTodo(todoNo);
	}
	
	@Override
	public int getTotalCount() {
		// TODO Auto-generated method stub
		return mapper.getTotalCount();
	}
	
	@Override
	public int getCompleteCount() {
		// TODO Auto-generated method stub
		return mapper.selectCompleteCount();
	}
	
	@Override
	public List<Todo> getTodoList() {
		// TODO Auto-generated method stub
		return mapper.selectTodoList();
	}
}
