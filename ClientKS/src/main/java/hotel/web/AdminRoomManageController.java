package hotel.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import hotel.common.APIResponse;
import hotel.common.PaginationMeta;
import hotel.model.Room;
import hotel.model.RoomCategories;
import hotel.model.UserDto;
import hotel.request.GetCurrentUserRequest;
import hotel.request.LockedRoomRequest;
import hotel.request.LoginRequestDto;
import hotel.request.UpdateRoomDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/room-manage")
public class AdminRoomManageController {

	private RestTemplate rest = new RestTemplate();
	List<Object> rooms;
	Map<String, Integer> paginationMeta;
	String success = "";
	String error = "";

	@GetMapping
	public String showForm(Model model, @CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		List<RoomCategories> categories = getAllCategories();
		model.addAttribute("categories", categories);
		model.addAttribute("room", new Room());
		if (curUser == null) {
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "admin/login";
		} else {
			UserDto user = setCurUser(curUser);
			model.addAttribute("user", user);

		}
		getPagination();
		PaginationMeta pagination = setPage();
		if (!success.isEmpty()) {
			model.addAttribute("success", success);
		}
		if (!error.isEmpty()) {
			model.addAttribute("error", error);
		}
		success = "";
		error = "";
		model.addAttribute("pagination", pagination);
		model.addAttribute("rooms", rooms);
		return "admin/room-manage";
	}

	@SuppressWarnings("unchecked")
	@PostMapping
	public String addNewRoom(@ModelAttribute("room") Room room, Model model,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		List<RoomCategories> categories = getAllCategories();
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
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
		return "redirect:/admin/room-manage";
	}

	@GetMapping("/room-category")
	public String showCategories(Model model,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		List<RoomCategories> categories = getAllCategories();
		model.addAttribute("categories", categories);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		return "admin/room-category";
	}

	@GetMapping("/room-category/{id}")
	public String deleteCategories(Model model, @PathVariable String id,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
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
	public String addRoomCategories(@RequestParam("nameCate") String nameCate, Model model,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		RoomCategories category = new RoomCategories(nameCate);
		String url = "http://localhost:8081/api/categories";
		LinkedHashMap<String, String> data = rest.postForObject(url, category, LinkedHashMap.class);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		return "redirect:/admin/room-manage/room-category";
	}

	@GetMapping("/locked/{id}/{locked}")
	public String lockRoom(Model model, @PathVariable("id") String id, @PathVariable("locked") String lockedStr,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		String url = "http://localhost:8081/api/room/locked";
		boolean locked = Boolean.parseBoolean(lockedStr);
		locked = !locked;
		lockedStr = lockedStr.toString();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("locked", lockedStr);
		LockedRoomRequest request = new LockedRoomRequest(id, locked);
		rest.put(url, request, params);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		success = "Cập nhật thành công!";
		model.addAttribute("room", new Room());
		return "redirect:/admin/room-manage";
	}

	@GetMapping("/{page}")
	public String getPage(@PathVariable(value = "page") int page, Model model,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		List<RoomCategories> categories = getAllCategories();
		model.addAttribute("categories", categories);
		if (curUser == null) {
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "admin/login";

		} else {
			UserDto user = setCurUser(curUser);
			model.addAttribute("user", user);
		}
		String url = "http://localhost:8081/api/room/roomPageAdmin?page-number=" + page + "&page-size=5";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		rooms = (List<Object>) data.get("rooms");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");

		PaginationMeta pagination = setPage();
		model.addAttribute("pagination", pagination);
		model.addAttribute("rooms", rooms);
		model.addAttribute("room", new Room());
		return "admin/room-manage";
	}

	private List<Room> getAllRooms() {
		String url = "http://localhost:8081/api/room";
		List<Room> rooms = Arrays.asList(rest.getForObject(url, Room[].class));
		return rooms;
	}

	@PostMapping("/edit")
	public String editRoom(Model model, @RequestParam("roomName") String name, @RequestParam("price") String price,
			@RequestParam("image") String image, @RequestParam("quantity") String quantity,
			@RequestParam("roomId") String id, @RequestParam("idCategory") String categoryId,
			@RequestParam("description") String description,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		List<RoomCategories> categories = getAllCategories();
		Map<Long, String> mapCate = new HashMap<>();
		for (RoomCategories cate : categories) {
			mapCate.put(cate.getIdCate(), cate.getNameCate());

		}
		String url = "http://localhost:8081/api/room";
		Long idLong = Long.parseLong(id);
		Long quantityLong = Long.parseLong(quantity);
		Long idCate = Long.parseLong(categoryId);
		Long priceLong = Long.parseLong(price);
		UpdateRoomDto updateRoomDto = new UpdateRoomDto(idLong, name, priceLong, image, quantityLong, idCate,
				mapCate.get(idCate), description);
		Map<String, String> param = new HashMap<>();
		param.put("id", id);
		param.put("name", name);
		param.put("price", price);
		param.put("image", image);
		param.put("quantity", quantity);
		param.put("categoryId", categoryId);
		param.put("description", description);
		rest.put(url, updateRoomDto, param);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		success = "Cập nhật thành công!";
		model.addAttribute("room", new Room());
		return "redirect:/admin/room-manage";
	}

	@GetMapping("/delete/{id}")
	public String deleteRoom(Model model, @PathVariable String id,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		String url = "http://localhost:8081/api/room/" + id;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		rest.delete(url, params);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		return "redirect:/admin/room-manage";
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

	private List<RoomCategories> getAllCategories() {
		String url = "http://localhost:8081/api/categories";
		List<RoomCategories> categories = Arrays.asList(rest.getForObject(url, RoomCategories[].class));
		return categories;
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

	@SuppressWarnings("unchecked")
	public void getPagination() {
		String url = "http://localhost:8081/api/room/roomPageAdmin?page-size=5";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		rooms = (List<Object>) data.get("rooms");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");
	}

}
