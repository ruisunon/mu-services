package io.cita.app.business.favourite.customer.service.impl;

import io.cita.app.business.favourite.customer.model.CustomerFavouriteResponse;
import io.cita.app.business.favourite.customer.service.CustomerFavouriteService;
import io.cita.app.mapper.CustomerMapper;
import io.cita.app.mapper.FavouriteMapper;
import io.cita.app.model.domain.entity.Favourite;
import io.cita.app.model.domain.id.FavouriteId;
import io.cita.app.model.dto.CustomerDto;
import io.cita.app.model.dto.FavouriteDto;
import io.cita.app.model.dto.request.ClientPageRequest;
import io.cita.app.util.ClientPageRequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.cita.app.exception.wrapper.CustomerNotFoundException;
import io.cita.app.exception.wrapper.FavouriteAlreadyExistsException;
import io.cita.app.exception.wrapper.FavouriteNotFoundException;
import io.cita.app.repository.CustomerRepository;
import io.cita.app.repository.FavouriteRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class CustomerFavouriteServiceImpl implements CustomerFavouriteService {
	
	private final CustomerRepository customerRepository;
	private final FavouriteRepository favouriteRepository;
	// private final SaloonRepository saloonRepository;
	
	@Override
	public CustomerFavouriteResponse fetchAllFavourites(final String username, final ClientPageRequest clientPageRequest) {
		log.info("** Fetch all favourites by customer.. *");
		final var customerDto = this.retrieveCustomerByUsername(username);
		return new CustomerFavouriteResponse(
				customerDto,
				this.favouriteRepository.findAllByCustomerId(customerDto.getId(), 
						ClientPageRequestUtils.from(clientPageRequest))
					.map(FavouriteMapper::toDto));
	}
	
	@Transactional
	@Override
	public Boolean deleteFavourite(final String username, final Integer saloonId) {
		log.info("** Delete favourite by customer.. *");
		final var favouriteId = new FavouriteId(this.retrieveCustomerByUsername(username).getId(), saloonId);
		this.favouriteRepository.deleteById(favouriteId);
		return !this.favouriteRepository.existsById(favouriteId);
	}
	
	@Transactional
	@Override
	public FavouriteDto addFavourite(final String username, final Integer saloonId) {
		log.info("** Add new favourite by customer.. *");
		
		final var customer = this.customerRepository.findByCredentialUsernameIgnoringCase(username)
				.orElseThrow(() -> new CustomerNotFoundException("Customer with username %s not found".formatted(username)));
		
		// Check if this favourite already exists..
		final var favouriteId = new FavouriteId(customer.getId(), saloonId);
		this.favouriteRepository
				.findById(favouriteId).ifPresent(f -> {
			throw new FavouriteAlreadyExistsException("This is already part of your favourites");
		});
		
		// persist..
		this.favouriteRepository.saveFavourite(Favourite.builder()
					.customerId(favouriteId.getCustomerId())
					.saloonId(favouriteId.getSaloonId())
					.favouriteDate(LocalDateTime.now())
					.identifier(UUID.randomUUID().toString())
					/*
					.customer(customer)
					.saloon(this.saloonRepository.findById(favouriteId.getSaloonId())
					 		.orElseThrow(SaloonNotFoundException::new))
					 */
					.build());
		
		return this.favouriteRepository.findById(favouriteId)
				.map(FavouriteMapper::toDto)
				.orElseThrow(FavouriteNotFoundException::new);
	}
	
	private CustomerDto retrieveCustomerByUsername(final String username) {
		return this.customerRepository
				.findByCredentialUsernameIgnoringCase(username)
				.map(CustomerMapper::toDto)
				.orElseThrow(() -> new CustomerNotFoundException(
						"Customer with username: %s not found".formatted(username)));
	}
	
}




