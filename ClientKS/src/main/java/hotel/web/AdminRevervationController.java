package hotel.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import hotel.common.APIResponse;
import hotel.common.PaginationMeta;
import hotel.model.LoginRequestDto;
import hotel.model.RoomBooked;
import hotel.model.UserDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/revervation")
public class AdminRevervationController {

	private RestTemplate rest = new RestTemplate();

	List<Object> rooms;
	Map<String, Integer> paginationMeta;
	LinkedHashMap<String, String> curUser = haveCurrentUser();

	@GetMapping
	public String home(Model model) {
		curUser = haveCurrentUser();
		if (curUser == null) {
			model.addAttribute("user", null);
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "admin/login";
		} else {
			UserDto user = new UserDto();
			user.setEmail(curUser.get("email"));
			user.setName(curUser.get("name"));
			user.setAvatar(curUser.get("avatar"));
			user.setPhone(curUser.get("phone"));
			model.addAttribute("user", user);
		}
		getRoomPagination("");
		List<RoomBooked> dtoSugs = new ArrayList<>();
		PaginationMeta page = setPage();
		model.addAttribute("pagination", page);
		model.addAttribute("rooms", rooms);

		return "admin/room-booked";
	}

	@GetMapping("/{page}")
	public String getPage(@PathVariable(value = "page") int page, Model model) {
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
		String url = "http://localhost:8081/api/room-booking/roomPageAdmin?page-number=" + page + "&page-size=5";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		rooms = (List<Object>) data.get("rooms");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");
		List<RoomBooked> dtoSugs = new ArrayList<>();
		PaginationMeta pagination = setPage();
		model.addAttribute("pagination", pagination);
		model.addAttribute("rooms", rooms);
		return "admin/room-booked";
	}

	@GetMapping("/delete/{id}")
	public String deleteCategories(Model model, @PathVariable String id) {
		String url = "http://localhost:8081/api/room-booking/" + id;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		rest.delete(url, params);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		return "redirect:/revervation";
	}

	@GetMapping("/update/{id}")
	public String updateRoomBooked(Model model, @PathVariable String id) {
		String url = "http://localhost:8081/api/room-booking/payment";
		Map<String, String> param = new HashMap<>();
		param.put("id", id);
		rest.put(url, id, param);
		return "redirect:/admin/revervation";
	}

	@SuppressWarnings("unchecked")
	public void getRoomPagination(String username) {
		String url = "http://localhost:8081/api/room-booking/roomPageAdmin?page-size=5";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		rooms = (List<Object>) data.get("rooms");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");
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
