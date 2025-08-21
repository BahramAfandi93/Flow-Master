//package com.calcpro.flowmaster.configuration
//
//import com.calcpro.flowmaster.service.UserService
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.web.SecurityFilterChain
//
//@Configuration
//@EnableWebSecurity
//class SecurityConfig(
//    private val userService: UserService,
////    private val customOAuth2UserService: CustomOAuth2UserService // Inject the service instead of defining it manually
//) {
//
//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        return http
//            .csrf { it.disable() }
//            .authorizeHttpRequests { auth ->
//                auth
//                    .antMatchers("/", "/login", "/oauth2/**").permitAll()  // Allow access to login and OAuth2 endpoints
//                    .anyRequest().authenticated()
//            }
//            .oauth2Login { oauth2 ->
//                oauth2
//                    .loginPage("/login")  // Use our custom login page
//                    .userInfoEndpoint { userInfo ->
//                        userInfo.userService(userService) // Handle user persistence
//                    }
//            }
//            .oauth2Login { oauth2 ->
//                oauth2
//                    .loginPage("/oauth2/authorization/google")  // Redirects users to Google login automatically
//                    .userInfoEndpoint { userInfo ->
//                        userInfo.userService(userService) // Use custom user service for persistence
//                    }
//            }
//            .build()
//    }
//}
