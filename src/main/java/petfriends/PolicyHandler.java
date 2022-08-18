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
            
            //지급포인트
            Double earnPoint = amount * (double)0.1 ; //결제금액의 10%
            
            //현재포인트
            List<Point> userPointList = paymentService.findPointAllByUserId(walkEnded.getUserId());
            Double currentPoint = (double)0;
            if(userPointList != null || userPointList.size() > 0) {
            	currentPoint = userPointList.get(userPointList.size()-1).getCurrentPoint() + earnPoint;
            }
            
            //저장데이터
            Point point = new Point();     		
            point.setCreateDate(new Timestamp(System.currentTimeMillis()));
            point.setCurrentPoint(currentPoint);
            point.setPoint(earnPoint);
            point.setPointGubun(PointGubun.EARN);
            point.setReservedId(walkEnded.getReservedId());
            point.setUserId(walkEnded.getUserId());
            
            //point지급 저장
            pointRepository.save(point);  
        }
	   
    }
}
