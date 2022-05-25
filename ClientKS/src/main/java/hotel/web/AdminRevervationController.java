package hotel.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import hotel.common.APIResponse;
import hotel.common.PaginationMeta;
import hotel.model.Room;
import hotel.model.RoomBooked;
import hotel.model.RoomBooking;
import hotel.model.UserDto;
import hotel.request.GetCurrentUserRequest;
import hotel.request.LoginRequestDto;
import hotel.request.PaymentRequest;
import hotel.request.RoomBookingRequestDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/revervation")
public class AdminRevervationController {

	private RestTemplate rest = new RestTemplate();

	List<Object> rooms;
	Map<String, Integer> paginationMeta;

	@GetMapping
	public String home(Model model, @CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);

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
		PaginationMeta page = setPage();
		model.addAttribute("pagination", page);
		model.addAttribute("rooms", rooms);
		List<Room> bookRooms = getAllRoom();
		model.addAttribute("bookRooms", bookRooms);

		return "admin/room-booked";
	}

	@GetMapping("/{page}")
	public String getPage(@PathVariable(value = "page") int page, Model model,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
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
		String url = "http://localhost:8081/api/room-booking/roomPageAdmin?page-number=" + page + "&page-size=5";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		rooms = (List<Object>) data.get("rooms");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");
		List<RoomBooked> dtoSugs = new ArrayList<>();
		PaginationMeta pagination = setPage();
		model.addAttribute("pagination", pagination);
		model.addAttribute("rooms", rooms);
		List<Room> bookRooms = getAllRoom();
		model.addAttribute("bookRooms", bookRooms);
		return "admin/room-booked";
	}

	@GetMapping("/delete/{id}")
	public String deleteCategories(Model model, @PathVariable String id,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		String url = "http://localhost:8081/api/room-booking/" + id;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		rest.delete(url, params);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		return "redirect:/admin/revervation";
	}

	@GetMapping("/update/{id}/{roomname}")
	public String updateRoomBooked(Model model, @PathVariable String id, @PathVariable String roomname) {
		String url = "http://localhost:8081/api/room-booking/payment";
		Map<String, String> param = new HashMap<>();
		param.put("id", id);
		param.put("roomname", roomname);
		PaymentRequest request = new PaymentRequest(id, roomname);
		rest.put(url, request, param);
		return "redirect:/admin/revervation";
	}

	@PostMapping("/bookRoomAdmin")
	public String bookRoomHanlder(@RequestParam(name = "checkIn") String checkIn,
			@RequestParam(name = "checkOut") String checkOut, @RequestParam(name = "totalMoney") String total,
			@RequestParam(name = "roomId") String id, Model model,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate checkInAt = LocalDate.parse(checkIn, formatter);
		LocalDate checkOutAt = LocalDate.parse(checkOut, formatter);
		LocalDateTime checkInDate = checkInAt.atStartOfDay();
		LocalDateTime checkOutDate = checkOutAt.atStartOfDay();
		Long totalLong = Long.parseLong(total);
		Long roomId = Long.parseLong(id);
		RoomBookingRequestDto booking = new RoomBookingRequestDto(checkInDate, checkOutDate, totalLong, false, roomId,
				"anonymous");
		String url = "http://localhost:8081/api/room-booking";
		RoomBooking roomBooking = rest.postForObject(url, booking, RoomBooking.class);
		log.info(roomBooking.getTotalMoney() + "");
		return "redirect:/admin/revervation";
	}

	@SuppressWarnings("unchecked")
	public void getRoomPagination(String username) {
		String url = "http://localhost:8081/api/room-booking/roomPageAdmin?page-size=5";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		rooms = (List<Object>) data.get("rooms");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");
	}

	private List<Room> getAllRoom() {
		String url = "http://localhost:8081/api/room";
		List<Room> rooms = Arrays.asList(rest.getForObject(url, Room[].class));
		return rooms;
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
