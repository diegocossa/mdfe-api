package br.com.conexao.mdfe.api.config;

import br.com.conexao.mdfe.api.config.property.MdfeProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter{

    @Autowired
    private MdfeProperty mdfeProperty;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        res.setHeader("Access-Control-Allow-Credentials","true");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())
                && origemPermitida(res, req)) {
            res.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
            res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, EMP");
            res.setHeader("Access-Control-Max-Age", "3600");

            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {

    }

    private Boolean origemPermitida(HttpServletResponse res, HttpServletRequest req) {
        String[] origins = mdfeProperty.getSeguranca().getOrigemPermitida();
        Boolean _originAccepted;
        /* Implementação para aceitar multiplas origens. */
        for (String origin : origins) {
            _originAccepted = origin.equals(req.getHeader("Origin"));

            if (_originAccepted) {
                return true;
            }
        }
        return false;
    }
}
