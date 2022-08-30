package petfriends.payment.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import petfriends.AbstractEvent;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PointPayed extends AbstractEvent {
 	
    private String userId;
    private String PointPayKind;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Timestamp changeDate;
    
}