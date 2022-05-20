package hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hotel.model.dto.LoginRequestDto;
import hotel.model.dto.LogoutRequestDto;
import hotel.service.LoginServiceImpl;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/login", produces = "application/json")
public class LoginController {
	@Autowired
	private LoginServiceImpl loginServiceImpl;

	@PostMapping
	public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
		return ResponseEntity.ok(loginServiceImpl.login(dto));
	}

	@PostMapping("/currentUser")
	public ResponseEntity<?> getCurrentUser(@RequestBody String role) {
		return ResponseEntity.ok(loginServiceImpl.getCurrenUser(role));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody LogoutRequestDto dto) {
		return ResponseEntity.ok(loginServiceImpl.logout(dto));
	}
}
