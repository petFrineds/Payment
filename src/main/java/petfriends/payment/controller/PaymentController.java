package petfriends.payment.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import petfriends.payment.model.Payment;
import petfriends.payment.model.Point;
import petfriends.payment.model.PointPayKind;
import petfriends.payment.model.UserImage;
import petfriends.payment.repository.UserImageRepository;
import petfriends.payment.service.PaymentService;


 @RestController
 @RequestMapping("/")
 public class PaymentController {

	 @Autowired
	 PaymentService paymentService;
	
	 
	 // 이미지 업로드/조회 샘플 /////////////////////////////////////
	 /**
		  CREATE TABLE `user_image` (
		`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
		`user_id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
		`user_name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
		`mime_type` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
		`user_image` BLOB NULL DEFAULT NULL,
		`original_name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
		`create_date` TIMESTAMP NULL DEFAULT NULL,
		PRIMARY KEY (`id`) USING BTREE
	)
	COLLATE='utf8mb4_general_ci'
	ENGINE=InnoDB
	AUTO_INCREMENT=3041
	;

	*/
	 
	 
	 @Autowired
	 UserImageRepository userImageRepository;
  
     @PostMapping("/payments/upload")
     public Long handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
  
         UserImage userImage = new UserImage();
         userImage.setMimeType(file.getContentType());
         userImage.setOriginalName(file.getOriginalFilename());
         userImage.setUserImage(file.getBytes());
         UserImage saveUuserImg =  userImageRepository.save(userImage);
         
         return saveUuserImg.getId();
     }
     

    @GetMapping("/payments/imgae/{id}")
    public ResponseEntity<byte[]> findOne(@PathVariable Long id) {
    	    Optional<UserImage> user = userImageRepository.findById(id);
    	    
    	    if(user.isPresent()) {
    	    	
    	    	UserImage userImage = user.get();
    	    	HttpHeaders headers = new HttpHeaders();
    	        headers.add("Content-Type", userImage.getMimeType());
    	        headers.add("Content-Length", String.valueOf(userImage.getUserImage().length));
    	    	return new ResponseEntity<byte[]>(userImage.getUserImage(), headers, HttpStatus.OK);
    	    }
    	    
    	   return null;
 
    }
        
	 // 이미지 업로드/조회 샘플 끝////////////////////////////////////////// 
    
    
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
	 
	 /** 포인트 현금전환 : 
	   필수para : pointGubun : "ENCASH"
	           : bankName 
	           : accountNumber
	           : currentPoint //현재포인트
	           : point //현금전환할포인트
	  처리      : currentPoint = currentPoint - point 하여 INSERT      
     */        
	 @PostMapping("/points")
	 public Point changePoint(@Valid @RequestBody Point point) {
		 
		 if(point.getPointGubun() != null & PointPayKind.ENCASH.equals(point.getPointGubun())) {
			 if(point.getBankName() == null ) {
				 new RuntimeException( "은행명을 입력해주세요");
			 }
			 if(point.getAccountNumber() == null ) {
				 new RuntimeException( "계좌번호를 입력해주세요");
			 }
			 if(point.getPoint() > point.getCurrentPoint()) {
				 new RuntimeException( "남은 포인트가 부족합니다");
			 }
		 }
		 
		 return paymentService.changePoint(point);
	 }
	 
	 
 }

 
