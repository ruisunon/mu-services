package io.cita.app.mapper;

import io.cita.app.model.domain.entity.SaloonTag;
import io.cita.app.model.dto.SaloonDto;
import io.cita.app.model.dto.SaloonTagDto;
import io.cita.app.model.dto.TagDto;
import lombok.NonNull;

public interface SaloonTagMapper {
	
	public static SaloonTagDto toDto(@NonNull final SaloonTag saloonTag) {
		return SaloonTagDto.builder()
				.saloonId(saloonTag.getSaloonId())
				.tagId(saloonTag.getTagId())
				.taggedDate(saloonTag.getTaggedDate())
				.identifier(saloonTag.getIdentifier())
				.saloonDto(
					SaloonDto.builder()
						.id(saloonTag.getSaloon().getId())
						.identifier(saloonTag.getSaloon().getIdentifier())
						.code(saloonTag.getSaloon().getCode())
						.taxRef(saloonTag.getSaloon().getTaxRef())
						.name(saloonTag.getSaloon().getName())
						.isPrimary(saloonTag.getSaloon().getIsPrimary())
						.openingDate(saloonTag.getSaloon().getOpeningDate())
						.fullAdr(saloonTag.getSaloon().getFullAdr())
						.email(saloonTag.getSaloon().getEmail())
						.build())
				.tagDto(
					TagDto.builder()
						.id(saloonTag.getTag().getId())
						.identifier(saloonTag.getTag().getIdentifier())
						.name(saloonTag.getTag().getName())
						.description(saloonTag.getTag().getDescription())
						.build())
				.build();
	}
	
}




