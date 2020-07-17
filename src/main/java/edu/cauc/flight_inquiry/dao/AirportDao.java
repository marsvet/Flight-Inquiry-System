package edu.cauc.flight_inquiry.dao;

import edu.cauc.flight_inquiry.po.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AirportDao extends JpaRepository<Airport, String>, JpaSpecificationExecutor<Airport> {
}
