package petfriends.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import petfriends.payment.model.UserImage;

public interface UserImageRepository extends JpaRepository<UserImage, Long>{
    
}