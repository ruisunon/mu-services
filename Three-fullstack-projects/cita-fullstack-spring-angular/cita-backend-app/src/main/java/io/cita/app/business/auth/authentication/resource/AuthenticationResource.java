package io.cita.app.business.auth.authentication.resource;

import io.cita.app.business.auth.authentication.model.LoginRequest;
import io.cita.app.business.auth.authentication.model.LoginResponse;
import io.cita.app.business.auth.authentication.service.AuthenticationService;
import io.cita.app.model.dto.response.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api-version}" + "/authenticate")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationResource {
	
	private final AuthenticationService authenticationService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<LoginResponse>> authenticate(@RequestBody @Valid final LoginRequest loginRequest) {
		log.info("** Authenticate user...*");
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponse<>(1, HttpStatus.OK, true, 
						this.authenticationService.authenticate(loginRequest)));
	}
	
}




