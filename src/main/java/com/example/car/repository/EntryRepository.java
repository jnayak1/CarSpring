package com.example.car.repository;

import com.example.car.model.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    Page<Entry> findByCarVin(String carVin, Pageable pageable);
    Optional<Entry> findByIdAndCarVin(Long id, String carVin);
}
