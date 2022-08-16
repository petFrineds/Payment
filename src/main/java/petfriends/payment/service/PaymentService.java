package petfriends.payment.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import petfriends.payment.model.PayGubun;
import petfriends.payment.model.Payment;
import petfriends.payment.repository.PaymentRepository;

@Service
public class PaymentService {
	 
	 @Autowired
	 PaymentRepository paymentRepository;
	 
	 public List<Payment> findAllByUserId(String userId) {
		 return paymentRepository.findAllByUserId(userId);
	 } 
	 
	 public Payment pay(Payment payment) {
			return paymentRepository.save(payment);
	 } 
	 
	 public Payment refund(Long id) {		 
		 Optional<Payment> pay = paymentRepository.findById(id);
		 if(pay.isPresent()) {
			 
			Payment p = pay.get();
			p.setPayGubun(PayGubun.REFUND);
			
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());    
			p.setRefundDate(timestamp);
			
			return paymentRepository.save(p);
			
		}
		 
		return null;
	 } 
}

