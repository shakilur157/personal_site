package com.sazzadur.payload.request;


import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginRequest {
	@NotNull
	private String username;

	@NotNull
	private String password;

	@NotNull
	private String userType;
}
