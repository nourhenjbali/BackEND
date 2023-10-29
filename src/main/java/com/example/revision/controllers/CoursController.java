package com.example.revision.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.revision.entities.Class;
import com.example.revision.entities.Cours;
import com.example.revision.entities.ImageData;
import com.example.revision.payload.CoursDto;
import com.example.revision.repository.ClassRepository;
import com.example.revision.repository.CoursRepository;
import com.example.revision.services.StorageService;

import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/cours")
public class CoursController {
	@Autowired
	private StorageService service;
	@Autowired
	private CoursRepository coursRepository;
	@Autowired
	private ClassRepository classRepository;
	@Autowired
	private com.example.revision.repository.MatiereRepository MatiereRepository;
	
	@GetMapping("/classes")
	public List<com.example.revision.entities.Class> getAllClasses() {
	   return classRepository.findAll();
	}
	@GetMapping("/matieres")
	public List<com.example.revision.entities.Matiere> getAllMatieres() {
	   return MatiereRepository.findAll();
	}
	@GetMapping("/courses")
	public List<CoursDto> getAllCourses() {
		 List<Cours> coursList = coursRepository.findAll();
		    return coursList.stream().map(cours -> {
		        CoursDto coursDto = new CoursDto();
		        coursDto.setId(cours.getId());
		        coursDto.setMatiere(cours.getMatiere());
		        coursDto.setClasseName(cours.getClasse().getName());
		        coursDto.setImageName(cours.getImage().getName());
		        return coursDto;
		    }).collect(Collectors.toList());
	}
	@CrossOrigin(origins = "*")
	@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("file")MultipartFile file,@RequestParam("classe") Long id, @RequestParam("subject") String subject) throws IOException {
		System.out.println("file :"+ file + "class : " + id + "subject :" + subject );
		ImageData uploadImage = service.uploadImage(file);
		Class classe = classRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Classe not found"));
		Cours cours = new Cours();
		cours.setImage(uploadImage);
		cours.setClasse(classe);	
		cours.setMatiere(subject);
		coursRepository.save(cours);
		return new ResponseEntity<String>("{\"success\":1}", HttpStatus.OK);
	}

	@GetMapping("/{fileName}")
	public ResponseEntity<?> downloadImage(@PathVariable String fileName){
		byte[] imageData=service.downloadImage(fileName);
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.valueOf("image/png"))
				.body(imageData);

	}


}
