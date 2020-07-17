package edu.cauc.flight_inquiry.dao;

import edu.cauc.flight_inquiry.po.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AirlineDao extends JpaRepository<Airline, String>, JpaSpecificationExecutor<AirlineDao> {

  public boolean existsByZhFullName(String zhFullName);

  public List<Airline> findAllByZhSimpleNameLike(String zhSimpleNameLike);

  public List<Airline> findAllByEnSimpleNameLike(String enSimpleNameLike);

  public Optional<Airline> findByZhFullName(String zhFullName);

  public List<Airline> findAllByZhFullNameLike(String zhFullNameLike);

  public List<Airline> findAllByEnFullNameLike(String enFullNameLike);

  public Optional<Airline> findByTwoCode(String twoCode);

  public Optional<Airline> findByThreeCode(String threeCode);

  public List<Airline> findAllByType(String type);

}
