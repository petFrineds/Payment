package petfriends.payment.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import petfriends.payment.dto.Payed;
import petfriends.payment.dto.Refunded;

@Entity
@DynamicUpdate
@Table(name="payment")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="pay_id")
    private Long id;
    private Long reservedId;
    private String userId;
    private String userName;
    @Enumerated(EnumType.STRING)
    private PayType payType;
    private String refundYn;
    private String cardCompany;
    private String cardNumber;
    private String cardValidMonth;
    private String cardValidYear;
    private String cardCvc;
    private Double amount;
    private Double refundAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Timestamp payDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Timestamp refundDate;
    @Transient 
    private Double currentPoint;
    
	@PostPersist
    public void onPostPersist(){
        Payed payed = new Payed();
        BeanUtils.copyProperties(this, payed);
        payed.publishAfterCommit(); 
        
        try {
                Thread.currentThread().sleep((long) (400 + Math.random() * 220));
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
    }

    @PostUpdate
    public void onPostUpdate(){
        Refunded refunded = new Refunded();
        BeanUtils.copyProperties(this, refunded);
        refunded.publishAfterCommit();
    }


}
