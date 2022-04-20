package br.com.conexao.mdfe.api;

import br.com.conexao.mdfe.api.async.GeradorCadeiaCertificadoService;
import br.com.conexao.mdfe.api.config.property.MdfeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(value = {"br.com.conexao.mdfe.api.repository"}, enableDefaultTransactions = false)
@EnableAspectJAutoProxy
@EnableConfigurationProperties({MdfeProperty.class})
public class MdfeApiApplication {

	private static final Logger logger = LoggerFactory.getLogger(MdfeApiApplication.class);

	@Autowired
	private MdfeProperty mdfeProperty;

	@Autowired
	private GeradorCadeiaCertificadoService geradorCadeiaCertificado;

	public static void main(String[] args) {
		SpringApplication.run(MdfeApiApplication.class, args);
	}

	//Após construir em produção deverá gerar os CACerts
	@PostConstruct
	public void postConstruct() {
		//Setar o timezone para não ocorrer problemas de getTimezoneDefault da Fincatto
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

		//Executar os cacerts sempre que iniciar pois como fica dentro do docker, toda vez que iniciar deve gerar o arquivo.
		try
		{
			if (mdfeProperty.getCaCerts().getGerarCacerts()) {
				geradorCadeiaCertificado.gerarCadeiaProducao();
				geradorCadeiaCertificado.gerarCadeiaHomologacao();

				logger.info("Arquivos de cacerts atualizados com sucesso!");
			}
		} catch (Exception e){

			logger.error("Erro ao atualizar cacerts. Exception: " + e.getMessage());

		}
	}
}
