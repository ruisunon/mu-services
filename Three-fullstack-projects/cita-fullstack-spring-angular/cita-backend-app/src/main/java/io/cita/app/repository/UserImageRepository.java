package io.cita.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.cita.app.model.domain.entity.UserImage;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Integer> {
	
	Optional<UserImage> findByIdentifier(final String identifier);
	
}


