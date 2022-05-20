package hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hotel.common.APIResponse;
import hotel.model.dto.SignUpRequestDto;
import hotel.service.LoginServiceImpl;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/signup")
public class RegisterController {
	@Autowired
	private LoginServiceImpl loginServiceImpl;

	@PostMapping
	public APIResponse register(@RequestBody SignUpRequestDto dto) {
		return loginServiceImpl.register(dto);
	}

	@GetMapping(path = "confirm")
	public String confirm(@RequestParam("token") String token) {
		return loginServiceImpl.confirmToken(token);
	}
}
