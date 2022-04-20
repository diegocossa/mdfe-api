package br.com.conexao.mdfe.api.tenant.hibernate;

import java.util.List;

public class TenantListEmpresasContext {

    final public static Long DEFAULT_TENANT = null;

    private static ThreadLocal<List<Long>> listaEmpresas = new ThreadLocal<List<Long>>()
    {
        @Override
        protected List<Long> initialValue() {
            return null;
        }
    };

    public static void setCurrentList(List<Long> listIdEmpresa) {
        listaEmpresas.set(listIdEmpresa);
    }

    public static List<Long> getCurrentList() {
        return listaEmpresas.get();
    }

    public static void clear() {
        listaEmpresas.remove();
    }

}