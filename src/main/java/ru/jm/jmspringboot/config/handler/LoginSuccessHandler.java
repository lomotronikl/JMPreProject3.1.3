package ru.jm.jmspringboot.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        System.out.println("authority:");
        for ( GrantedAuthority authority:
          authentication.getAuthorities()) {
            System.out.println(authority.getAuthority().toUpperCase());
         /*
            if (authority.getAuthority().toUpperCase().indexOf("ADMIN")>=0){
                httpServletResponse.sendRedirect("admin/");
                return;
            }

          */
        }
        httpServletResponse.sendRedirect("user/");
    }
}