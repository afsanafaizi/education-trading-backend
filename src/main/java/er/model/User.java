package er.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
}