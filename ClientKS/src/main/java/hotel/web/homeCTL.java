package hotel.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import hotel.common.APIResponse;
import hotel.model.GetCurrentUserRequest;
import hotel.model.LoginRequestDto;
import hotel.model.Room;
import hotel.model.RoomDto;
import hotel.model.SignUpRequestDto;
import hotel.model.UserDto;

@Controller
public class homeCTL {
	RestTemplate rest = new RestTemplate();

	@GetMapping("/")
	public String home(Model model, @CookieValue(value = "user", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		List<Room> sugRooms = getRoomSuggest();
		List<RoomDto> dtoSugs = new ArrayList<>();
		for (Room room : sugRooms) {
			dtoSugs.add(toRoomDto(room));
		}
		model.addAttribute("rooms", dtoSugs);
		if (curUser == null) {
			model.addAttribute("signupDto", new SignUpRequestDto());
			model.addAttribute("loginRequest", new LoginRequestDto());
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

	@GetMapping("/about")
	public String about(Model model, @CookieValue(value = "user", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		List<Room> sugRooms = getRoomSuggest();
		List<RoomDto> dtoSugs = new ArrayList<>();
		for (Room room : sugRooms) {
			dtoSugs.add(toRoomDto(room));
		}
		model.addAttribute("rooms", dtoSugs);
		if (curUser == null) {
			model.addAttribute("signupDto", new SignUpRequestDto());
			model.addAttribute("loginRequest", new LoginRequestDto());
			model.addAttribute("user", null);

		} else {
			UserDto user = new UserDto();
			user.setEmail(curUser.get("email"));
			user.setName(curUser.get("name"));
			user.setAvatar(curUser.get("avatar"));
			user.setPhone(curUser.get("phone"));
			model.addAttribute("user", user);
		}
		return "customer/about";
	}

	@GetMapping("/contact")
	public String contact(Model model, @CookieValue(value = "user", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		List<Room> sugRooms = getRoomSuggest();
		List<RoomDto> dtoSugs = new ArrayList<>();
		for (Room room : sugRooms) {
			dtoSugs.add(toRoomDto(room));
		}
		model.addAttribute("rooms", dtoSugs);
		if (curUser == null) {
			model.addAttribute("signupDto", new SignUpRequestDto());
			model.addAttribute("loginRequest", new LoginRequestDto());
			model.addAttribute("user", null);

		} else {
			UserDto user = new UserDto();
			user.setEmail(curUser.get("email"));
			user.setName(curUser.get("name"));
			user.setAvatar(curUser.get("avatar"));
			user.setPhone(curUser.get("phone"));
			model.addAttribute("user", user);
		}
		return "customer/contact";
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

	public List<Room> getRoomSuggest() {
		String url = "http://localhost:8081/api/room/suggest";
		List<Room> rooms = Arrays.asList(rest.getForObject(url, Room[].class));
		return rooms;
	}

	private RoomDto toRoomDto(Room room) {
		RoomDto dto = new RoomDto();
		dto.setId(room.getId());
		dto.setName(room.getName());
		dto.setPrice(room.getPrice());
		dto.setImage(room.getImage());
		dto.setLocked(room.getLocked());
		dto.setEnabled(room.getEnabled());
		dto.setQuantity(room.getQuantity());
		dto.setCategoryId(room.getCategoryId());
		dto.setCategory(room.getCategory());
		dto.setRoomBookings(room.getRoomBookings());
		String[] des = room.getDescription().split("\\|");
		dto.setDescription(des);
		return dto;
	}
}
