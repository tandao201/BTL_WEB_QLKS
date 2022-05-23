package hotel.web;

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
import hotel.common.PaginationMeta;
import hotel.model.AdminSignUpRequest;
import hotel.model.LockedUserRequest;
import hotel.model.LoginRequestDto;
import hotel.model.UpdateRequestDto;
import hotel.model.UserDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/account-manage")
public class AdminAccountManageController {
	RestTemplate rest = new RestTemplate();
	List<Object> users;
	Map<String, Integer> paginationMeta;
	LinkedHashMap<String, String> curUser = haveCurrentUser();
	String success = "";

	@GetMapping
	public String showForm(Model model) {
		curUser = haveCurrentUser();
		LinkedHashMap<String, String> curUser = haveCurrentUser();
		getPagination();

		if (curUser == null) {
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
		PaginationMeta page = setPage();
		model.addAttribute("pagination", page);
		model.addAttribute("users", users);
		if (!success.isEmpty()) {
			model.addAttribute("success", success);
		}
		success = "";
		model.addAttribute("account", new AdminSignUpRequest());
		return "admin/account-manage";
	}

	@GetMapping("/{page}")
	public String getPage(@PathVariable(value = "page") int page, Model model) {
		curUser = haveCurrentUser();
		if (curUser == null) {
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
		String url = "http://localhost:8081/users/userPage?page-number=" + page + "&page-size=5";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		users = (List<Object>) data.get("users");
		paginationMeta = (Map<String, Integer>) data.get("paginationMeta");

		PaginationMeta pagination = setPage();
		model.addAttribute("pagination", pagination);
		model.addAttribute("users", users);
		model.addAttribute("account", new AdminSignUpRequest());
		return "admin/account-manage";
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(Model model, @PathVariable String id) {
		String url = "http://localhost:8081/users/" + id;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		rest.delete(url, params);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		success = "Xóa thành công!";
		model.addAttribute("account", new AdminSignUpRequest());
		return "redirect:/admin/account-manage";
	}

	@PostMapping("/edit")
	public String editUser(Model model, @RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam("role") String role, @RequestParam("name") String name) {
		String url = "http://localhost:8081/users/admin";
		UpdateRequestDto updateRequestDto = new UpdateRequestDto(username, email, phone, "", name, role);
		Map<String, String> param = new HashMap<>();
		param.put("username", username);
		param.put("email", email);
		param.put("phone", phone);
		param.put("role", role);
		param.put("name", name);
		rest.put(url, updateRequestDto, param);
		curUser = haveCurrentUser();
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		success = "Cập nhật thành công!";
		model.addAttribute("account", new AdminSignUpRequest());
		return "redirect:/admin/account-manage";
	}

	@GetMapping("/locked/{id}/{locked}")
	public String lockUser(Model model, @PathVariable("id") String id, @PathVariable("locked") String lockedStr) {
		String url = "http://localhost:8081/users/admin/locked";
		boolean locked = Boolean.parseBoolean(lockedStr);
		locked = !locked;
		lockedStr = lockedStr.toString();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("locked", lockedStr);
		LockedUserRequest request = new LockedUserRequest(id, locked);
		rest.put(url, request, params);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		success = "Cập nhật thành công!";
		model.addAttribute("account", new AdminSignUpRequest());
		return "redirect:/admin/account-manage";
	}

	@SuppressWarnings("unchecked")
	public void getPagination() {
		String url = "http://localhost:8081/users/userPage?page-size=5";
		Map<String, Object> data = rest.getForObject(url, Map.class);
		users = (List<Object>) data.get("users");
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

	@SuppressWarnings("unchecked")
	@PostMapping
	public String addNewUser(@ModelAttribute("account") AdminSignUpRequest request, Model model) {
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		request.setEnabled(true);
		request.setLocked(false);
		request.setAvatar("https://cdn5.vectorstock.com/i/1000x1000/27/89/user-account-flat-icon-vector-14992789.jpg");
		request.setActived(false);
		String url = "http://localhost:8081/users";
		LinkedHashMap<String, String> data = rest.postForObject(url, request, LinkedHashMap.class);
		log.info(data.get("name"));
		return "admin/account-manage";
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
}
