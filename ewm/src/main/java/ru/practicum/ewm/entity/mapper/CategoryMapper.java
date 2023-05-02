package ru.practicum.ewm.entity.mapper;

import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.dto.CategoryDto;
import ru.practicum.ewm.entity.dto.NewCategoryDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
    public static Category toObject(NewCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public static CategoryDto toDto(Category obj) {
        return CategoryDto.builder()
                .id(obj.getId())
                .name(obj.getName())
                .build();
    }

    public static List<CategoryDto> toDtos(List<Category> objs) {
        List<CategoryDto> dtos = new ArrayList<>();
        objs.forEach(i -> dtos.add(toDto(i)));
        return dtos;
    }
}
