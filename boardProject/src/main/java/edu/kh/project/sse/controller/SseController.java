package edu.kh.project.sse.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import edu.kh.project.member.dto.Member;
import edu.kh.project.sse.dto.Notification;
import edu.kh.project.sse.mapper.SseMapper;
import edu.kh.project.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController // @Controller + @ResponseBody / 해당 클래스 내에 모든 메서드는 비동기 통신용
								// 응답 본문에 값 그대로 반환
@Slf4j
public class SseController {
	
	// 아래 emitters 필드에서 final 예약어를 사용하고 있어 
	// @RequiredArgsConstructor 사용 불가
	@Autowired
	private SseService service;
						
	// SseEmitter : 서버로부터 메시지를 전달 받을
	//              클라이언트 정보를 저장한 객체
	//              == 연결된 클라이언트
	
	// ConcurrentHashMap : 멀티스레드 환경에서 동기화를 보장하는 Map
	//                     (한번에 요청이 있어도 차례대로 처리 => 안정성 보장)
	private final Map<String, SseEmitter> emitters
		= new ConcurrentHashMap<>();
	
	/**
	 * 클라이언트 연결 요청 처리
	 * @return
	 */
	@GetMapping("sse/connect")
	public SseEmitter sseConnect(
			@SessionAttribute("loginMember") Member loginMember) {
		
		// Map 에 저장된 key 값으로 회원 번호 얻어오기
		String clientId = loginMember.getMemberNo() + "";
		
		// SseEmitter 객체 생성
		// -> 연결 대기 시간 10 분 설정 (ms 단위)
		SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);
		
		// 클라이언트 정보를 Map 에 추가
		emitters.put(clientId, emitter);
		
		// 클라이언트 연결 종료 시 Map 에서 제거
		// () -> {} 내부가 한 줄 이라 괄호 생략
		emitter.onCompletion(() -> emitters.remove(clientId));
		
		// 클라이언트 타임 아웃 시 Map 에서 제거
		emitter.onTimeout(() -> emitters.remove(clientId));
		
		return emitter;
		// return 자료형 안바꿈!! 임시로 적고 안바꿔나서 오류
	}
	
	/**
	 * 알림 메시지 전송
	 */
	@PostMapping("sse/send")
	public void sendNotification(		
			@RequestBody Notification notification,
			@SessionAttribute("loginMember") Member loginMember
			) {
		
		// 알림 보낸 회원(현재 로그인한 회원) 번호 추가
		notification.setSendMemberNo(loginMember.getMemberNo());
		
		// 전달 받은 알림 데이터를 DB 에 저장하고 
		// 알림 받을 회원의 번호
		// + 해당 회원이 읽지 않은 알림 개수 반환 받는 서비스 호출
		Map<String, Object> map
			= service.insertNotification(notification);
		
		
		// 알림을 받을 클라이언트 id(수정 예정)
		String clientId = map.get("receiveMemberNo").toString();
		
		log.debug("clientId : {}", clientId);
		
		// 연결된 클라이언트 대기 명단(emitters) 에서
		// clientId 가 일치하는 클라이언트 찾기
		SseEmitter emitter = emitters.get(clientId);
		
		log.debug(" map : {}", map);
		
		// clientId 가 일치하는 클라이언트가 있을 경우
		if (emitter != null) {
			try {		// 누가 몇 개의 알림을 안 읽었는지 저장된 map
				emitter.send(map);
				
			} catch(Exception e) {
				emitters.remove(clientId);
			}
		}
		
	}
	
	//--------------------------------------------------------------------------
	
	/**
	 * 로그인한 회원의 알림 목록 조회
	 * @param loginMeber
	 * @return
	 */
	@GetMapping("notification")
	public List<Notification> selectNotificationList(
			@SessionAttribute("loginMember") Member loginMeber
			){
		
		int memberNo = loginMeber.getMemberNo();
		
		return service.selectNotificationList(memberNo);
		
	}
	
	
	/**
	 * 현재 로그인한 회원의 알림 중
	 * 읽지 않은 알림 개수 조회
	 * ("NOTIFICATION".NOTIFICATION_CHECK = 'N')
	 * @return
	 */
	@GetMapping("notification/notReadCheck")
	public int notReadCheck(
			@SessionAttribute("loginMember") Member loginMember)	{
		
		int memberNo = loginMember.getMemberNo();
		
		return service.notReadCheck(memberNo);
	}
	
	
	
	/**
	 * 알림 삭제
	 * @param notificationNo
	 */
	@DeleteMapping("notification")
	public void deleteNotification(
			@RequestBody int notificationNo) {
		
		service.deleteNotification(notificationNo);
	}
	
	
	/**
	 * 알림 읽음 여부 변경(N -> Y)
	 * @param notificationNo
	 */
	@PutMapping("notification")
	public void updateNotification(
			@RequestBody int notificationNo) {
		
		service.updateNotification(notificationNo);
	}
 	
	
}
