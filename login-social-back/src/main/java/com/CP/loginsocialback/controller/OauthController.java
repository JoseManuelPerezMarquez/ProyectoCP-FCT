package com.CP.loginsocialback.controller;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CP.loginsocialback.dto.TokenDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@RestController
@RequestMapping("/oauth")
@CrossOrigin
public class OauthController {

	@Value("${google.clientId}")
	String googleClientId;
	
	@PostMapping("/google")
	public ResponseEntity<?> google(@RequestBody TokenDto tokenDto) throws IOException{
		final NetHttpTransport transport =new NetHttpTransport();
		final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
		GoogleIdTokenVerifier.Builder verifier =
				new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
				.setAudience(Collections.singletonList(googleClientId));
		final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(),tokenDto.getValue());
		final GoogleIdToken.Payload payload = googleIdToken.getPayload();
		return new  ResponseEntity(payload, HttpStatus.OK);
	}
	
	@PostMapping("/facebook")
	public ResponseEntity<?> facebook(@RequestBody TokenDto tokenDto) throws IOException{
		Facebook facebook = new FacebookTemplate(tokenDto.getValue());
		//final String [] fields = {"email", "picture"};
		User user = facebook.fetchObject("me",User.class/*fields*/);
		return new ResponseEntity(user, HttpStatus.OK);
	}
}
