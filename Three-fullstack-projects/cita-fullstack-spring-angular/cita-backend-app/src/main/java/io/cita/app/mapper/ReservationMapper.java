package io.cita.app.mapper;

import io.cita.app.model.domain.entity.Reservation;
import io.cita.app.model.dto.CustomerDto;
import io.cita.app.model.dto.ReservationDto;
import io.cita.app.model.dto.SaloonDto;
import lombok.NonNull;

public interface ReservationMapper {
	
	public static ReservationDto toDto(@NonNull final Reservation reservation) {
		return ReservationDto.builder()
				.id(reservation.getId())
				.identifier(reservation.getIdentifier())
				.code(reservation.getCode())
				.description(reservation.getDescription())
				.startDate(reservation.getStartDate())
				.cancelDate(reservation.getCancelDate())
				.completeDate(reservation.getCompleteDate())
				.status(reservation.getStatus())
				.customerDto(
					CustomerDto.builder()
						.id(reservation.getCustomer().getId())
						.identifier(reservation.getCustomer().getIdentifier())
						.ssn(reservation.getCustomer().getSsn())
						.firstname(reservation.getCustomer().getFirstname())
						.lastname(reservation.getCustomer().getLastname())
						.isMale(reservation.getCustomer().getIsMale())
						.email(reservation.getCustomer().getEmail())
						.phone(reservation.getCustomer().getPhone())
						.birthdate(reservation.getCustomer().getBirthdate())
						.build())
				.saloonDto(
					SaloonDto.builder()
						.id(reservation.getSaloon().getId())
						.identifier(reservation.getSaloon().getIdentifier())
						.code(reservation.getSaloon().getCode())
						.taxRef(reservation.getSaloon().getTaxRef())
						.name(reservation.getSaloon().getName())
						.isPrimary(reservation.getSaloon().getIsPrimary())
						.openingDate(reservation.getSaloon().getOpeningDate())
						.fullAdr(reservation.getSaloon().getFullAdr())
						.iframeGoogleMap(reservation.getSaloon().getIframeGoogleMap())
						.email(reservation.getSaloon().getEmail())
						.build())
				.build();
	}
	
}




