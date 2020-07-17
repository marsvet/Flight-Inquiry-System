package edu.cauc.flight_inquiry.dao;

import edu.cauc.flight_inquiry.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

  public Optional<User> findByEmail(String email);

}
