package br.com.conexao.mdfe.api.tenant;

import br.com.conexao.mdfe.api.token.JwtTokenUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class TenancyInterceptor extends HandlerInterceptorAdapter {

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String tenantId = null;

        //Não pode tentar abrir o token quando for refresh token pois da erro de conversão.
        if (request.getHeader("Authorization") != null
                && !"refresh_token".equalsIgnoreCase(request.getParameter("grant_type"))) {

            //Busca o tenant no header removendo o "Bearer "
            tenantId = JwtTokenUtil.getTenantIdFromToken(request.getHeader("Authorization").substring(7));
        }

        if (!isNull(request.getHeader("EMP"))) {
            request.setAttribute("EMP", request.getHeader("EMP").toString());
        }

        if (!isNull(request.getHeader("EMPL"))) {
            request.setAttribute("EMPL", request.getHeader("EMPL").toString());
        }

        if (!isNull(tenantId)) {
            request.setAttribute("TENANT_ID", tenantId);
        }

        return true;
    }

    public static String getTenantId() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
            return (String) requestAttributes.getAttribute("TENANT_ID", RequestAttributes.SCOPE_REQUEST);
        }

        return null;
    }

    public static Long getEmpId() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes.getAttribute("EMP", RequestAttributes.SCOPE_REQUEST) != null) {
            return Long.valueOf(requestAttributes.getAttribute("EMP", RequestAttributes.SCOPE_REQUEST).toString());
        }

        return null;
    }

    public static List<Long> getListaEmpesasMotorista() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes.getAttribute("EMPL", RequestAttributes.SCOPE_REQUEST) != null) {
            String EMPL = requestAttributes.getAttribute("EMPL", RequestAttributes.SCOPE_REQUEST).toString();

            List<Long> listaEmpresas = Arrays.asList(EMPL.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

            return listaEmpresas;
        }

        return null;
    }

}
