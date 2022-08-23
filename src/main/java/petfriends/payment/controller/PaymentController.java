package petfriends.payment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import petfriends.payment.model.Payment;
import petfriends.payment.model.Point;
import petfriends.payment.service.PaymentService;


 @RestController
 @RequestMapping("/")
 public class PaymentController {

	 @Autowired
	 PaymentService paymentService;
	 
	 @GetMapping("/payments/pays/{userId}")
	 public List<Payment> findPaymentByUserId(@PathVariable("userId") String userId) {
		 return paymentService.findAllByUserId(userId);
	 }
	 
	 @GetMapping("/payments/{id}")
	 public Payment findPaymentById(@PathVariable("id") Long id) {
		 return paymentService.findById(id);
	 }
	 
	 @PostMapping("/payments")
	 public Payment pay(@Valid @RequestBody Payment payment) {
		 return paymentService.pay(payment);
	 }
	 
	 @PutMapping("/payments/{reservedId}")
	 	 public Payment refund(@PathVariable Long reservedId) {
		 return paymentService.refund(reservedId);
	 }
	 
	 @GetMapping("/payments/points/{userId}")
	 public List<Point> findPointAllByUserId(@PathVariable("userId") String userId) {
		 return paymentService.findPointAllByUserId(userId);
	 }
	 
	 //포인트지급 테스트용
	 @PostMapping("/points")
	 public Point earn(@Valid @RequestBody Point point) {
		 return paymentService.earn(point);
	 }
	 
	 
 }

 
