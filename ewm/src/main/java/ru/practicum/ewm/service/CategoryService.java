package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.dto.CategoryDto;
import ru.practicum.ewm.entity.dto.NewCategoryDto;
import ru.practicum.ewm.entity.mapper.CategoryMapper;
import ru.practicum.ewm.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toObject(newCategoryDto);
        category = categoryRepository.save(category);
        return CategoryMapper.toDto(category);
    }
}
