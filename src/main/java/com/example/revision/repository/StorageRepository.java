package com.example.revision.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.revision.entities.ImageData;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<ImageData,Long> {
    Optional<ImageData> findByName(String fileName);
}