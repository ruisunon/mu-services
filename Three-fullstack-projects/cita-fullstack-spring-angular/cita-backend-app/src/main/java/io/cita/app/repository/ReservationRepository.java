package io.cita.app.repository;

import io.cita.app.model.domain.ReservationStatus;
import io.cita.app.model.domain.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	
	Optional<Reservation> findByIdentifier(final String identifier);
	Page<Reservation> findAllByCustomerId(final Integer customerId, final Pageable pageable);
	Optional<Reservation> findByCode(final String code);
	Page<Reservation> findAllBySaloonId(final Integer saloonId, final Pageable pageable);
	List<Reservation> findAllBySaloonId(final Integer saloonId);
	Optional<Reservation> findByStartDateAndStatus(final LocalDateTime startDate, final ReservationStatus status);
	
	@Query(name = "List<Reservation>.searchAllByCustomerIdLikeKey")
	List<Reservation> searchAllByCustomerIdLikeKey(@Param("customerId") final Integer customerId, @Param("key") final String key);
	
	@Query(name = "List<Reservation>.searchAllBySaloonIdLikeKey")
	List<Reservation> searchAllBySaloonIdLikeKey(@Param("saloonId") final Integer saloonId, @Param("key") final String key);
	
	List<Reservation> findAllByStatusAndStartDateBetween(final ReservationStatus status, 
			final LocalDateTime from, final LocalDateTime to);
	
}



