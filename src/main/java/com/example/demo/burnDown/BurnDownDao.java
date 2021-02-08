package com.example.demo.burnDown;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BurnDownDao extends JpaRepository<BurnDownDto, Integer> {

}