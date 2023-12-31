package io.cita.app.repository;

import io.cita.app.model.domain.entity.Favourite;
import io.cita.app.model.domain.id.FavouriteId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {
	
	Optional<Favourite> findByIdentifier(final String identifier);
	Page<Favourite> findAllByCustomerId(final Integer customerId, final Pageable pageable);
	
	@Modifying
	@Query(name = "int.saveFavourite", nativeQuery = true)
	int saveFavourite(@Param("favourite") final Favourite favourite);
	
}



