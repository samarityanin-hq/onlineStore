package main.store.ControllersTest;

import main.store.Config.JwtService;
import main.store.Config.RateLimiter;
import main.store.DTO.Response.JwtAuthentication;
import main.store.Entities.User;
import main.store.Enums.UserRole;
import main.store.Security.CustomUserDetails;
import main.store.Security.JwtFilter;
import main.store.Security.SecurityConfig;
import main.store.Security.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.Duration;
import java.util.Collections;

import static org.mockito.Mockito.*;

@Import({SecurityConfig.class, JwtFilter.class})
public abstract class AbstractWebTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected JwtService jwtService;
    @MockitoBean
    protected UserDetailService userDetailService;
    @MockitoBean
    protected RateLimiter rateLimiter;

    protected User user;
    protected CustomUserDetails userDetails;
    protected String accessToken = "valid-access-token";
    protected String refreshToken = "valid-refresh-token";

    void setUpConfig(){
        user = new User();
        user.setEmail("user@email.com");
        user.setRole(UserRole.ROLE_USER);
        userDetails = new CustomUserDetails(user,
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));

        when(rateLimiter.allowRequest(any(String.class), any(Integer.class), any(Duration.class))).thenReturn(true);

        JwtAuthentication jwt = new JwtAuthentication(accessToken, refreshToken);
        when(jwtService.generateAuthToken(any(User.class))).thenReturn(jwt);
        when(jwtService.validateJwtToken(accessToken)).thenReturn(true);
        when(jwtService.isAccessToken(accessToken)).thenReturn(true);
        when(jwtService.validateJwtToken("invalid-access-token")).thenReturn(false);
        when(jwtService.getEmailFromToken(accessToken)).thenReturn("user@email.com");

        when(userDetailService.loadUserByUsername("user@email.com")).thenReturn(userDetails);

    }
}
