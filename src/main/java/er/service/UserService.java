package er.service;

import er.dto.UserDTO;
import er.model.User;
import er.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public UserDTO createUser (UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        userRepository.persist(user);

        userDTO.setId(user.getId()); // Set the generated ID to the DTO for response.

        return userDTO;
    }

    @Transactional
    public UserDTO getUserById (Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            return convertToUserDTO(user);
        }
        return null;
    }

    @Transactional
    public List<UserDTO> getAllUsers () {
        List<User> users = userRepository.listAll();
        return users.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @Transactional
    public void delete (long id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToUserDTO (User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        return userDTO;
    }

    // Add any other user-related business logic as needed.
}