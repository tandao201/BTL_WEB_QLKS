package hotel.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import hotel.common.APIResponse;
import hotel.common.PaginationMeta;
import hotel.model.RoomBooked;
import hotel.model.UserDto;
import hotel.request.GetCurrentUserRequest;
import hotel.request.LoginRequestDto;
import hotel.request.SignUpRequestDto;

@Controller
@RequestMapping("/revervation")
public class ReservationController {

	RestTemplate rest = new RestTemplate();

	List<Object> rooms;
	Map<String, Integer> paginationMeta;

	@GetMapping
	public String home(Model model, @CookieValue(value = "user", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		if (curUser == null) {
			model.addAttribute("signupDto", new SignUpRequestDto());
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "customer/login";
		} else {
			UserDto user = new UserDto();
			user.setEmail(curUser.get("email"));
			user.setName(curUser.get("name"));
			user.setAvatar(curUser.get("avatar"));
			user.setPhone(curUser.get("phone"));
			model.addAttribute("user", user);
		}
		getRoomPagination(curUser.get("username"));
		List<RoomBooked> dtoSugs = new ArrayList<>();
		PaginationMeta page = setPage();
		model.addAttribute("pagination", page);
		model.addAttribute("rooms", rooms);

		return "customer/room-booked";
	}

	@GetMapping("/{page}")
	public String getPage(@PathVariable(value = "page") int page, Model model,
			@CookieValue(value = "user", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		if (curUser == null) {
			model.addAttribute("signupDto", new SignUpRequestDto());
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "customer/login";

		} else {
			UserDto user = new UserDto();
			user.setEmail(curUser.get("email"));
			user.setName(curUser.get("name"));
			user.setAvatar(curUser.get("avatar"));
			user.setPhone(curUser.get("phone"));
			model.addAttribute("user", user);
		}
		String url = "http://localhost:8081/api/room-booking/roomPage/" + curUser.get("username") + "?page-number="
				+ page + "&page-size=7";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		rooms = (List<Object>) data.get("rooms");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");
		List<RoomBooked> dtoSugs = new ArrayList<>();
		PaginationMeta pagination = setPage();
		model.addAttribute("pagination", pagination);
		model.addAttribute("rooms", rooms);
		return "customer/room-booked";
	}

	@GetMapping("/delete/{id}")
	public String deleteCategories(Model model, @PathVariable String id,
			@CookieValue(value = "user", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		String url = "http://localhost:8081/api/room-booking/" + id;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		rest.delete(url, params);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		return "redirect:/revervation";
	}

	@SuppressWarnings("unchecked")
	public void getRoomPagination(String username) {
		String url = "http://localhost:8081/api/room-booking/roomPage/" + username + "?page-size=7";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		rooms = (List<Object>) data.get("rooms");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");
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

	private PaginationMeta setPage() {
		PaginationMeta page = new PaginationMeta();
		page.setTotalCount(paginationMeta.get("totalCount"));
		page.setPageSize(paginationMeta.get("pageSize"));
		page.setTotalPage(paginationMeta.get("totalPage"));
		page.setPageNumber(paginationMeta.get("pageNumber"));
		page.setIsFirst(paginationMeta.get("isFirst"));
		page.setIsLast(paginationMeta.get("isLast"));
		return page;
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
