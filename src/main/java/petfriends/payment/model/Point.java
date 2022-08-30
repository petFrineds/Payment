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
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import petfriends.payment.dto.PointChanged;
import petfriends.payment.dto.PointPayed;

@Entity
@DynamicUpdate
@Data
@Table(name="point")
@NoArgsConstructor
@AllArgsConstructor

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
    private PointPayKind pointGubun;    
	private Double point;
    private Double currentPoint;
    private String bankName;
    private String accountNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Timestamp createDate;

	@PostPersist
    public void onPostPersist(){
        PointChanged point = new PointChanged();
        BeanUtils.copyProperties(this, point);
        point.setChangeDate(this.getCreateDate());
        point.publishAfterCommit(); 
        
        try {
                Thread.currentThread().sleep((long) (400 + Math.random() * 220));
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
        
        if(PointPayKind.EARN.equals(getPointGubun()) || PointPayKind.WAGE.equals(getPointGubun())) { //포인트지급일 경우 MyPage에 상태변경
	        PointPayed pointpayed = new PointPayed();
	        BeanUtils.copyProperties(this, pointpayed);
	        pointpayed.setChangeDate(this.getCreateDate());
	        pointpayed.publishAfterCommit(); 
       
	        try {
	                Thread.currentThread().sleep((long) (400 + Math.random() * 220));
	        } catch (InterruptedException e) {
	                e.printStackTrace();
	        }
        }
    }

}
