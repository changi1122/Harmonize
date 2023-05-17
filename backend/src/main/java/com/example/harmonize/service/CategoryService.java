package com.example.harmonize.service;

import com.example.harmonize.entity.Category;
import com.example.harmonize.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category GetCategoryByID(Long id){
        return categoryRepository.findById(id).get();
    }
}
