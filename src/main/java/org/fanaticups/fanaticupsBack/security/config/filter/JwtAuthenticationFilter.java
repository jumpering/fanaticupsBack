package org.fanaticups.fanaticupsBack.security.config.filter;

import java.io.IOException;

import org.fanaticups.fanaticupsBack.dao.entities.UserEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.UserRepository;
import org.fanaticups.fanaticupsBack.security.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        //1. obtener el header con el jwt
        String authHeader = request.getHeader("Authorization"); //Bearer
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        //2 Obtener el jwt del header
        String jwt = authHeader.split(" ")[1];

        //3. Obtener el subject/usename desde el jwt
        String username = this.jwtService.extractUsername(jwt);

        //4. Setear un objeto Authentication dentro del securityContext (que está dentro del securityContextHolder)
        
        UserEntity userEntity = this.userRepository.findByEmail(username).get();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            username, null, userEntity.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        //5. ejecutar el resto de filres
        filterChain.doFilter(request, response);




        //////....TODO para agregar header s nuevas peticiones
        // if (jwtUtilService.validateToken(jwt, userDetails)) {

        //         UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        //                 userDetails, null, userDetails.getAuthorities());
        //         authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //         SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //     }
    }
    
}
