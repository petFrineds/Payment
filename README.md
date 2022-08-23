---------------------------------------------------
1. 사용테이블
---------------------------------------------------
테이블스페이스 : petfriends

테이블생성 Script: CREATE TABLE `payment` ( 
	`pay_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`reserved_id` BIGINT(20) NULL DEFAULT NULL,
	`user_id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`user_name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`pay_type` ENUM('CARD','POINT') NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`pay_gubun` ENUM('PAY','REFUND') NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`card_number` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`amount` DOUBLE NULL DEFAULT NULL,
	`pay_date` TIMESTAMP NULL DEFAULT NULL,
	`refund_date` TIMESTAMP NULL DEFAULT NULL,
               PRIMARY KEY (`pay_id`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `point` (
	`point_id` BIGINT(20) NULL AUTO_INCREMENT,
	`payment_id` BIGINT(20) NULL DEFAULT NULL,
	`reserved_id` BIGINT(20) NULL DEFAULT NULL,
	`user_id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`user_name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`point_gubun` ENUM('PAY','EARN','REFUND','ENCAHS') NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`point` DOUBLE NULL DEFAULT NULL,
	`current_point` DOUBLE NULL DEFAULT NULL,
              `bank_name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
              `account_number` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`create_date` TIMESTAMP NULL DEFAULT NULL,
	 PRIMARY KEY (`point_id`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

---------------------------------------------------  
2. kafka설치 
---------------------------------------------------  
참고사이트 : http://www.msaschool.io/operation/implementation/implementation-seven/  

--------------------------------------------------  
3. API
--------------------------------------------------  
1) Payment에서 아래와 같이 api 통해 데이터 생성하면, mariadb[payment테이블]에 데이터 저장되고, message publish.  
    - 데이터생성(postman사용) : POST http://localhost:8082/payments/   
{
  "amount": 35000,
  "cardNumber": "12312312",
  "currentPoint": 2000,
  "payDate": "2022-08-18 14:28:33.102",
  "payGubun": "PAY",
  "payType": "CARD",
  "reservedId": 1,
  "userId": "soya95",
  "userName": "SOMINA"
}

2) 예약번호에 해당하는 결제조회 : GET http://localhost:8080/payments/{reservedid}  
3) 환불 : PUT http://localhost:8080/payments/{id}
4) 결제내역 조회(사용자별) : GET http://localhost:8080/payments/pays/soya95
5) 포인트내역조회(사용자별) : GET http://localhost:8080/payments/points/{userId}
6) 포인트이력생성(지불/환불/지급) POST http://localhost:8080/points
{"userId":"soya95", "userName":"somina", “pointGubun”:”PAY”(사용)/”REFUND”(환불)/”EARN”(지급), "point":"10000" }
6) 포인트이력생성(현금전환) POST http://localhost:8080/points 
{"userId":"soya95", "userName":"somina", “pointGubun”:”ENCASH”(현금화), “bankName”:”하나은행”,“accountNumber”:”123425113212”,”currentPoint”:”20000”, “point”:”10000” }

--------------------------------------------------  
4. 구조   
   -controller  
   -service  
   -repository  
   -dto  
   -model  
   -config : KafkaProcessor.java, WebConfig.java(CORS적용)  
--------------------------------------------------  
  
6. swagger추가 : http://localhost:8080/swagger-ui.html
