package petfriends.payment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import petfriends.payment.model.Point;
import petfriends.payment.repository.PointRepository;

@Service
public class PointService {
	 
	 @Autowired
	 PointRepository pointRepository;
	 
	 public List<Point> findAllByUserId(String userId) {
		 return pointRepository.findAllByUserId(userId);
	 } 
	 
//	 public Payment pay(Payment payment) {
//			return pointRepository.save(payment);
//	 } 
//	 
//	 public Payment refund(Long id) {		 
//		 Optional<Payment> pay = pointRepository.findById(id);
//		 if(pay.isPresent()) {
//			 
//			Payment p = pay.get();
//			p.setPayGubun(PayGubun.REFUND);
//			
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());    
//			p.setRefundDate(timestamp);
//			
//			return pointRepository.save(p);
//			
//		}
//		 
//		return null;
//	 } 
}

