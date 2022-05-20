package hotel.web;

import java.util.LinkedHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import hotel.common.APIResponse;
import hotel.model.UserDto;

@Controller
public class homeCTL {
	RestTemplate rest = new RestTemplate();

	@GetMapping("/")
	public String home(Model model) {
		LinkedHashMap<String, String> curUser = haveCurrentUser();
		if (curUser == null) {
			model.addAttribute("user", null);

		} else {
			UserDto user = new UserDto();
			user.setEmail(curUser.get("email"));
			user.setName(curUser.get("name"));
			user.setAvatar(curUser.get("avatar"));
			user.setPhone(curUser.get("phone"));
			model.addAttribute("user", user);
		}
		return "customer/index";
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
}
