package ru.practicum.ewm.entity.mapper;

import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.dto.CategoryDto;
import ru.practicum.ewm.entity.dto.NewCategoryDto;

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
}
