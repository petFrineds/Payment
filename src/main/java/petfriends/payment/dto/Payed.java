package petfriends.payment.dto;

import java.sql.Timestamp;

import lombok.Data;
import petfriends.AbstractEvent;
import petfriends.payment.model.PayGubun;
import petfriends.payment.model.PayType;

@Data
public class Payed extends AbstractEvent {

    private Long id; //pay_id
    private Long reservedId;
    private String userId;
    private String userName;
    private PayType payType;
    private PayGubun payGubun;
    private String cardNumber;
    private Double amount;
    private Timestamp payDate;
    private Timestamp refundDate;
    private Double currentPoint;

}
