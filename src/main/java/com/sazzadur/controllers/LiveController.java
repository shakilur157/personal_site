package com.sazzadur.controllers;

import com.sazzadur.models.UserPosts;
import com.sazzadur.payload.response.commonResponse.CommonErrorResponse;
import com.sazzadur.payload.response.commonResponse.CommonResponse;
import com.sazzadur.repositories.PostRepository;
import com.sazzadur.repositories.RoleRepository;
import com.sazzadur.repositories.UserRepository;
import com.sazzadur.security.jwt.JwtUtils;
import com.sazzadur.utils.CommonUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class LiveController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PostRepository postRepository;

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public void handleMissingParams(MissingServletRequestParameterException ex) {
		String name = ex.getParameterName();
		System.out.println(name + " parameter is missing");
	}

	@PreAuthorize("hasAuthority('USER')")
	@RequestMapping(value = "/user/data", method = RequestMethod.POST)
	public ResponseEntity<?> postUserData(@RequestBody UserPosts posts){
		try {
			System.out.println(posts);
			postRepository.save(posts);
			return new ResponseEntity<CommonResponse>(new CommonResponse(200, "success", "upload success"), HttpStatus.OK);
		}catch (Exception exception){
			return  new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.BAD_REQUEST.value(), "Something is missing, Please check again"), HttpStatus.BAD_REQUEST);
		}
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/pending/requests", method = RequestMethod.GET)
	public ResponseEntity<?> getAllPendingPosts(){
		try {
			Optional<UserPosts> posts = postRepository.findUserPostsByStatus("PENDING");
			return new ResponseEntity<CommonResponse>(new CommonResponse(200, "success", posts.get()), HttpStatus.OK);
		}catch (Exception exception){
			return  new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.BAD_REQUEST.value(), "Something is missing, Please check again"), HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/all/posts", method = RequestMethod.GET)
	public ResponseEntity<?> getAllPosts(){
		try {
			Iterable<UserPosts> posts = postRepository.findAll();
			List<UserPosts> users = new ArrayList<>();
			posts.forEach(userPosts -> users.add(userPosts));
			return new ResponseEntity<CommonResponse>(new CommonResponse(200, "success", users), HttpStatus.OK);
		}catch (Exception exception){
			return  new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.BAD_REQUEST.value(), "Something is missing, Please check again"), HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/accepted/requests", method = RequestMethod.GET)
	public ResponseEntity<?> getAllAcceptedPosts(){
		try {
			List<Optional<UserPosts>> posts = postRepository.findAllByStatus("ACCEPTED");
			if(posts.isEmpty()){
				return new ResponseEntity<CommonResponse>(new CommonResponse(200, "success", "No records found"), HttpStatus.OK);
			}else{
				return new ResponseEntity<CommonResponse>(new CommonResponse(200, "success", posts), HttpStatus.OK);
			}
		}catch (Exception exception){
			return  new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.BAD_REQUEST.value(), "Something is missing, Please check again"), HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/accept/request/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> acceptPendingRequest(@PathVariable Long id){
		try {
			Optional<UserPosts> posts = postRepository.findById(id);
			UserPosts tempPost = posts.get();
			tempPost.setStatus("ACCEPTED");
			postRepository.save(tempPost);
			return new ResponseEntity<CommonResponse>(new CommonResponse(200, "success", posts.get()), HttpStatus.OK);
		}catch (Exception exception){
			return  new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.BAD_REQUEST.value(), "Something is missing, Please check again"), HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/delete/post/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePost(@PathVariable Long id){
		try {
			postRepository.deleteById(id);
			return new ResponseEntity<CommonResponse>(new CommonResponse(200, "success", "post deleted successfully"), HttpStatus.OK);
		}catch (Exception exception){
			return  new ResponseEntity<CommonErrorResponse>(new CommonErrorResponse(HttpStatus.BAD_REQUEST.value(), "Something is missing, Please check again"), HttpStatus.BAD_REQUEST);
		}
	}



}
