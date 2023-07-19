package er.dto;

import lombok.Data;

@Data
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;

    // Constructors, getters, setters, etc.

    public UserDTO() {
    }

    public UserDTO(long id, String firstName, String lastName, String username, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    // Getters and setters for other fields (if needed).

    // Constructors, getters, setters, etc.
}