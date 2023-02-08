package com.example.demo.comm;

import com.example.demo.config.JwtConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.RequestScope;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequestScope
@Slf4j
public class JwtUtils {

    @Value("${app.cookie.jwt.secret}")
    private String jwtSecret;

    @Value("${app.cookie.jwt.expiration}")
    private int jwtExpirationMs;

    @Value("${app.cookie.jwt.name}")
    private String jwtCookie;

    @Resource
    private ObjectMapper objectMapper;

    public void generateJwtCookie(HttpSession httpSession, UserDetailsImpl userPrincipal) throws JsonProcessingException {
        String jwt = generateTokenFromUsername(userPrincipal);
        httpSession.setAttribute(jwtCookie, jwt);
    }

    private Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateTokenFromUsername(UserDetailsImpl userPrincipal) throws JsonProcessingException {
        Key key = this.getKey();
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtConfig.userKey, objectMapper.writeValueAsString(userPrincipal));
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        String jwt = (String) httpSession.getAttribute(jwtCookie);
        if (StringUtils.hasText(jwt)){
            return jwt;
        } else {
            return null;
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Key key = this.getKey();
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public UserDetailsImpl getUserNameFromJwtToken(String token) throws JsonProcessingException {
        Key key = this.getKey();
        String json = (String) Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody().get(JwtConfig.userKey);


        SimpleModule module = new SimpleModule("GrantedAuthority");
        String moduleName = module.getModuleName();
        module.addDeserializer(GrantedAuthority.class, new GrantedAuthorityDeser());
        Set<Object> registeredModuleIds = objectMapper.getRegisteredModuleIds();
        boolean isRegistered = false;
        for (Object registeredModuleId : registeredModuleIds) {
            isRegistered = registeredModuleId.equals(moduleName);
            if (isRegistered) {
                break;
            }
        }
        if (!isRegistered) {
            objectMapper.registerModule(module);
        }

        return objectMapper.readValue(json, UserDetailsImpl.class);
    }
}
