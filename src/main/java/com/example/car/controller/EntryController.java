package com.example.car.controller;

import com.example.car.exception.ResourceNotFoundException;
import com.example.car.model.Entry;
import com.example.car.repository.EntryRepository;
import com.example.car.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class EntryController {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/cars/{carVin}/entries")
    public Page<Entry> getAllEntriesByCarVin(@PathVariable (value = "carVin") String carVin,
                                                Pageable pageable) {
        if(!carRepository.existsByVin(carVin)) {
            throw new ResourceNotFoundException("CarVin " + carVin + " not found");
        }

        return entryRepository.findByCarVin(carVin, pageable);
    }

    @PostMapping("/cars/{carVin}/entries")
    public Entry createEntry(@PathVariable (value = "carVin") String carVin,
                                 @Valid @RequestBody Entry entry) {
        return carRepository.findByVin(carVin).map(car -> {
            entry.setCar(car);
            return entryRepository.save(entry);
        }).orElseThrow(() -> new ResourceNotFoundException("CarVin " + carVin + " not found"));
    }

    @PutMapping("/cars/{carVin}/entries/{entryId}")
    public Entry updateEntry(@PathVariable (value = "carVin") String carVin,
                                 @PathVariable (value = "entryId") Long entryId,
                                 @Valid @RequestBody Entry entryRequest) {
        if(!carRepository.existsByVin(carVin)) {
            throw new ResourceNotFoundException("CarVin " + carVin + " not found");
        }

        return entryRepository.findById(entryId).map(entry -> {
            entry.setTimeStamp(entryRequest.getTimeStamp());
            return entryRepository.save(entry);
        }).orElseThrow(() -> new ResourceNotFoundException("EntryId " + entryId + "not found"));
    }

    @DeleteMapping("/cars/{carVin}/entries/{entryId}")
    public ResponseEntity<?> deleteEntry(@PathVariable (value = "carVin") String carVin,
                              @PathVariable (value = "entryId") Long entryId) {
        return entryRepository.findByIdAndCarVin(entryId, carVin).map(entry -> {
            entryRepository.delete(entry);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Entry not found with id " + entryId + " and carVin " + carVin));
    }
}
