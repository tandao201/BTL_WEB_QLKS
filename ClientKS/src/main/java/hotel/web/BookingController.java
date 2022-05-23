package hotel.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import hotel.common.APIResponse;
import hotel.model.Room;
import hotel.model.RoomBooking;
import hotel.model.RoomBookingRequestDto;
import hotel.model.UserDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/booking", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
		MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })

public class BookingController {

	RestTemplate rest = new RestTemplate();

	@GetMapping
	public String bookingRoom(Model model) {
		LinkedHashMap<String, String> curUser = haveCurrentUser();
		model.addAttribute("booking", new RoomBookingRequestDto());
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
		return "customer/booking";
	}

	@PostMapping
	public String showFormWhenClickBook(@RequestParam(name = "id") String id,
			@RequestParam(name = "quantity") String quantity, @RequestParam(name = "category") String category,
			@RequestParam(name = "image") String image, @RequestParam(name = "name") String name,
			@RequestParam(name = "price") String price, Model model) {
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
		RoomBookingRequestDto room = new RoomBookingRequestDto();
		room.setStatus(false);
		Room roomX = new Room();
		roomX.setName(name);
		roomX.setCategory(category);
		roomX.setImage(image);
		roomX.setId(Integer.valueOf(id));
		roomX.setPrice(Integer.parseInt(price));
		roomX.setQuantity(Integer.valueOf(quantity));
		model.addAttribute("room", roomX);
		model.addAttribute("booking", room);
		log.info(id);
		return "customer/booking";
	}

	@PostMapping("/bookRoom")
	public String bookRoomHanlder(@RequestParam(name = "checkIn") String checkIn,
			@RequestParam(name = "checkOut") String checkOut, @RequestParam(name = "totalMoney") String total,
			@RequestParam(name = "roomId") String id, Model model) {
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate checkInAt = LocalDate.parse(checkIn, formatter);
		LocalDate checkOutAt = LocalDate.parse(checkOut, formatter);
		LocalDateTime checkInDate = checkInAt.atStartOfDay();
		LocalDateTime checkOutDate = checkOutAt.atStartOfDay();
		Long totalLong = Long.parseLong(total);
		Long roomId = Long.parseLong(id);
		RoomBookingRequestDto booking = new RoomBookingRequestDto(checkInDate, checkOutDate, totalLong, false, roomId,
				curUser.get("username"));
		String url = "http://localhost:8081/api/room-booking";
		RoomBooking roomBooking = rest.postForObject(url, booking, RoomBooking.class);
		log.info(roomBooking.getTotalMoney() + "");
		return "redirect:/revervation";
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
