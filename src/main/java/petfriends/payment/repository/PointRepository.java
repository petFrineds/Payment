package petfriends.payment.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import petfriends.payment.model.Point;

public interface PointRepository extends JpaRepository<Point, Long>{

    List<Point> findAllByUserId(String userId);    
    
    List<Point> findAllByPaymentId(Long paymentId);
    
}