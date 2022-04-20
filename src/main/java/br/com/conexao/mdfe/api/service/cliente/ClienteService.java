package br.com.conexao.mdfe.api.service.cliente;

import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.grupo.GrupoEnum;
import br.com.conexao.mdfe.api.model.pessoa.PessoaLogin;
import br.com.conexao.mdfe.api.model.pessoa.RetornoCadastroUsuario;
import br.com.conexao.mdfe.api.model.pessoa.TipoUsuario;
import br.com.conexao.mdfe.api.model.pessoa.Usuario;
import br.com.conexao.mdfe.api.resource.usuario.GeradorSenha;
import br.com.conexao.mdfe.api.service.pessoa.PessoaLoginService;
import br.com.conexao.mdfe.api.service.pessoa.PessoaService;
import br.com.conexao.mdfe.api.util.DatabaseSessionManager;
import br.com.conexao.mdfe.api.util.GeradorTenantId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class ClienteService {

    @Autowired
    private DatabaseSessionManager databaseSessionManager;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    private String tenantId;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PessoaLoginService pessoaLoginService;

    @Autowired
    private ValidaErro validaErro;

    private void gerarTenantId() {

        List<String> schemas = new ArrayList<>();

        do {

            tenantId = GeradorTenantId.gerar(8);

            schemas = entityManager.createNativeQuery(
                    "select schema_name from information_schema.schemata where schema_name = ?")
                    .setParameter(1, tenantId)
                    .getResultList();

        } while (schemas.size() > 0);
    }


    public void criarNovoSchema(Usuario usuario, HttpServletRequest request) throws SQLException {

        PessoaLogin pessoaLogin = pessoaLoginService.findByEmail(usuario.getEmail());

        if (nonNull(pessoaLogin)) {
            validaErro.addErro("erro-email-existente-novo-cliente","E-mail já cadastrado na base de dados.");
            validaErro.trataErros();
        }

        gerarTenantId();

        //Executa migração para criar novo schema do cliente
        GerarMigracaoNovoTenacy gerarMigracaoNovoTenacy = new GerarMigracaoNovoTenacy(dataSource);
        gerarMigracaoNovoTenacy.migrar(tenantId);

        //Seta o tenantContext para o tenant criado acima
        databaseSessionManager.alteraTenantDinamico(tenantId);

        Usuario usuario3GBrasil = new Usuario();
        usuario3GBrasil.setEmail(tenantId + "@3gbrasil.com.br");
        usuario3GBrasil.setNome("3G Brasil");
        usuario3GBrasil.setFone("46988019975");
        usuario3GBrasil.setCpf("58237579054");
        usuario3GBrasil.setFlagativo(true);
        usuario3GBrasil.setFlagdel(false);

        //Inserir usuario do 3GBrasil para fazer login na conta do cliente para suporte
        pessoaService.cadastrarNovoUsuario(usuario3GBrasil, tenantId, "#3gbrasil#mdfe#", GrupoEnum.G3BRASIL);


        // Começa a gravar os dados do cliente
        usuario.setFlagativo(true);
        usuario.setFlagdel(false);
        usuario.setTipousuario(TipoUsuario.OPERADOR);

        String senha = GeradorSenha.gerar(6);

        //Inserir o primeiro usuario do cliente
        RetornoCadastroUsuario retorno = pessoaService.cadastrarNovoUsuario(usuario, tenantId, senha, GrupoEnum.ADMINISTRADORES);

        pessoaService.enviarEmailCadastroUsuario(retorno.getSenha(), retorno.getPessoa());

    }

}