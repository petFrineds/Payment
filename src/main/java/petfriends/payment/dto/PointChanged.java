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
public class PointChanged extends AbstractEvent {
 	
    private String userId;
    private Double point;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Timestamp changeDate;
    
}