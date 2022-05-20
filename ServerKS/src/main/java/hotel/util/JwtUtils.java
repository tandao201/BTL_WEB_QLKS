package hotel.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import hotel.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

	private static String secret = "something";
	private static long expDuration = 60 * 60;

	public String generateJwt(User user) {

		long miliTime = System.currentTimeMillis();
		long expTime = miliTime + expDuration * 1000;

		Date issuedAt = new Date(miliTime);
		Date expriration = new Date(expTime);
		Claims claims = Jwts.claims().setIssuer(user.getId().toString()).setIssuedAt(issuedAt)
				.setExpiration(expriration);

		claims.put("role", user.getRole());
		claims.put("name", user.getName());
		claims.put("username", user.getUsername());

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}
}
