package br.com.conexao.mdfe.api.config;

import br.com.conexao.mdfe.api.service.ibge.EstadoService;
import br.com.conexao.mdfe.api.service.ibge.IbgeService;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.List;

@Configuration
public class FlywayConfig {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private IbgeService ibgeService;

    @Bean
    public boolean migrationPublic(){
        Flyway flyway = new Flyway();
        flyway.setLocations("db/migration/public");
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("1");
        flyway.setTable("schema_version");
        flyway.setSchemas("public");
        flyway.migrate();
        return true;
    }

    @Bean
    public boolean migrationSchemas(){

        List<String> schemas = manager.createNativeQuery(
                "select schema_name from information_schema.schemata where schema_name !~ '^pg_' and schema_name <> 'information_schema' and schema_name <> 'public' ")
                .getResultList();

        if (schemas.size() == 0) {
            schemas.add("mdfe"); //Cria nosso schema padrão para uso de desenvolvimento caso nao tenha nenhum schema
        }

        Flyway flyway = new Flyway();
        flyway.setLocations("db/migration/schemas");
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("1");
        flyway.setTable("schema_version");

        for (String schema : schemas) {
            flyway.setSchemas(schema);
            flyway.migrate();
        }

        sincronizaDadosIbge(); // TODO - Remover chamada daqui e colocar no post construct do MdfeApiApplication.java

        return true;
    }

    // TODO - Remover metodo daqui e colocar em uma classe nova no "util"
    private void sincronizaDadosIbge() {

        try {
            if (estadoService.findAll().size() == 0) {
                ibgeService.sincronizar();
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}





