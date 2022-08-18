package petfriends.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import petfriends.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

    List<Payment> findAllByUserId(String userId);
    
    Optional<Payment> findById(Long id);
    
    List<Payment> findByReservedId(Long reservedId);
    
}