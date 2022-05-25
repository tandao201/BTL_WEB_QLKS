package hotel.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import hotel.common.APIResponse;
import hotel.model.RoomBooked;
import hotel.model.Statistic;
import hotel.model.UserDto;
import hotel.request.GetCurrentUserRequest;
import hotel.request.LoginRequestDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/statistic")
public class AdminStatisticController {
	RestTemplate rest = new RestTemplate();
	List<Object> users;
	Map<String, Integer> paginationMeta;

	String success = "";
	String error = "";

	@GetMapping
	public String showForm(Model model, @CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);

		if (curUser == null) {
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "admin/login";
		} else {
			UserDto user = setCurUser(curUser);
			model.addAttribute("user", user);
		}

		if (!success.isEmpty()) {
			model.addAttribute("success", success);
		}
		if (!error.isEmpty()) {
			model.addAttribute("error", error);
		}
		success = "";
		error = "";
		String url = "http://localhost:8081/api/statistic";
		List<Long> turnover = getListTurnover();
		Statistic statistic = rest.getForObject(url, Statistic.class);

		model.addAttribute("statistic", statistic);
		model.addAttribute("turnover", turnover);
		return "admin/statistic";
	}

	private List<Long> getListTurnover() {
		String url = "http://localhost:8081/api/room-booking";
		List<RoomBooked> roomBookings = Arrays.asList(rest.getForObject(url, RoomBooked[].class));

		Map<String, Long> turnoverMap = new HashMap<>();
		for (int i = 1; i <= 12; i++)
			turnoverMap.put(i + "", Long.valueOf("0"));
		for (RoomBooked room : roomBookings) {
			String month = String.valueOf(room.getCheckInAt().getMonthValue());
			Long value = turnoverMap.get(month);
			value += room.getPrice();
			turnoverMap.put(month, value);
		}
		List<Long> rs = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			rs.add(turnoverMap.get(i + ""));
		}
		return rs;

	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, String> haveCurrentUser(String username) {
		String urlCurUser = "http://localhost:8081/login/currentUser";
		GetCurrentUserRequest request = new GetCurrentUserRequest("ADMIN", username);
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
}
