package io.cita.app.business.auth;

import io.cita.app.model.dto.CredentialDto;
import io.cita.app.model.dto.response.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api-version}" + "/credentials")
@Slf4j
@RequiredArgsConstructor
public class CredentialResource {
	
	private final CredentialService credentialService;
	
	@GetMapping("/identifier/{identifier}")
	public ResponseEntity<ApiResponse<CredentialDto>> findByIdentifier(@PathVariable final String identifier) {
		log.info("** Find by identifier.. *");
		return ResponseEntity.ok(new ApiResponse<>(1, HttpStatus.OK, true, 
				this.credentialService.findByIdentifier(identifier.strip())));
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<ApiResponse<CredentialDto>> findByUsername(@PathVariable final String username) {
		log.info("** Find by username.. *");
		return ResponseEntity.ok(new ApiResponse<>(1, HttpStatus.OK, true, 
				this.credentialService.findByUsername(username)));
	}
	
}







