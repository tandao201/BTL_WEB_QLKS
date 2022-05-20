package hotel.web;

import java.util.LinkedHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import hotel.common.APIResponse;
import hotel.model.LoginRequestDto;
import hotel.model.PasswordChangeDto;
import hotel.model.SignUpRequestDto;
import hotel.model.UserDto;

@Controller
@RequestMapping("/password")
public class PasswordChangeController {

	private RestTemplate rest = new RestTemplate();

	@GetMapping()
	public String showFormPasswordChange(Model model) {
		LinkedHashMap<String, String> curUser = haveCurrentUser();
		if (curUser == null) {
			model.addAttribute("signupDto", new SignUpRequestDto());
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "customer/login";
		} else {
			UserDto user = setCurUser(curUser);
			model.addAttribute("user", user);
		}

		return "customer/passwordChange";
	}

	@PostMapping
	public String passwordChange(@RequestParam("passwordCur") String passwordCur,
			@RequestParam("passwordNew") String passwordNew, @RequestParam("passwordNewRe") String passwordNewRe,
			@RequestParam("username") String username, Model model) {

		PasswordChangeDto passwordChangeDto = new PasswordChangeDto(passwordCur, passwordNew, username);
		String url = "http://localhost:8081/users/password";
		APIResponse apiResponse = rest.postForObject(url, passwordChangeDto, APIResponse.class);
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> data = (LinkedHashMap<String, String>) apiResponse.getData();
		if (data.get("error") != null) {
			UserDto user = new UserDto();
			user.setAvatar(data.get("avatar"));
			user.setName(data.get("name"));
			model.addAttribute("user", user);
			model.addAttribute("error", data.get("error"));

		} else {
			UserDto user = new UserDto();
			user.setEmail(data.get("email"));
			user.setName(data.get("name"));
			user.setAvatar(data.get("avatar"));
			user.setPhone(data.get("phone"));
			user.setUsername(data.get("username"));
			model.addAttribute("user", user);
			model.addAttribute("success", "Đổi mật khẩu thành công!");
		}
		return "customer/account";
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, String> haveCurrentUser() {
		String urlCurUser = "http://localhost:8081/login/currentUser";
		APIResponse apiResponse = rest.postForObject(urlCurUser, "USER", APIResponse.class);
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
