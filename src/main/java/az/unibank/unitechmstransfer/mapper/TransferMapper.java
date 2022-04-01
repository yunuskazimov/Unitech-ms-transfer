package az.unibank.unitechmstransfer.mapper;

import az.unibank.unitechmstransfer.dao.entity.TransferEntity;
import az.unibank.unitechmstransfer.model.TransferDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransferMapper {
    TransferMapper INSTANCE = Mappers.getMapper(TransferMapper.class);

    TransferEntity toEntity(TransferDto dto);
}
