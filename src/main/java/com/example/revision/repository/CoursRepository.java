package com.example.revision.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.revision.entities.Cours;

public interface CoursRepository extends JpaRepository<Cours, Long>{
	
}
