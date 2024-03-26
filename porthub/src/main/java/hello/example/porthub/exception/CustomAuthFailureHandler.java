package hello.example.porthub.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;

        if (exception instanceof org.springframework.security.authentication.BadCredentialsException) {
            errorMessage = "Check Your Email and Password";
        } else if (exception instanceof org.springframework.security.authentication.InternalAuthenticationServiceException) {
            errorMessage = "Sys Error";
        } else if (exception instanceof org.springframework.security.core.userdetails.UsernameNotFoundException) {
            errorMessage = "This account does not exist";
        } else {
            errorMessage = "Unknown error.";
        }

        try {
            response.sendRedirect("/login?error=" + errorMessage);
        } catch (IOException e) {
            // IOException 처리
            e.printStackTrace();
        }
    }
}
