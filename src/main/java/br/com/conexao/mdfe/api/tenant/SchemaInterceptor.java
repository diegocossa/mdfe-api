package br.com.conexao.mdfe.api.tenant;

import br.com.conexao.mdfe.api.tenant.hibernate.TenantListEmpresasContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class SchemaInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {

        //Dados do schema

        String tenantSession = TenancyInterceptor.getTenantId();

        if (tenantSession != null) {
            TenantContext.setCurrentTenant(tenantSession);
        } else {
            TenantContext.setCurrentTenant("public");
        }

        //Dados da empresa

        Long tenantIdEmpresa = TenancyInterceptor.getEmpId();

        if (tenantIdEmpresa != null) {
            TenantIdEmpresaContext.setCurrentTenant(tenantIdEmpresa);
        } else {
            TenantIdEmpresaContext.setCurrentTenant(null);
        }

        //Dados da lista de empresas dos motoristas

        List<Long> listEmpresa = TenancyInterceptor.getListaEmpesasMotorista();

        if (listEmpresa != null) {
            TenantListEmpresasContext.setCurrentList(listEmpresa);
        } else {
            TenantListEmpresasContext.setCurrentList(null);
        }

        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        TenantContext.clear();
    }
}
