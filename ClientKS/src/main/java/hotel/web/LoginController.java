package hotel.web;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import hotel.common.APIResponse;
import hotel.model.GetCurrentUserRequest;
import hotel.model.LoginRequestDto;
import hotel.model.LogoutRequestDto;
import hotel.model.SignUpRequestDto;
import hotel.model.UpdateRequestDto;
import hotel.model.UserDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

	private RestTemplate rest = new RestTemplate();

	@GetMapping()
	public String showLoginForm(Model model, @CookieValue(value = "user", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		if (curUser == null) {
			model.addAttribute("signupDto", new SignUpRequestDto());
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "customer/login";
		} else {
			UserDto user = setCurUser(curUser);
			model.addAttribute("user", user);

			return "customer/account";
		}

	}

	@PostMapping()
	public String login(LoginRequestDto loginRequestDto, Model model, HttpServletResponse response)
			throws JsonMappingException, JsonProcessingException {

		loginRequestDto.setRole("USER");
		String url = "http://localhost:8081/login";
		APIResponse apiResponse = rest.postForObject(url, loginRequestDto, APIResponse.class);
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> curUser = (LinkedHashMap<String, String>) apiResponse.getData();

		Integer status = apiResponse.getStatus();
		log.info(curUser.get("email"));

//			log.info(loginRequestDto.getUsername());
//			log.info(loginRequestDto.getPassword());
		if (status != 200) {
			model.addAttribute("signupDto", new SignUpRequestDto());
			model.addAttribute("loginRequest", new LoginRequestDto());
			model.addAttribute("error", curUser.get("error"));
			return "customer/login";
		}
		Cookie cookie = new Cookie("user", curUser.get("username"));
		cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
		response.addCookie(cookie);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);

		return "customer/account";
	}

	@GetMapping("/account")
	public String dashboard(Model model, @CookieValue(value = "user", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		if (curUser == null) {
			model.addAttribute("signupDto", new SignUpRequestDto());
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "customer/login";
		} else {
			UserDto user = setCurUser(curUser);
			model.addAttribute("user", user);

			return "customer/account";
		}

	}

	@PostMapping("/logout")
	public String logout(LogoutRequestDto dto, Model model,
			@CookieValue(value = "user", defaultValue = "no user") String username) {
		if (username == null) {
			return "customer/index";
		} else {

			dto.setUsername(username);
			String url = "http://localhost:8081/login/logout";
			APIResponse apiResponseLogout = rest.postForObject(url, dto, APIResponse.class);
			String data = apiResponseLogout.getData().toString();
			if (data != null) {
				model.addAttribute("signupDto", new SignUpRequestDto());
				model.addAttribute("loginRequest", new LoginRequestDto());
				return "customer/login";
			}
			return "customer/index";
		}

	}

	@PostMapping("/update")
	public String updateCurUser(@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam("avatar") String avatar,
			@RequestParam("name") String name, Model model,
			@CookieValue(value = "user", defaultValue = "no user") String usernameCookie) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		UpdateRequestDto updateRequestDto = new UpdateRequestDto(username, email, phone, avatar, name,
				curUser.get("role"));
		String url = "http://localhost:8081/users";
		Map<String, String> param = new HashMap<>();
		param.put("username", username);
		param.put("email", email);
		param.put("phone", phone);
		param.put("avatar", avatar);
		param.put("name", name);
		rest.put(url, updateRequestDto, param);
		UserDto user = new UserDto();
		user.setEmail(email);
		user.setAvatar(avatar);
		user.setPhone(phone);
		user.setUsername(username);
		user.setName(name);
		model.addAttribute("user", user);
		return "customer/account";
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, String> haveCurrentUser(String username) {
		String urlCurUser = "http://localhost:8081/login/currentUser";
		GetCurrentUserRequest request = new GetCurrentUserRequest("USER", username);
		APIResponse apiResponse = rest.postForObject(urlCurUser, request, APIResponse.class);
		LinkedHashMap<String, String> curUser = (LinkedHashMap<String, String>) apiResponse.getData();
		if (curUser.get("error") != null) {
			return null;
		} else {
			return curUser;
		}
	}

	private UserDto setCurUser(LinkedHashMap<String, String> curUser) {
		UserDto user = new UserDto();
		user.setEmail(curUser.get("email"));
		user.setName(curUser.get("name"));
		user.setAvatar(curUser.get("avatar"));
		user.setPhone(curUser.get("phone"));
		user.setUsername(curUser.get("username"));
		return user;
	}

	@PostMapping("/signup")
	public String signUp(SignUpRequestDto signUpRequestDto, Model model) {
		String url = "http://localhost:8081/signup";
		APIResponse apiResponse = rest.postForObject(url, signUpRequestDto, APIResponse.class);
		LinkedHashMap<String, String> data = (LinkedHashMap<String, String>) apiResponse.getData();
		if (data.get("error") != null) {
			model.addAttribute("error", data.get("error"));
		} else {
			return "customer/checkEmail";
		}
		model.addAttribute("signupDto", new SignUpRequestDto());
		model.addAttribute("loginRequest", new LoginRequestDto());
		return "customer/login";
	}
}
