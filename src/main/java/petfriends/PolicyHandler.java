package petfriends;

import java.sql.Timestamp;
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
    	if(walkEnded.isMe()){
            System.out.println("######## walkEndede listener  : " + walkEnded.toJson());
           
            //결제금액 
            Double amount = paymentService.getAmount(walkEnded.getReservedId());
        
            //댕주인 지급포인트
            Double earnPoint = amount * (double)0.1 ; //결제금액의 10%
            //댕주인 현재포인트
            List<Point> userPointList = paymentService.findPointAllByUserId(walkEnded.getUserId());
            Double currentPoint = (double)0;
            if(!userPointList.isEmpty()) {
            	currentPoint = userPointList.get(userPointList.size()-1).getCurrentPoint() + earnPoint;
            }
            //댕주인 저장데이터
            Point userPoint = new Point();     		
            userPoint.setCreateDate(new Timestamp(System.currentTimeMillis()));
            userPoint.setCurrentPoint(currentPoint);
            userPoint.setPoint(earnPoint);
            userPoint.setPointGubun(PointGubun.EARN);
            userPoint.setReservedId(walkEnded.getReservedId());
            userPoint.setUserId(walkEnded.getUserId());
            //댕주인 point지급 저장
            pointRepository.save(userPoint);  
            
            
            //도그워커 지급포인트
            Double dwEarnPoint = amount; //결제금액
            //댕주인 현재포인트
            List<Point> dwPointList = paymentService.findPointAllByUserId(walkEnded.getDogWalkerId());
            Double dwCurrentPoint = (double)0;
            if(!dwPointList.isEmpty()) {
            	dwCurrentPoint = dwPointList.get(dwPointList.size()-1).getCurrentPoint() + dwEarnPoint;
            }
            
            //도그워커 저장데이터
            Point dwPoint = new Point();     		
            dwPoint.setCreateDate(new Timestamp(System.currentTimeMillis()));
            dwPoint.setCurrentPoint(dwCurrentPoint);
            dwPoint.setPoint(dwEarnPoint);
            dwPoint.setPointGubun(PointGubun.EARN);
            dwPoint.setReservedId(walkEnded.getReservedId());
            dwPoint.setUserId(walkEnded.getDogWalkerId());
            
            //도그워커 point(요금)지급 저장
            pointRepository.save(dwPoint);  
            
        }
    }

}
