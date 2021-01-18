package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateCreateDao extends JpaRepository<TemplateCreateDto, Integer> {

	List<TemplateCreateDto> findByNextCreateDate(LocalDate nextCreateDate);

}
