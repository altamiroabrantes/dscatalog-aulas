package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findALL() {

		return categoryRepository.findAll().stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

//		List<Category> list = categoryRepository.findAll();
//		List<CategoryDTO> listDTO = new ArrayList<>();
//		for (Category cat: list) {
//			listDTO.add(new CategoryDTO(cat));
//		}

//		return listDTO;
	}

	@Transactional(readOnly = true)
	public CategoryDTO finById(Long id) {
		return new CategoryDTO(
				categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		return new CategoryDTO(categoryRepository.save(new Category(null, dto.getName())));
	}
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = categoryRepository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = categoryRepository.save(entity);
			return new CategoryDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
}
