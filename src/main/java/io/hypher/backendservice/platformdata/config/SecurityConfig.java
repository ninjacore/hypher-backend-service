// package io.hypher.backendservice.platformdata.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.access.channel.ChannelProcessingFilter;

// import static org.springframework.security.config.Customizer.withDefaults;

// import org.apache.catalina.filters.CorsFilter;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//         // pre-flight don't have JSESSIONIDs
//         // http.cors(withDefaults()); // deprecated
//         http.cors(Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer)

//         // cross-site request forgery
//         http.csrf().disable();

//         // permit all for first phase
//         http.authorizeHttpRequests()
//             .requestMatchers("/actuator/**").permitAll()
//             .requestMatchers("/api/**").permitAll();
//             // .anyRequest().authenticated();

//         // ensure CORS config is applied to all requests
//         http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);

//         return http.build();



//     }
    
// }
