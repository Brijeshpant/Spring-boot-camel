package com.brij;

import com.brijeshpant.soap.gen.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mappings(value = {
            @Mapping(target = "name", expression = "java(entity.getFirstName() + ' ' + entity.getLastName())"),
            @Mapping(target = "address", source = "entity.address"),
            @Mapping(target = "empId", source = "entity.id"),
            @Mapping(target = "email", source = "entity.emailId")
    })
    Employee personToPersonDTO(Person entity);
}