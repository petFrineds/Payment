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
@Table(name="payment")
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
    @Enumerated(EnumType.STRING)
    private PayGubun payGubun;
    private String cardNumber;
    private Double amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Timestamp payDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Timestamp refundDate;
     
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public PayGubun getPayGubun() {
		return payGubun;
	}

	public void setPayGubun(PayGubun payGubun) {
		this.payGubun = payGubun;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Timestamp getPayDate() {
		return payDate;
	}

	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}

	public Timestamp getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Timestamp refundDate) {
		this.refundDate = refundDate;
	}

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
