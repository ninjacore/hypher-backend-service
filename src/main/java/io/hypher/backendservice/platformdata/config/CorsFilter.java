// package io.hypher.backendservice.platformdata.config;

// import jakarta.servlet.ServletException;
// import java.io.IOException;

// import org.springframework.web.filter.OncePerRequestFilter;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// public class CorsFilter extends OncePerRequestFilter{

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

//         response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

//         response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

//         response.setHeader("Access-Control-Allow-Headers", "*");

//         // cookies allowed
//         response.setHeader("Access-Control-Allow-Credentials", "true");

//         response.setHeader("Access-Control-Max-Age", "3600");


//         filterChain.doFilter(request, response);

//     }
    
// }
