package hotel.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import hotel.common.APIResponse;
import hotel.model.LoginRequestDto;
import hotel.model.Room;
import hotel.model.RoomCategories;
import hotel.model.UserDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/room-manage")
public class AdminRoomManageController {

	private RestTemplate rest = new RestTemplate();
	LinkedHashMap<String, String> curUser = haveCurrentUser();

	@GetMapping
	public String showLoginForm(Model model) {
		List<RoomCategories> categories = getAllCategories();
		model.addAttribute("categories", categories);
		model.addAttribute("room", new Room());
		if (curUser == null) {
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "admin/login";
		} else {
			UserDto user = setCurUser(curUser);
			model.addAttribute("user", user);

			return "admin/room-manage";
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping
	public String addNewRoom(@ModelAttribute("room") Room room, Model model) {
		List<RoomCategories> categories = getAllCategories();
		Map<Long, String> mapCate = new HashMap<>();
		for (RoomCategories cate : categories) {
			mapCate.put(cate.getIdCate(), cate.getNameCate());

		}
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		room.setEnabled(true);
		room.setLocked(false);
		room.setCategory(mapCate.get(room.getCategoryId()));

		String url = "http://localhost:8081/api/room";
		LinkedHashMap<String, String> data = rest.postForObject(url, room, LinkedHashMap.class);
		log.info(data.get("name"));
		model.addAttribute("categories", categories);
		return "admin/room-manage";
	}

	@GetMapping("/room-category")
	public String showCategories(Model model) {
		List<RoomCategories> categories = getAllCategories();
		model.addAttribute("categories", categories);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		return "admin/room-category";
	}

	@GetMapping("/room-category/{id}")
	public String deleteCategories(Model model, @PathVariable String id) {
		String url = "http://localhost:8081/api/categories/" + id;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		log.info("Id: " + id);
		rest.delete(url, params);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		return "redirect:/admin/room-manage/room-category";
	}

	@PostMapping("room-category")
	public String addRoomCategories(@RequestParam("nameCate") String nameCate, Model model) {
		RoomCategories category = new RoomCategories(nameCate);
		String url = "http://localhost:8081/api/categories";
		LinkedHashMap<String, String> data = rest.postForObject(url, category, LinkedHashMap.class);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		return "redirect:/admin/room-manage/room-category";
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

	private List<RoomCategories> getAllCategories() {
		String url = "http://localhost:8081/api/categories";
		List<RoomCategories> categories = Arrays.asList(rest.getForObject(url, RoomCategories[].class));
		return categories;
	}

}
