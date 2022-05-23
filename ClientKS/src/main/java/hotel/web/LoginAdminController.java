package hotel.web;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import hotel.common.APIResponse;
import hotel.model.LoginRequestDto;
import hotel.model.LogoutRequestDto;
import hotel.model.UpdateRequestDto;
import hotel.model.UserDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/login")
public class LoginAdminController {

	private RestTemplate rest = new RestTemplate();
	LinkedHashMap<String, String> curUser = haveCurrentUser();

	@GetMapping()
	public String showLoginForm(Model model) {
		curUser = haveCurrentUser();
		if (curUser == null) {
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "admin/login";
		} else {
			UserDto user = setCurUser(curUser);
			model.addAttribute("user", user);

			return "admin/adminSite";
		}

	}

	@PostMapping()
	public String login(LoginRequestDto loginRequestDto, Model model)
			throws JsonMappingException, JsonProcessingException {

		loginRequestDto.setRole("ADMIN");
		String url = "http://localhost:8081/login";
		APIResponse apiResponse = rest.postForObject(url, loginRequestDto, APIResponse.class);
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> curUser = (LinkedHashMap<String, String>) apiResponse.getData();

		Integer status = apiResponse.getStatus();
		log.info(curUser.get("email"));

//			log.info(loginRequestDto.getUsername());
//			log.info(loginRequestDto.getPassword());
		if (status != 200) {
			model.addAttribute("loginRequest", new LoginRequestDto());
			model.addAttribute("error", curUser.get("error"));
			return "admin/login";
		}
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);

		return "admin/adminSite";
	}

	@GetMapping("/account")
	public String dashboard(Model model) {
		LinkedHashMap<String, String> curUser = haveCurrentUser();
		if (curUser == null) {
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "admin/login";
		} else {
			UserDto user = setCurUser(curUser);
			model.addAttribute("user", user);

			return "admin/adminSite";
		}

	}

	@PostMapping("/logout")
	public String logout(LogoutRequestDto dto, Model model) {
		LinkedHashMap<String, String> curUser = haveCurrentUser();
		if (curUser == null) {
			return "customer/index";
		} else {
			String urlCurUser = "http://localhost:8081/login/currentUser";
			APIResponse apiResponse = rest.postForObject(urlCurUser, "ADMIN", APIResponse.class);
			dto.setUsername(curUser.get("username"));
			String url = "http://localhost:8081/login/logout";
			APIResponse apiResponseLogout = rest.postForObject(url, dto, APIResponse.class);
			String data = apiResponseLogout.getData().toString();
			if (data != null) {
				model.addAttribute("loginRequest", new LoginRequestDto());
				return "admin/login";
			}
			return "customer/index";
		}

	}

	@PostMapping("/update")
	public String updateCurUser(@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam("avatar") String avatar,
			@RequestParam("name") String name, Model model) {
		curUser = haveCurrentUser();
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
		return "admin/adminSite";
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, String> haveCurrentUser() {
		String urlCurUser = "http://localhost:8081/login/currentUser";
		APIResponse apiResponse = rest.postForObject(urlCurUser, "ADMIN", APIResponse.class);
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
}
