package petfriends.payment.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


@Entity
@DynamicUpdate
@Table(name="user_image")
@Data
public class UserImage {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    private String userId;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Timestamp createDate;
    private String mimeType;
    private byte[] userImage;
    private String originalName;
}
