package com.fastcampus.projectboard.config;

import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import com.fastcampus.projectboard.security.BoardPrincipal;
import com.fastcampus.projectboard.security.KakaoOAuth2Response;
import com.fastcampus.projectboard.service.UserAccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService
    ) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                )
                .build();
//        return http
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .formLogin().and()
//                .build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // static resources
//        return webSecurity -> webSecurity.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountService userAccountService) {
        return username -> userAccountService
                .searchUser(username)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            UserAccountService userAccountService,
            PasswordEncoder passwordEncoder
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            final OAuth2User oAuth2User = delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String providerId = String.valueOf(kakaoResponse.id().toString());
            String username = registrationId + "_" + providerId;
            String dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID());

            return userAccountService.searchUser(username)
                    .map(BoardPrincipal::from)
                    .orElseGet(() ->
                        BoardPrincipal.from(
                                userAccountService.saveUser(
                                        username,
                                        dummyPassword,
                                        kakaoResponse.email(),
                                        kakaoResponse.nickname(),
                                        null
                                )
                        )
                    );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
