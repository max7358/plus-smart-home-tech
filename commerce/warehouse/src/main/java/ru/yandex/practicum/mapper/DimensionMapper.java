package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.dto.DimensionDto;
import ru.yandex.practicum.model.Dimension;

@Mapper
public interface DimensionMapper {
    DimensionMapper INSTANCE = Mappers.getMapper(DimensionMapper.class);

    DimensionDto toDto(Dimension dimension);

    Dimension toDimension(DimensionDto dimensionDto);
}
