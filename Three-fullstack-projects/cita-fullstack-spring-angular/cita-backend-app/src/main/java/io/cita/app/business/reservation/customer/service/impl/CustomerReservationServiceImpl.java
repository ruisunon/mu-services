package io.cita.app.business.reservation.customer.service.impl;

import io.cita.app.business.reservation.ReservationCommonService;
import io.cita.app.business.reservation.customer.model.CustomerReservationResponse;
import io.cita.app.business.reservation.customer.service.CustomerReservationService;
import io.cita.app.mapper.CustomerMapper;
import io.cita.app.mapper.ReservationMapper;
import io.cita.app.model.domain.ReservationStatus;
import io.cita.app.model.domain.entity.OrderedDetail;
import io.cita.app.model.domain.entity.Reservation;
import io.cita.app.model.dto.CustomerDto;
import io.cita.app.model.dto.ReservationDto;
import io.cita.app.model.dto.request.ClientPageRequest;
import io.cita.app.model.dto.request.OrderedDetailRequest;
import io.cita.app.model.dto.request.ReservationRequest;
import io.cita.app.util.ClientPageRequestUtils;
import io.cita.app.util.StringWrapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.cita.app.common.AppConstants;
import io.cita.app.exception.wrapper.CustomerNotFoundException;
import io.cita.app.exception.wrapper.OutdatedStartDateReservationException;
import io.cita.app.exception.wrapper.ReservationAlreadyExistsException;
import io.cita.app.exception.wrapper.SaloonNotFoundException;
import io.cita.app.repository.CustomerRepository;
import io.cita.app.repository.OrderedDetailRepository;
import io.cita.app.repository.ReservationRepository;
import io.cita.app.repository.SaloonRepository;

import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class CustomerReservationServiceImpl implements CustomerReservationService {
	
	private final CustomerRepository customerRepository;
	private final ReservationRepository reservationRepository;
	private final ReservationCommonService reservationCommonService;
	private final SaloonRepository saloonRepository;
	// private final ServiceDetailRepository serviceDetailRepository;
	private final OrderedDetailRepository orderedDetailRepository;
	
	@Override
	public CustomerReservationResponse fetchAllReservations(final String username, final ClientPageRequest clientPageRequest) {
		log.info("** Fetch all reservations by customer.. *");
		final var customerDto = this.retrieveCustomerByUsername(username);
		return new CustomerReservationResponse(
				customerDto,
				this.reservationRepository.findAllByCustomerId(customerDto.getId(), 
						ClientPageRequestUtils.from(clientPageRequest))
					.map(ReservationMapper::toDto));
	}
	
	@Override
	public CustomerReservationResponse searchAllByCustomerIdLikeKey(final String username, final String key) {
		log.info("** Search all reservations by customerId like key by customer.. *");
		final var customerDto = this.retrieveCustomerByUsername(username);
		return new CustomerReservationResponse(
				customerDto, 
				new PageImpl<>(this.reservationRepository
						.searchAllByCustomerIdLikeKey(
								customerDto.getId(), key.strip().toLowerCase()).stream()
							.map(ReservationMapper::toDto)
							.sorted(Comparator
									.comparing(ReservationDto::getStartDate)
									.reversed())
							.toList()));
	}
	
	@Transactional
	@Override
	public ReservationDto cancelReservation(final Integer reservationId) {
		return this.reservationCommonService.cancelReservation(reservationId);
	}
	
	@Transactional
	@Override
	public ReservationDto createReservation(final ReservationRequest reservationRequest) {
		log.info("** Create new reservation by customer.. *");
		
		if (!isReservationStartDateValid(reservationRequest))
			throw new OutdatedStartDateReservationException("Illegal Starting date reservation, plz choose a valid date");
		
		this.reservationRepository
				.findByStartDateAndStatus(
						reservationRequest.startDate(),
						ReservationStatus.NOT_STARTED).ifPresent(r -> {
			throw new ReservationAlreadyExistsException("Time requested is occupied! please choose another time");
		});
		
		final var serviceDetailsIds = reservationRequest
				.serviceDetailsIds().stream()
					.distinct()
					.sorted()
					.toList();
		
		final var reservation = Reservation.builder()
				.startDate(reservationRequest.startDate())
				.customer(this.customerRepository
						.findByCredentialUsernameIgnoringCase(reservationRequest.username().strip())
						.orElseThrow(() -> new CustomerNotFoundException(
								"Customer with username %s not found".formatted(reservationRequest.username()))))
				.saloon(this.saloonRepository
						.findById(reservationRequest.saloonId())
						.orElseThrow(SaloonNotFoundException::new))
				.description(StringWrapperUtils
						.trimIfBlank(reservationRequest.description()))
				.build();
		
		final var savedReservation = this.reservationRepository.save(reservation);
		
		final var orderedDetail = new OrderedDetail();
		
		for (int serviceDetailId: serviceDetailsIds) {
			orderedDetail.setReservationId(savedReservation.getId());
			orderedDetail.setServiceDetailId(serviceDetailId);
			orderedDetail.setOrderedDate(LocalDateTime.now());
			/*
			orderedDetail.setReservation(savedReservation);
			orderedDetail.setServiceDetail(this.serviceDetailRepository.findById(serviceDetailId)
					.orElseThrow(ServiceDetailNotFoundException::new));
			*/
			// persist...
			this.orderedDetailRepository.saveOrderedDetail(new OrderedDetailRequest(
					orderedDetail.getReservationId(), 
					orderedDetail.getServiceDetailId(), 
					orderedDetail.getOrderedDate()));
		}
		
		return ReservationMapper.toDto(savedReservation);
	}
	
	private CustomerDto retrieveCustomerByUsername(final String username) {
		return this.customerRepository
				.findByCredentialUsernameIgnoringCase(username)
				.map(CustomerMapper::toDto)
				.orElseThrow(() -> new CustomerNotFoundException(
						"Customer with username: %s not found".formatted(username)));
	}
	
	private static boolean isReservationStartDateValid(final ReservationRequest reservationRequest) {
		return reservationRequest.startDate()
					   .isAfter(LocalDateTime.now().plusMinutes(AppConstants.VALID_START_DATE_AFTER_MINUTES))
			   && reservationRequest.startDate().getMinute() == 0 || reservationRequest.startDate().getMinute() == 30;
	}
	
}




