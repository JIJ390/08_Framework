package edu.kh.project.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Lombok : Java 개발 시 자주 사용하는 구문을 
//				  컴파일 시 자동 추가하는 '라이브러리'

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor			// 실질적으로 사용은 안하나 builder 사용을 위해 필요!
@Builder
@ToString
public class Member {

  private int 		memberNo;
  private String 	memberEmail;
  private String 	memberPw;
  private String 	memberNickname;
  private String 	memberTel;
  private String 	memberAddress;
  private String 	profileImg;
  private String 	enrollDate;
  private String 	memberDelFl;
  private int 		authority; 
	
	
}
