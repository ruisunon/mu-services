package io.cita.app.business.auth.authentication.service.impl;

import io.cita.app.business.auth.authentication.model.LoginRequest;
import io.cita.app.business.auth.authentication.model.LoginResponse;
import io.cita.app.business.auth.authentication.service.AuthenticationService;
import io.cita.app.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.cita.app.exception.wrapper.PasswordNotMatchException;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtUtils jwtUtils;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public LoginResponse authenticate(final LoginRequest loginRequest) {
		log.info("** Authenticate user.. *");
		
		final var userDetails = this.userDetailsService.loadUserByUsername(loginRequest.username());
		
		if (userDetails == null || !this.passwordEncoder.matches(loginRequest.password(), userDetails.getPassword()))
			throw new PasswordNotMatchException("Incorrect password");
		
		final var authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userDetails.getUsername(), loginRequest.password()));
		
		return new LoginResponse(authentication.getName(), this.jwtUtils.generateToken(userDetails));
	}
	
}



