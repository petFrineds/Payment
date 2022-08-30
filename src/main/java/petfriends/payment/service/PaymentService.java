package petfriends.payment.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		    Payment pay = paymentRepository.save(payment);
		    
		 	if(PayType.POINT.equals(pay.getPayType())) {
		 		System.out.println(pay);
		 		//현재포인트 가져와서 빼줘야함.
		 		Point p = new Point();
		 		p.setPaymentId(pay.getId());
		 		p.setReservedId(pay.getReservedId());
		 		p.setPoint(-pay.getAmount());
		 		p.setCurrentPoint(pay.getCurrentPoint()-pay.getAmount());
		 		p.setCreateDate(pay.getPayDate());
		 		p.setPointGubun(PointPayKind.PAY);
		 		p.setUserId(pay.getUserId());
		 		p.setUserName(pay.getUserName());
		 		pointRepository.save(p);
		 	}
		 			
			return pay;
	 } 
	 
	 public Payment refund(Long reservedId) {
		 
		 List<Payment> paymentList = paymentRepository.findByReservedId(reservedId);
		 if(!paymentList.isEmpty()) {
			 
			Payment pay = paymentList.get(0);
			pay.setRefundYn("Y");
			
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());    
			pay.setRefundDate(timestamp);
			
			if(PayType.POINT.equals(pay.getPayType())) {
				
				List<Point> payList = pointRepository.findAllByPaymentId(pay.getId());
				Double currentPoint = payList.get(0).getCurrentPoint();
			
		 		Point point = new Point();
		 		point.setPaymentId(pay.getId());
		 		point.setReservedId(pay.getReservedId());
		 		point.setPoint(pay.getAmount());
		 		point.setCurrentPoint(currentPoint + pay.getAmount());
		 		point.setCreateDate(pay.getPayDate());
		 		point.setPointGubun(PointPayKind.REFUND);
		 		point.setUserId(pay.getUserId());
		 		point.setUserId(pay.getUserName());
		 		
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

