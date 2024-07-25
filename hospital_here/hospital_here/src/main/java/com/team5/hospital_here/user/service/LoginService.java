package com.team5.hospital_here.user.service;


import com.team5.hospital_here.common.exception.CustomException;
import com.team5.hospital_here.common.exception.ErrorCode;
import com.team5.hospital_here.common.jwt.JwtUtil;
import com.team5.hospital_here.common.jwt.entity.RefreshToken;
import com.team5.hospital_here.common.jwt.repository.RefreshTokenRepository;
import com.team5.hospital_here.user.entity.login.Login;
import com.team5.hospital_here.user.entity.login.LoginDTO;
import com.team5.hospital_here.user.repository.LoginRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    private final String LOGIN_SUCCESS = "로그인 인증되었습니다.";
    private final String TOKEN_REFRESH_SUCCESS = "토큰이 재발급 되었습니다.";

    /**
     * 이메일로 로그인 정보를 검색합니다.
     * @param email 검색할 이메일
     * @return 검색한 로그인 정보
     * @exception CustomException 존재하지 않는 로그인 정보
     */
    public Login findByEmail(String email){
        return loginRepository.findByEmail(email).orElseThrow(()->
            new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 로그인 정보가 맞는지 검증하고
     * JWT를 발행합니다.
     * @param loginDTO 검증할 로그인 정보
     * @param response JWT를 발행할 서블렛
     * @return 로그인 성공
     * @exception CustomException 존재하지 않는 로그인 정보 또는 비밀번호 매칭 실패
     */
    public ResponseEntity<Map<String, Object>> login(LoginDTO loginDTO, HttpServletResponse response){
        Login login = findByEmail(loginDTO.getEmail());
        matchPassword(loginDTO, login);

        RefreshToken dbToken = refreshTokenRepository.findByLogin(login).orElse(new RefreshToken());
        dbToken.setLogin(login);

        createToken(dbToken, login.getEmail(), response);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", login.getId());//유저 id랑 로그인 id 랑 같아서
        responseBody.put("message", LOGIN_SUCCESS);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private void createToken(RefreshToken dbToken, String email, HttpServletResponse response){
        String accessToken = "Bearer " + jwtUtil.generateAccessToken(email);
        response.setHeader("Authorization", accessToken);

        String refreshToken = jwtUtil.generateRefreshToken(email);
        Cookie cookie = new Cookie(jwtUtil.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtUtil.REFRESH_TOKEN_COOKIE_MAX_AGE);
        response.addCookie(cookie);

        dbToken.setToken(refreshToken);
        refreshTokenRepository.save(dbToken);
    }

    /**
     * 패스워드가 일치하는지 검증합니다.
     * @param loginDTO 요청한 로그인 정보
     * @param login 데이터베이스 로그인 정보
     * @exception CustomException 비밀번호 매칭 실패
     */
    private void matchPassword(LoginDTO loginDTO, Login login){
        if(!passwordEncoder.matches(loginDTO.getPassword(), login.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_USER_CREDENTIALS);
        }
    }

    public String createNewAccessToken(HttpServletResponse response, String refreshToken){
        String email = jwtUtil.getEmailFromRefreshToken(refreshToken);

        RefreshToken dbToken = refreshTokenRepository.findByLoginEmail(email).orElseThrow(()->
            new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if(!dbToken.getToken().equals(refreshToken))
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);


        createToken(dbToken, email, response);

        return TOKEN_REFRESH_SUCCESS;
    }
}
