package io.cita.app.resource.v1;

import io.cita.app.model.dto.EmployeeDto;
import io.cita.app.model.dto.response.ManagerWorkerInfoResponse;
import io.cita.app.model.dto.response.api.ApiResponse;
import io.cita.app.service.ManagerWorkerInfoService;
import io.cita.app.util.UserRequestExtractorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("${app.api-version}" + "/employees/managers/workers")
@Slf4j
@RequiredArgsConstructor
public class ManagerWorkerInfoResource {
	
	@Qualifier("managerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final ManagerWorkerInfoService managerWorkerInfoService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<ManagerWorkerInfoResponse>> fetchAllSubWorkers(final WebRequest webRequest) {
		log.info("** Fetch all sub workers by manager.. *");
		return ResponseEntity.ok(new ApiResponse<>(1, HttpStatus.OK, true,
				this.managerWorkerInfoService.fetchAllSubWorkers(this.userRequestExtractorUtil.extractUsername(webRequest))));
	}
	
	@GetMapping("/{workerId}")
	public ResponseEntity<ApiResponse<EmployeeDto>> fetchWorkerInfo(final WebRequest webRequest,
                                                                    @PathVariable final String workerId) {
		log.info("** Fetch worker info by manager.. *");
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ResponseEntity.ok(new ApiResponse<>(1, HttpStatus.OK, true, 
				this.managerWorkerInfoService.fetchWorkerInfo(Integer.parseInt(workerId))));
	}
	
}




