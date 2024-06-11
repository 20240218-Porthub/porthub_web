package hello.example.porthub.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;

        if ("해당 계정은 정지되었습니다.".equals(exception.getMessage())) {
            errorMessage = "해당 계정은 정지되었습니다.";
        } else if (exception instanceof org.springframework.security.authentication.BadCredentialsException) {
            errorMessage = "이메일과 비밀번호를 확인하세요!";
        } else if (exception instanceof LockedException) {
            errorMessage = "해당 계정은 정지되었습니다. 문의 메일: wjdgusdlekd3@yonsei.ac.kr";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "Sys Error";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "This account does not exist";
        } else {
            errorMessage = "Unknown error.";
        }




        // Log the error message
        logger.error("Authentication failed: {}", errorMessage);

        // Encode the error message to safely pass it as a URL parameter
        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

        try {
            response.sendRedirect("/login?error=" + encodedErrorMessage);
        } catch (IOException e) {
            // Log the IOException
            logger.error("IOException occurred while redirecting: ", e);
        }


    }

}
