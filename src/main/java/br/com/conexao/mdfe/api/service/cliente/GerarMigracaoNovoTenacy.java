package br.com.conexao.mdfe.api.service.cliente;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

public class GerarMigracaoNovoTenacy {

    private DataSource dataSource;

    public GerarMigracaoNovoTenacy(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void migrar(String tenantId){
        Flyway flyway = new Flyway();
        flyway.setLocations("db/migration/schemas");
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("1");
        flyway.setTable("schema_version");
        flyway.setSchemas(tenantId);
        flyway.migrate();
    }

}
