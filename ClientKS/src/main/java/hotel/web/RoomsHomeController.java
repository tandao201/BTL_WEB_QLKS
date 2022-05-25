package hotel.web;

import java.util.ArrayList;
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
import hotel.model.RoomDto;
import hotel.model.UserDto;
import hotel.request.GetCurrentUserRequest;
import hotel.request.RoomBookingRequestDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/rooms")
public class RoomsHomeController {
	RestTemplate rest = new RestTemplate();
	List<Object> rooms;
	Map<String, Integer> paginationMeta;

	@GetMapping()
	public String home(Model model, @CookieValue(value = "user", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		getRoomPagination();
		List<RoomDto> dtoSugs = new ArrayList<>();
		for (Object room : rooms) {
			dtoSugs.add(toRoomDto(room));
		}

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
		PaginationMeta page = setPage();
		model.addAttribute("pagination", page);
		model.addAttribute("rooms", dtoSugs);
		return "customer/rooms";
	}

	@GetMapping("/{page}")
	public String getPage(@PathVariable(value = "page") int page, Model model,
			@CookieValue(value = "user", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
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
		String url = "http://localhost:8081/api/room/roomPage?page-number=" + page;
		Map<String, Object> data = rest.getForObject(url, Map.class);
		rooms = (List<Object>) data.get("rooms");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");
		List<RoomDto> dtoSugs = new ArrayList<>();
		for (Object room : rooms) {
			dtoSugs.add(toRoomDto(room));
		}
		PaginationMeta pagination = setPage();
		model.addAttribute("pagination", pagination);
		model.addAttribute("rooms", dtoSugs);
		return "customer/rooms";
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

	@SuppressWarnings("unchecked")
	public void getRoomPagination() {
		String url = "http://localhost:8081/api/room/roomPage";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		rooms = (List<Object>) data.get("rooms");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");
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

	private RoomDto toRoomDto(Object room) {
		RoomDto dto = new RoomDto();
		Map<String, Object> data = (Map<String, Object>) room;
		dto.setId((int) data.get("id"));
		dto.setName((String) data.get("name"));
		dto.setPrice((int) data.get("price"));
		dto.setImage((String) data.get("image"));
		dto.setLocked((boolean) data.get("locked"));
		dto.setEnabled((boolean) data.get("enabled"));
		dto.setQuantity((int) data.get("quantity"));
		dto.setCategoryId((int) data.get("categoryId"));
		dto.setCategory((String) data.get("category"));
		dto.setRoomBookings((List<RoomBookingRequestDto>) data.get("roomBookings"));
		String tmp = (String) data.get("description");
		String[] des = tmp.split("\\|");
		dto.setDescription(des);
		return dto;
	}
}
