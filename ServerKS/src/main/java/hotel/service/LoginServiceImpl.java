package hotel.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import hotel.common.APIResponse;
import hotel.model.ConfirmationToken;
import hotel.model.User;
import hotel.model.dto.LoginRequestDto;
import hotel.model.dto.LogoutRequestDto;
import hotel.model.dto.SignUpRequestDto;
import hotel.model.dto.UserDto;
import hotel.model.mapper.LoginMapper;
import hotel.model.mapper.UserMapper;
import hotel.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class LoginServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	private final String USER_NOT_FOUND = "User with username %s not found!";
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	private final EmailSender emailSender;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
	}

	@Transactional
	public APIResponse register(SignUpRequestDto dto) {
		boolean userExist = userRepository.findByUsername(dto.getUsername()).isPresent();
		APIResponse apiResponse = new APIResponse();
		if (userExist) {

			Map<String, String> error = new HashMap<>();
			error.put("error", "USERNAME HAS ALREADY TAKEN!");
			apiResponse.setData(error);
			return apiResponse;
		}
		String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
		dto.setPassword(encodedPassword);
		User user = LoginMapper.signUpDtoToUser(dto);
		userRepository.save(user);

		// send confirmation token
		String token = UUID.randomUUID().toString();

		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(10), user);
		confirmationTokenService.saveConfirmationToken(confirmationToken);

		String link = "http://localhost:8081/signup/confirm?token=" + token;
		emailSender.send(user.getEmail(), buildEmail(user.getName(), link));

		Map<String, String> msg = new HashMap<>();
		msg.put("mgs", "Signup Successful");
		apiResponse.setData(msg);
		return apiResponse;
	}

	@Transactional
	public String confirmToken(String token) {
		ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
				.orElseThrow(() -> new IllegalStateException("Token not found"));

		if (confirmationToken.getConfirmedAt() != null) {

			return "Email already confirmed" + "<br><a href=\"http://localhost:8080/login\">Đăng kí</a>";
		}

		LocalDateTime expiredAt = confirmationToken.getExpiredAt();

		if (expiredAt.isBefore(LocalDateTime.now())) {

			return "Token expired" + "<br><a href=\"http://localhost:8080/login\">Đăng kí</a>";
		}

		confirmationTokenService.setConfirmedAt(token);
		userRepository.enableUser(confirmationToken.getUser().getUsername());
		return "confirmed" + "<br><a href=\"http://localhost:8080/login\">Đăng nhập</a>";
	}

	private String buildEmail(String name, String link) {
		return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" + "\n"
				+ "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" + "\n"
				+ "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
				+ "    <tbody><tr>\n" + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" + "        \n"
				+ "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
				+ "          <tbody><tr>\n" + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
				+ "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
				+ "                  <tbody><tr>\n" + "                    <td style=\"padding-left:10px\">\n"
				+ "                  \n" + "                    </td>\n"
				+ "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n"
				+ "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
				+ "                    </td>\n" + "                  </tr>\n" + "                </tbody></table>\n"
				+ "              </a>\n" + "            </td>\n" + "          </tr>\n" + "        </tbody></table>\n"
				+ "        \n" + "      </td>\n" + "    </tr>\n" + "  </tbody></table>\n"
				+ "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
				+ "    <tbody><tr>\n" + "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
				+ "      <td>\n" + "        \n"
				+ "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
				+ "                  <tbody><tr>\n"
				+ "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n"
				+ "                  </tr>\n" + "                </tbody></table>\n" + "        \n" + "      </td>\n"
				+ "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" + "    </tr>\n"
				+ "  </tbody></table>\n" + "\n" + "\n" + "\n"
				+ "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
				+ "    <tbody><tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n" + "    <tr>\n"
				+ "      <td width=\"10\" valign=\"middle\"><br></td>\n"
				+ "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n"
				+ "        \n"
				+ "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Xin chào "
				+ name
				+ ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Cảm ơn bạn đã đăng kí. Vui lòng click đường link dưới đây để kích hoạt tài khoản. </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\""
				+ link + "\">Activate Now</a> </p></blockquote>\n Link sẽ hết hạn sau 15 phút. <p>Xin cảm ơn!</p>"
				+ "        \n" + "      </td>\n" + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
				+ "    </tr>\n" + "    <tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n"
				+ "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" + "\n" + "</div></div>";
	}

	public APIResponse login(LoginRequestDto dto) {
		APIResponse aiApiResponse = new APIResponse();
		Optional<User> userExist = userRepository.findOneByUsernameIgnoreCaseEnabled(dto.getUsername(), dto.getRole());

		if (userExist.isPresent()) {
			User user = userExist.get();
			if (user.getLocked()) {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Accout with this username is locked!");
				aiApiResponse.setData(error);
				aiApiResponse.setData(error);
				aiApiResponse.setStatus(404);
				return aiApiResponse;
			}
			if (bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
				UserDto userDto = UserMapper.toUserDto(user);
				Map<String, String> account = new HashMap<>();
				account.put("name", user.getName());
				account.put("email", user.getEmail());
				account.put("avatar", user.getAvatar());
				account.put("role", user.getRole());
				account.put("username", user.getUsername());
				account.put("phone", user.getPhone());
				aiApiResponse.setData(account);
				userRepository.activeUser(user.getUsername());
			} else {
				Map<String, String> error = new HashMap<>();
				error.put("error", "Wrong Password!");
				aiApiResponse.setData(error);
				aiApiResponse.setStatus(404);
			}

		} else {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Username not found!");
			aiApiResponse.setData(error);
			aiApiResponse.setData(error);
			aiApiResponse.setStatus(404);
		}
		return aiApiResponse;
	}

	public APIResponse getCurrenUser(String role) {
		APIResponse apiResponse = new APIResponse();
		Optional<User> curUser = userRepository.findByActivedAndRole(role);
		if (curUser.isPresent()) {
			User user = curUser.get();
			UserDto userDto = UserMapper.toUserDto(user);
			Map<String, String> account = new HashMap<>();
			account.put("name", user.getName());
			account.put("email", user.getEmail());
			account.put("avatar", user.getAvatar());
			account.put("role", user.getRole());
			account.put("username", user.getUsername());
			account.put("phone", user.getPhone());
			apiResponse.setData(account);
//			log.info(user.getUsername());
		} else {
			apiResponse.setStatus(404);
			Map<String, String> error = new HashMap<>();
			error.put("error", "No current user");
			apiResponse.setData(error);
		}
//		log.info(role);
		return apiResponse;
	}

	public APIResponse logout(LogoutRequestDto logoutRequestDto) {
		APIResponse apiResponse = new APIResponse();
		Optional<User> curUser = userRepository.findByActivedAndUsername(true, logoutRequestDto.getUsername());
		if (curUser.isPresent()) {
			User user = curUser.get();
			userRepository.inactiveUser(user.getUsername());
			apiResponse.setData("Logout successfull!");
		}
		return apiResponse;
	}
}
