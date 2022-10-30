package main.security;

import io.smallrye.jwt.build.Jwt;
import main.dto.Role;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class JwtService {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    private String generateJsonWebToken(Set<Role> roles) {
        Set<String> groups = Set.of(roles.stream().map(Role::getValue).toArray(String[]::new));
        return Jwt.issuer(issuer)
                .subject("wmapp-2022-jwt")
                .upn("wmapp-2022-jwt")
                .claim(Claims.birthdate.name(), "1985-10-25")
                .claim("clientUUID", "Test")
                .groups(groups)
                .expiresAt(System.currentTimeMillis() + 3600)
                .sign();
    }
    public String generateUserJwt() {
        return generateJsonWebToken(Set.of(Role.USER));
    }

    public String generateAdminJwt() {
        return generateJsonWebToken(Set.of(Role.ADMIN));
    }

    public String generateSuperAdminJwt() {
        return generateJsonWebToken(Set.of(Role.USER, Role.ADMIN));
    }


}
