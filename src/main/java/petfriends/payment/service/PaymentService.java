package petfriends.payment.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import petfriends.PaymentApplication;
import petfriends.payment.model.PayType;
import petfriends.payment.model.Payment;
import petfriends.payment.model.Point;
import petfriends.payment.model.PointPayKind;
import petfriends.payment.repository.PaymentRepository;
import petfriends.payment.repository.PointRepository;

@Service
public class PaymentService {
	 
	 @Autowired
	 PaymentRepository paymentRepository;
	 
	 @Autowired
	 PointRepository pointRepository;
	 
	 public List<Payment> findAllByUserId(String userId) {
		 return paymentRepository.findAllByUserId(userId);
	 } 
	 
	 public Payment findById(Long id) {
	
		 if(paymentRepository.findById(id).isPresent()) {
			 Optional<Payment> pay = paymentRepository.findById(id);
			 return pay.get();
		 }
		 return null;
	 } 
	 
	 public List<Point> findPointAllByUserId(String userId) {
		 return pointRepository.findAllByUserId(userId);
	 } 
	 
	 public Payment pay(Payment payment) {
			payment.setRefundYn("N"); //default값 : pay일경우
		    //Timestamp timestamp = new Timestamp(System.currentTimeMillis());    
	
			String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			System.out.println("====================> Server PayDate now : " + dateStr);
			payment.setPayDate(dateStr);
			
			
            if(PayType.CARD.equals(payment.getPayType())) {
    			//외부 카드승인 요청 서비스 호출
    			System.out.println("====================> card approval service call ");
                String a = PaymentApplication.applicationContext.getBean(petfriends.external.ExternalService.class).card_pay(payment);
                System.out.println("====================> 승인번호 오나 :  " + a );
                if(a != null || a !="") {
                	payment.setCardApprovalNumber(a);
                }
            }
            
            Payment pay = paymentRepository.save(payment); 
		    
		 	if(PayType.POINT.equals(pay.getPayType())) {
		 		System.out.println(pay);
		 		//현재포인트 가져와서 빼줘야함.
		 		Point p = new Point();
		 		p.setPaymentId(pay.getId());
		 		p.setReservedId(pay.getReservedId());
		 		p.setPoint(-pay.getAmount());
		 		p.setCurrentPoint(pay.getCurrentPoint()-pay.getAmount());
		 		p.setCreateDate(dateStr);
		 		p.setPointGubun(PointPayKind.PAY);
		 		p.setUserId(pay.getUserId());
		 		p.setUserName(pay.getUserName());
		 		pointRepository.save(p);
		 	}
		 			
			return pay;
	 } 
	 
	 public Payment refund(Long reservedId) throws RuntimeException {
		 
		 List<Payment> paymentList = paymentRepository.findByReservedId(reservedId);
		 
		  Double refundAmount = (double)0;
		 
		 if(!paymentList.isEmpty()) {
			 
			Payment pay = paymentList.get(0);
			pay.setRefundYn("Y");

			
			// 환불시 10% 차감
			refundAmount = pay.getAmount() - (pay.getAmount() * 0.1); //환불금액 : 결제금액 - 결제금액*10%
			//System.out.println("==================> 환불금액계산 : " + refundAmount);
			pay.setRefundAmount(refundAmount);
			
			String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			
		    System.out.println("RefundDate timestamp : " + dateStr);
			pay.setRefundDate(dateStr);
			
			//외부카드승인취소
			Boolean successYn = false;
			if(PayType.CARD.equals(pay.getPayType())){
				
				System.out.println("====================> card approval cancel call ");
	            successYn = PaymentApplication.applicationContext.getBean(petfriends.external.ExternalService.class).card_cancel(pay.getCardApprovalNumber());
	            System.out.println("====================> 최소성공여부 :  " + successYn );
			
	            if(successYn == false) {
	            	new RuntimeException("카드 취소에 실패했습니다.");
	            }
			}
			
			if(PayType.POINT.equals(pay.getPayType())) {
				
				//System.out.println("==================> 포인트 환불 시작");
				
				List<Point> payList = pointRepository.findAllByPaymentId(pay.getId());
				Double currentPoint = payList.get(0).getCurrentPoint();
				
				//System.out.println("==================> pay정보 : " + pay);
				
		 		Point point = new Point();
		 		point.setPaymentId(pay.getId());
		 		point.setReservedId(pay.getReservedId());
		 		point.setPoint(refundAmount); //환불포인트
		 		point.setCurrentPoint(currentPoint + refundAmount);
		 		System.out.println("point createDate timestamp : " + pay.getPayDate());
		 		point.setCreateDate(pay.getPayDate());
		 		point.setPointGubun(PointPayKind.REFUND);
		 		point.setUserId(pay.getUserId());
		 		point.setUserName(pay.getUserName());
		 		
		 		//System.out.println("==================> 포인트 저장" + point);
		 		pointRepository.save(point);
			
			}
			
			return paymentRepository.save(pay);
	
		 } 
		 
		 return null;
	 }
	 
	 // 예약번호에 대한 결제금액 리턴
	 public Double getAmount(Long reservedId) throws RuntimeException { 
		 
		 List<Payment> paymentList = paymentRepository.findByReservedId(reservedId);
		 Double amount = (double)0;
		 
		 if(paymentList == null) {
			 new RuntimeException( "(" + reservedId +")"+ "예약번호에 해당하는 결제 내역이 없습니다. ");
		 }else {
			 amount = (paymentList.get(0)).getAmount();
		 }
		 
		 return amount;
		 
	 }
	 
	 public Point changePoint(Point point) {
		 Point po = new Point();
		 BeanUtils.copyProperties(point, po);
		 po.setCurrentPoint(point.getCurrentPoint()-point.getPoint());
		 return pointRepository.save(po);
	 }
	 
	 
}

