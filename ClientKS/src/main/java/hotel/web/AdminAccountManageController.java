package hotel.web;

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
import hotel.model.UserDto;
import hotel.request.AdminSignUpRequest;
import hotel.request.GetCurrentUserRequest;
import hotel.request.LockedUserRequest;
import hotel.request.LoginRequestDto;
import hotel.request.UpdateRequestDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/account-manage")
public class AdminAccountManageController {
	RestTemplate rest = new RestTemplate();
	List<Object> users;
	Map<String, Integer> paginationMeta;
	String success = "";
	String error = "";

	@GetMapping
	public String showForm(Model model, @CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		getPagination();

		if (curUser == null) {
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "admin/login";
		} else {
			UserDto user = setCurUser(curUser);
			model.addAttribute("user", user);
		}
		PaginationMeta pagination = setPage();

		if (!success.isEmpty()) {
			model.addAttribute("success", success);
		}
		if (!error.isEmpty()) {
			model.addAttribute("error", error);
		}
		success = "";
		error = "";
		model.addAttribute("account", new AdminSignUpRequest());
		model.addAttribute("pagination", pagination);
		model.addAttribute("users", users);
		return "admin/account-manage";
	}

	@GetMapping("/{page}")
	public String getPage(@PathVariable(value = "page") int page, Model model,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		if (curUser == null) {
			model.addAttribute("loginRequest", new LoginRequestDto());
			return "admin/login";

		} else {
			UserDto user = setCurUser(curUser);
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
	public String deleteUser(Model model, @PathVariable String id,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		String url = "http://localhost:8081/users/" + id;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		rest.delete(url, params);
		UserDto user = setCurUser(curUser);
		success = "Xóa thành công!";
		model.addAttribute("user", user);
		model.addAttribute("account", new AdminSignUpRequest());
		return "redirect:/admin/account-manage";
	}

	@PostMapping("/edit")
	public String editUser(Model model, @RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam("role") String role, @RequestParam("name") String name,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String usernameCookie) {
		String url = "http://localhost:8081/users/admin";
		UpdateRequestDto updateRequestDto = new UpdateRequestDto(username, email, phone, "", name, role);
		Map<String, String> param = new HashMap<>();
		param.put("username", username);
		param.put("email", email);
		param.put("phone", phone);
		param.put("role", role);
		param.put("name", name);
		rest.put(url, updateRequestDto, param);
		LinkedHashMap<String, String> curUser = haveCurrentUser(usernameCookie);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		success = "Cập nhật thành công!";
		model.addAttribute("account", new AdminSignUpRequest());
		return "redirect:/admin/account-manage";
	}

	@GetMapping("/locked/{id}/{locked}")
	public String lockUser(Model model, @PathVariable("id") String id, @PathVariable("locked") String lockedStr,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		String url = "http://localhost:8081/users/admin/locked";
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
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
	public String addNewUser(@ModelAttribute("account") AdminSignUpRequest request, Model model,
			@CookieValue(value = "userAdmin", defaultValue = "no user") String username) {
		LinkedHashMap<String, String> curUser = haveCurrentUser(username);
		UserDto user = setCurUser(curUser);
		model.addAttribute("user", user);
		request.setEnabled(true);
		request.setLocked(false);
		request.setAvatar("https://cdn5.vectorstock.com/i/1000x1000/27/89/user-account-flat-icon-vector-14992789.jpg");
		request.setActived(false);
		String url = "http://localhost:8081/users";
		LinkedHashMap<String, String> data = rest.postForObject(url, request, LinkedHashMap.class);
		if (data.get("error") != null) {
			error = data.get("error");
		} else {
			success = "Thêm mới thành công!";
		}
		return "redirect:/admin/account-manage";
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

	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, String> haveCurrentUserForEditUsers(String username, String role) {
		String urlCurUser = "http://localhost:8081/login/currentUser";
		GetCurrentUserRequest request = new GetCurrentUserRequest(role, username);
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
