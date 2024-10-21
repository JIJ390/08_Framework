package edu.kh.project.chatting.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.chatting.dto.ChattingRoom;
import edu.kh.project.chatting.dto.Message;
import edu.kh.project.member.dto.Member;


@Mapper
public interface ChattingMapper {

	
	/**
	 * 채팅 상대 검색
	 * @param query
	 * @param memberNo
	 * @return
	 */
	List<Member> selectTarget(
			@Param("query") String query,
			@Param("memberNo") int memberNo);

	
	/**
	 * 채팅방 존재 여부 확인
	 * @param targetNo
	 * @param memberNo
	 * @return
	 */
	int checkChattingRoom(
			@Param("targetNo") int targetNo, 
			@Param("memberNo") int memberNo);


	/**
	 * 채팅방 개설
	 * @param map
	 * @return chattingNo
	 */
	int createChattingRoom(Map<String, Integer> map);


	/**
	 * 채팅방 목록 조회
	 * @param memberNo
	 * @return
	 */
	List<ChattingRoom> selectRoomList(int memberNo);


	/**
	 * 특정 채팅방 메시지 모두 조회
	 * @param chattingNo
	 * @param memberNo
	 * @return
	 */
	List<Message> selectMessage(
			@Param("chattingNo") int chattingNo,
			@Param("memberNo")   int memberNo);


	/**
	 * 특정 채팅방의 글 중 내가 보내지 않은 글을 읽음 처리
	 * @param chattingNo
	 * @param memberNo
	 * @return
	 */
	int updateReadFlag(
			@Param("chattingNo") int chattingNo, 
			@Param("memberNo")   int memberNo);


	/**
	 * 메시지 삽입
	 * @param msg
	 * @return
	 */
	int insertMessage(Message msg);

}
