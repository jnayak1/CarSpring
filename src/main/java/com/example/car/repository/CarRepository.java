package com.example.car.repository;

import com.example.car.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
  Optional<Car> findByVin(String vin);
  boolean existsByVin(String vin);
}
