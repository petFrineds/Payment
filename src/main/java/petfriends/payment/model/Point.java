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

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import petfriends.payment.dto.Payed;
import petfriends.payment.dto.Refunded;

@Entity
@DynamicUpdate
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
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Timestamp createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public Long getReservedId() {
		return reservedId;
	}

	public void setReservedId(Long reservedId) {
		this.reservedId = reservedId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public PointGubun getPointGubun() {
		return pointGubun;
	}

	public void setPointGubun(PointGubun pointGubun) {
		this.pointGubun = pointGubun;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Double getCurrentPoint() {
		return currentPoint;
	}

	public void setCurrentPoint(Double currentPoint) {
		this.currentPoint = currentPoint;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
     
    

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
