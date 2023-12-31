package io.cita.app.business.rating.customer.resource;

import io.cita.app.business.rating.customer.model.CustomerRatingResponse;
import io.cita.app.model.dto.response.api.ApiResponse;
import io.cita.app.util.UserRequestExtractorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import io.cita.app.business.rating.customer.service.CustomerRatingService;

@RestController
@RequestMapping("${app.api-version}" + "/customers/ratings")
@Slf4j
@RequiredArgsConstructor
public class CustomerRatingResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerRatingService customerRatingService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<CustomerRatingResponse>> fetchAllRatings(final WebRequest request) {
		log.info("** Fetch all customer ratings.. *\n");
		return ResponseEntity.ok(new ApiResponse<>(1, HttpStatus.OK, true, 
				this.customerRatingService.fetchAllRatings(this.userRequestExtractorUtil.extractUsername(request))));
	}
	
}



