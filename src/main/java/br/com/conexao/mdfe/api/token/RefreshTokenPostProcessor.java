package br.com.conexao.mdfe.api.token;

import br.com.conexao.mdfe.api.config.property.MdfeProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{

    @Autowired
    private MdfeProperty mdfeProperty;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.getMethod().getName().equals("postAccessToken");
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken oAuth2AccessToken, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String refreshToken=  oAuth2AccessToken.getRefreshToken().getValue();

        //Converte o request e response para usar na adicionarRefreshTokenNoCookie()
        HttpServletRequest req = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        HttpServletResponse res = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();

        //Converte o token para usar o setRefreshToken na removerRefreshTokenBody()
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) oAuth2AccessToken;

        adicionarRefreshTokenNoCookie(refreshToken, req, res);
        removerRefreshTokenBody(token);

        return oAuth2AccessToken;
    }

    private void removerRefreshTokenBody(DefaultOAuth2AccessToken token) {
        token.setRefreshToken(null); //remover o refresh token do body deixando disponível somente no cookie.
    }

    private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse res) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true); //Somente no HTTP
        refreshTokenCookie.setSecure(mdfeProperty.getSeguranca().isEnableHttps()); //Adiciona Cookie no HTTPS
        refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
        refreshTokenCookie.setMaxAge(2592000); // Cookie expira em 30 dias.
        res.addCookie(refreshTokenCookie);
    }
}
