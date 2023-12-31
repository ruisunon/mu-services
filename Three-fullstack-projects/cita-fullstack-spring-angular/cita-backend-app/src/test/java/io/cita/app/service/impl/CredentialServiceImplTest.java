package io.cita.app.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import io.cita.app.business.auth.CredentialService;
import io.cita.app.exception.wrapper.CredentialNotFoundException;
import io.cita.app.model.domain.UserRoleBasedAuthority;
import io.cita.app.model.domain.entity.Credential;
import io.cita.app.model.domain.entity.Employee;
import io.cita.app.repository.CredentialRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CredentialServiceImplTest {
	
	@MockBean
	private CredentialRepository credentialRepository;
	
	@Autowired
	private CredentialService credentialService;
	private Credential credential;
	
	@BeforeEach
	void setUp() throws Exception {
		
		final var username = "selimhorri";
		this.credential = Credential.builder()
				.id(null)
				.username(username)
				.userRoleBasedAuthority(UserRoleBasedAuthority.WORKER)
				.isEnabled(true)
				.employee(
						Employee.builder()
							.firstname("Selim")
							.lastname("Horri")
							.build())
				.build();
		
		when(this.credentialRepository.findByUsernameIgnoreCase(username))
				.thenReturn(Optional.of(this.credential));
	}
	
	@Test
	void givenValidUsername_whenFindCredentialByUsername_thenCredentialShouldBeFound() {
		
		final var credentialDtoExpected = this.credentialService.findByUsername(this.credential.getUsername());
		
		assertThat(credentialDtoExpected).isNotNull();
		assertThat(credentialDtoExpected.getUsername()).isEqualTo(this.credential.getUsername());
		assertThat(credentialDtoExpected.getIsEnabled()).isTrue();
	}
	
	@Test
	void givenInvalidUsername_whenFindCredentialByUsername_thenCredentialNotFoundExceptionShouldBeThrown() {
		
		final var newUsername = this.credential.getUsername() + "iii";
		final var credentialNotFoundException = assertThrows(CredentialNotFoundException.class, 
				() -> this.credentialService.findByUsername(newUsername));
		
		assertThat(credentialNotFoundException).isNotNull();
		assertThat(credentialNotFoundException.getMessage())
				.startsWith("Credential ")
				.endsWith(" not found")
				.isEqualTo(String.format("Credential with username %s not found", newUsername));
	}
	
}





