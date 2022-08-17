package petfriends.payment.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import petfriends.payment.model.PayGubun;
import petfriends.payment.model.PayType;
import petfriends.payment.model.Payment;
import petfriends.payment.model.Point;
import petfriends.payment.model.PointGubun;
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
		 		p.setPointGubun(PointGubun.PAY);
		 		p.setUserId(pay.getUserId());
		 		p.setUserName(pay.getUserName());
		 		pointRepository.save(p);
		 	}
		 			
			return pay;
	 } 
	 
	 public Payment refund(Long id) {
		 
		 Optional<Payment> payment = paymentRepository.findById(id);
		 if(payment.isPresent()) {
			 
			Payment pay = payment.get();
			pay.setPayGubun(PayGubun.REFUND);
			
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
		 		point.setPointGubun(PointGubun.REFUND);
		 		point.setUserId(pay.getUserId());
		 		point.setUserId(pay.getUserName());
		 		
		 		pointRepository.save(point);
			
			}
			
			return paymentRepository.save(pay);
	
		 } 
		 
		 return null;
	 }
}

