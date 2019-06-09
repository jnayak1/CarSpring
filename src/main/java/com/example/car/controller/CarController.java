package com.example.car.controller;

import com.example.car.exception.ResourceNotFoundException;
import com.example.car.model.Car;
import com.example.car.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/cars")
    public Page<Car> getAllCars(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    @PostMapping("/cars")
    public Car createCar(@Valid @RequestBody Car car) {
        return carRepository.save(car);
    }

    @PutMapping("/cars/{carVin}")
    public Car updateCar(@PathVariable String carVin, @Valid @RequestBody Car carRequest) {
        return carRepository.findByVin(carVin).map(car -> {
            car.setTitle(carRequest.getTitle());
            car.setDealership(carRequest.getDealership());
            car.setLocation(carRequest.getLocation());
            car.setYear(carRequest.getYear());
            car.setMake(carRequest.getMake());
            car.setModel(carRequest.getModel());
            car.setTrim(carRequest.getTrim());
            car.setColor(carRequest.getColor());
            return carRepository.save(car);
        }).orElseThrow(() -> new ResourceNotFoundException("CarVin " + carVin + " not found"));
    }


    @DeleteMapping("/cars/{carVin}")
    public ResponseEntity<?> deleteCar(@PathVariable String carVin) {
        return carRepository.findByVin(carVin).map(car -> {
            carRepository.delete(car);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("CarVin " + carVin + " not found"));
    }

}
