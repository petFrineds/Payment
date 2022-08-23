package petfriends.payment.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@DynamicUpdate
@Data
@Table(name="point")
public class Point {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="point_id")
    private Long id;
    private Long paymentId;
    private Long reservedId;
    private String userId;
    private String userName;    
    @Enumerated(EnumType.STRING)
    private PointGubun pointGubun;    
	private Double point;
    private Double currentPoint;
    private String bankName;
    private String accountNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Timestamp createDate;

//	@PostPersist
//    public void onPostPersist(){
//        Payed payed = new Payed();
//        BeanUtils.copyProperties(this, payed);
//        payed.publishAfterCommit(); 
//        
//        try {
//                Thread.currentThread().sleep((long) (400 + Math.random() * 220));
//        } catch (InterruptedException e) {
//                e.printStackTrace();
//        }
//    }
//
//    @PostUpdate
//    public void onPostUpdate(){
//        Refunded refunded = new Refunded();
//        BeanUtils.copyProperties(this, refunded);
//        refunded.publishAfterCommit();
//
//    }





}
