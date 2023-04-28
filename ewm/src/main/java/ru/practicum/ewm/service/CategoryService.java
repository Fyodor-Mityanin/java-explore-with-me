package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.dto.CategoryDto;
import ru.practicum.ewm.entity.dto.NewCategoryDto;
import ru.practicum.ewm.entity.mapper.CategoryMapper;
import ru.practicum.ewm.error.exeptions.ConflictException;
import ru.practicum.ewm.error.exeptions.NotFoundException;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        categoryRepository.findByNameIgnoreCase(newCategoryDto.getName()).ifPresent(
                cat -> {
                    throw new ConflictException("Category with name=" + newCategoryDto.getName() + " already exist");
                }
        );
        Category category = CategoryMapper.toObject(newCategoryDto);
        category = categoryRepository.save(category);
        return CategoryMapper.toDto(category);
    }


    public List<CategoryDto> getCategories(Integer from, Integer size) {
        PageRequest pageable = PageRequest.of(from, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        return CategoryMapper.toDtos(categories.toList());
    }

    public CategoryDto patchCategory(CategoryDto categoryDto, Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category with id=" + catId + " was not found")
        );
        category.setName(categoryDto.getName());
        category = categoryRepository.save(category);
        return CategoryMapper.toDto(category);
    }

    public void deleteCategory(Long catId) {
        if (!eventRepository.existsByCategory_id(catId)) {
            categoryRepository.deleteById(catId);
        } else {
            throw new ConflictException("Category with id=" + catId + " has events");
        }
    }

    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category with id=" + catId + " was not found")
        );
        return CategoryMapper.toDto(category);
    }
}
