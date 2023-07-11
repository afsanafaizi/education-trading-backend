package er.repository;

import er.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class UserRepository implements PanacheRepository<User> {}