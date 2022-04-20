package br.com.conexao.mdfe.api.token;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        if ("/oauth/token".equalsIgnoreCase(req.getRequestURI())
                && "refresh_token".equalsIgnoreCase(req.getParameter("grant_type"))
                && req.getCookies() != null){

            for (Cookie cookie : req.getCookies()){
                if (cookie.getName().equalsIgnoreCase("refresh_token")){
                    String refreshToken = cookie.getValue();
                    req = new MyServletRequestMapper(req, refreshToken);
                }
            }
        }

        filterChain.doFilter(req, servletResponse);
    }

    @Override
    public void destroy() {

    }

    static class MyServletRequestMapper extends HttpServletRequestWrapper{

        private String refreshToken;

        public MyServletRequestMapper(HttpServletRequest request, String refreshToken) {
            super(request);
            this.refreshToken = refreshToken;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
            map.put("refresh_token",new String [] {this.refreshToken});
            map.setLocked(true);
            return map;
        }
    }
}
