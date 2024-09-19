package com.xantrix.webapp.security;

import java.io.Serializable;
import java.time.Clock;
import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -3301605591108950415L;

	@Autowired
	private JwtConfig jwtConfig;

	// Get signing key (ensure it is at least 64 bytes for HS512)
	private SecretKey getSigningKey() {
		byte[] keyBytes = jwtConfig.getSecret().getBytes();
		if (keyBytes.length < 64) {
			throw new IllegalArgumentException("The secret key must be at least 64 bytes (512 bits) long for HS512.");
		}
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims != null ? claimsResolver.apply(claims) : null;
	}

	private Claims getAllClaimsFromToken(String token) {
		Claims retVal = null;
		try {
			retVal = Jwts.parserBuilder()
					.setSigningKey(getSigningKey())
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return retVal;
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration != null && expiration.before(new Date());
	}
}