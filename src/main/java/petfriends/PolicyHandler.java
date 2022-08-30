package petfriends;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import petfriends.config.KafkaProcessor;
import petfriends.payment.dto.WalkEnded;
import petfriends.payment.model.Point;
import petfriends.payment.model.PointGubun;
import petfriends.payment.repository.PaymentRepository;
import petfriends.payment.repository.PointRepository;
import petfriends.payment.service.PaymentService;

@Service
public class PolicyHandler{
	
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }
    
    @Autowired
    PointRepository pointRepository;
    
    @Autowired
    PaymentRepository paymentRepository;
    
    @Autowired
    PaymentService paymentService;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverWalkEnded_(@Payload WalkEnded walkEnded){
    	
    	// 산책 종료시 포인트 지급 
    	// TODO :
    	// 1.포인트 : 책정은 일단 결제 액의 10% -> 결제금액은 reservedId로 조회하여 amount가져옴.
    	// 2.현재포인트 : 회원정보에서 가져와야하는데 임시로 포인트테이블에서 사용자이름으로 최종 포인트를 찾아옴.
    	String dogWalkerId = "";
    	String userId ="";
    	Double amount = (double)0;
    	
    	Double earnPoint =(double)0;
    	Double userCurrentPoint = (double)0;
    	Double userUpdateCurrentPoint = (double)0;
    	
    	Double dwCurrentPoint = (double)0;
    	Double dwEarnPoint = (double)0;
    	Double dwUpdateCurrentPoint = (double)0;
    	
    	if(walkEnded.isMe()){
            System.out.println("######## walkEndede listener  : " + walkEnded.toJson());
            
            ////댕주인//////////////////////////////////////////////////////////////////////////////
            userId = walkEnded.getUserId(); //userId
            amount = paymentService.getAmount(walkEnded.getReservedId()); //결제금액
            earnPoint = amount * 0.1 ; //사용자포인트(지급금액의 10%)
            
            //댕주인 현재포인트
            List<Point> userPointList = paymentService.findPointAllByUserId(userId);
            if(!userPointList.isEmpty()) {
            	userCurrentPoint = userPointList.get(userPointList.size()-1).getCurrentPoint();
            }
            
            userUpdateCurrentPoint = userCurrentPoint + earnPoint ; //새로추가될 포인트
            
            System.out.println("userId: "+ userId);
            System.out.println("amount:" + amount + "user현재포인트: "  + userCurrentPoint + ", 지급Point: " + earnPoint);
            System.out.println("user계산된현재포인트: " + userUpdateCurrentPoint);
            

            //댕주인 point데이터
            Point userPoint = new Point();     		
            userPoint.setCreateDate(new Timestamp(System.currentTimeMillis()));
            userPoint.setPoint(earnPoint);
            userPoint.setCurrentPoint(userUpdateCurrentPoint);
            userPoint.setPointGubun(PointGubun.EARN);
            userPoint.setReservedId(walkEnded.getReservedId());
            userPoint.setUserId(userId);
            
            
            ////dogWalker//////////////////////////////////////////////////////////////////////////////
            dogWalkerId = walkEnded.getDogWalkerId(); //dogWalkerId
            dwEarnPoint = amount; //도그워커 지급포인트
            //dogwalker 현재포인트
            List<Point> dwPointList = paymentService.findPointAllByUserId(dogWalkerId);
            if(!dwPointList.isEmpty()) {
            	dwCurrentPoint = dwPointList.get(dwPointList.size()-1).getCurrentPoint();
            }
            
           
            
            System.out.println("dogWalkerId: "+ dogWalkerId);
            System.out.println("amount:" + amount + "dogwalker현재포인트: "  + dwCurrentPoint + ", 지급Point: " + dwEarnPoint);
            dwUpdateCurrentPoint = dwCurrentPoint + dwEarnPoint ; //새로계산된 현재포인트
            System.out.println("dogwalker계산된현재포인트: " + dwUpdateCurrentPoint);
            
            
            //도그워커 point데이터
            Point dwPoint = new Point();     		
            dwPoint.setCreateDate(new Timestamp(System.currentTimeMillis()));
            dwPoint.setPoint(dwEarnPoint);
            dwPoint.setCurrentPoint(dwUpdateCurrentPoint);
            dwPoint.setPointGubun(PointGubun.WAGE);
            dwPoint.setReservedId(walkEnded.getReservedId());
            dwPoint.setUserId(dogWalkerId);
            
            List<Point> points = Arrays.asList(userPoint, dwPoint);
            pointRepository.saveAll(points); //user, 도그워커 point(요금)지급 저장
            
        }
    }

}
