package petfriends.payment.dto;

import lombok.Data;
import petfriends.AbstractEvent;
//import petfriends.walk.model.SmsStatus;

@Data
public class WalkEnded extends AbstractEvent {

    private Long id;
    private String walkStartDate;		// 산책 시작 일시분(실제)
    private String walkEndDate;			// 산책 종료 일시분(실제)	
    private Long reservedId;			// 예약ID
    private String userId;				// 회원ID
    private String dogWalkerId;			// 도그워커ID
//	@Enumerated(EnumType.STRING)
//	private SmsStatus smsStatus;		// SMS 발송 상태 (START, END)	
	
}