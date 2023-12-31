package io.cita.app.business.auth;

import io.cita.app.mapper.CredentialMapper;
import io.cita.app.model.dto.CredentialDto;
import io.cita.app.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.cita.app.exception.wrapper.CredentialNotFoundException;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {
	
	private final CredentialRepository credentialRepository;
	
	@Override
	public CredentialDto findById(final Integer id) {
		log.info("** Find user by id .. *");
		return this.credentialRepository.findById(id)
				.map(CredentialMapper::toDto)
				.orElseThrow(CredentialNotFoundException::new);
	}
	
	@Override
	public CredentialDto findByIdentifier(final String identifier) {
		return this.credentialRepository.findByIdentifier(identifier.strip())
				.map(CredentialMapper::toDto)
				.orElseThrow(CredentialNotFoundException::new);
	}
	
	@Override
	public CredentialDto findByUsername(final String username) {
		log.info("** Find user by username.. *");
		return this.credentialRepository.findByUsernameIgnoreCase(username)
				.map(CredentialMapper::toDto)
				.orElseThrow(() -> new CredentialNotFoundException(String
						.format("Credential with username %s not found", username)));
	}
	
}









