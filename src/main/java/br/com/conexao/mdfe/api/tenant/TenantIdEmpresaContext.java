package br.com.conexao.mdfe.api.tenant;

public class TenantIdEmpresaContext {

    final public static Long DEFAULT_TENANT = null;

    private static ThreadLocal<Long> empresaCorrente = new ThreadLocal<Long>()
    {
        @Override
        protected Long initialValue() {
            return DEFAULT_TENANT;
        }
    };

    public static void setCurrentTenant(Long idEmpresa) {
        empresaCorrente.set(idEmpresa);
    }

    public static Long getCurrentTenant() {
        return empresaCorrente.get();
    }

    public static void clear() {
        empresaCorrente.remove();
    }

}