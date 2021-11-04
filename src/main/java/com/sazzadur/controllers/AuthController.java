package com.sazzadur.controllers;

import com.sazzadur.models.User;

import com.sazzadur.models.UserRoles;
import com.sazzadur.payload.request.LoginRequest;
import com.sazzadur.payload.request.SignupRequest;
import com.sazzadur.payload.response.JwtResponse;
import com.sazzadur.payload.response.commonResponse.CommonErrorResponse;
import com.sazzadur.payload.response.commonResponse.CommonResponse;
import com.sazzadur.repositories.UserRepository;
import com.sazzadur.repositories.UserRolesRepositories;
import com.sazzadur.security.jwt.JwtUtils;
import com.sazzadur.security.services.UserDetailsImpl;
import com.sazzadur.enums.UserType;
import com.sazzadur.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRolesRepositories userRolesRepositories;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;


	CommonUtils commonUtils = new CommonUtils();

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority).collect(Collectors.toList());

			if(commonUtils.findValidUser(roles, loginRequest.getUserType())){
				return ResponseEntity.ok(
						new JwtResponse(HttpStatus.OK.value(), "success", jwt,
										userDetails.getId(),
										userDetails.getUsername(),
										roles));
			}else {
				return  new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.FORBIDDEN.value(), "Error: id or password is incorrect"), HttpStatus.FORBIDDEN);
			}
		}catch (Exception exception){
			return  new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.FORBIDDEN.value(), "Error: id or password is incorrect"), HttpStatus.FORBIDDEN);
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		if (signUpRequest.getPassword() == null) {
			return new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(),"Error: User name is empty"), HttpStatus.NOT_ACCEPTABLE);
		}
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.BAD_REQUEST.value(),"Error: User name is already taken"), HttpStatus.BAD_REQUEST);
		}
		if (signUpRequest.getPassword().length() < 4) {
			return new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.BAD_REQUEST.value(),"Error: Password should nto be less than 4 char"), HttpStatus.BAD_REQUEST);
		}
		User user;
		try{
			 user = new User(signUpRequest.getUsername(),
					encoder.encode(signUpRequest.getPassword()));
		}catch (Exception exception){
			return new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(400, exception.getMessage()), HttpStatus.BAD_REQUEST);
		}

		Set<String> strRoles = signUpRequest.getRole();
		Set<UserRoles> roles = new HashSet<>();

		if (strRoles == null || strRoles.size() == 0) {
			return new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(400, "Error: User role can not be null"), HttpStatus.BAD_REQUEST);
		} else {
			strRoles.forEach (role -> {
				if ("user".equals(role)) {
					UserRoles serviceCentre = userRolesRepositories.findByUserType(UserType.USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(serviceCentre);
				}else if("admin".equals(role)){
					UserRoles adminRole = userRolesRepositories.findByUserType(UserType.ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
				}
			});
		}
		if(roles.size() > 0){
			user.setRoles(roles);
			userRepository.save(user);
			return new ResponseEntity<CommonResponse>(new CommonResponse(200, "success", "sign up success"), HttpStatus.OK);
		}else{
			return new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(400, "Error: Not a valid user type"), HttpStatus.BAD_REQUEST);
		}
	}
}
