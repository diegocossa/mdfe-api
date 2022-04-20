package br.com.conexao.mdfe.api.service.motorista;

import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.grupo.Grupo;
import br.com.conexao.mdfe.api.model.grupo.GrupoEnum;
import br.com.conexao.mdfe.api.model.motorista.Motorista;
import br.com.conexao.mdfe.api.model.pessoa.Pessoa;
import br.com.conexao.mdfe.api.model.pessoa.PessoaLogin;
import br.com.conexao.mdfe.api.model.pessoa.TipoUsuario;
import br.com.conexao.mdfe.api.repository.motorista.MotoristaRepository;
import br.com.conexao.mdfe.api.resource.usuario.GeradorSenha;
import br.com.conexao.mdfe.api.service.grupo.GrupoService;
import br.com.conexao.mdfe.api.service.pessoa.PessoaLoginService;
import br.com.conexao.mdfe.api.service.pessoa.PessoaService;
import br.com.conexao.mdfe.api.tenant.TenantContext;
import br.com.conexao.mdfe.api.util.ValidarCpfCnpj;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class MotoristaService {

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PessoaLoginService pessoaLoginService;

    @Autowired
    private MotoristaService motoristaService;


    public List<Motorista> findAll() {
        return motoristaRepository.findAllByFlagdelIsFalse();
    }

    public Motorista findByIdMotorista(Long idmotorista) {
        return motoristaRepository.findByIdmotoristaAndFlagdelIsFalse(idmotorista);
    }

    public Motorista findByCpfAtivo(String cpf){
        return motoristaRepository.findByCpfAndFlagdelIsFalse(cpf);
    }

    @Transactional
    public Motorista atualizar(Long idmotorista, Motorista motorista) {
        Motorista motoristaSalvo = buscarMotoristaPeloId(idmotorista);

        validar(motorista);

        BeanUtils.copyProperties(motorista, motoristaSalvo, "idmotorista", "pessoa");

        atualizarPessoaLogin(motoristaSalvo);

        return motoristaSalvo;
    }

    public void atualizarPropriedadeFlagAtivo(Long idmotorista, Boolean flagativo) {
        Motorista motoristaSalvo = buscarMotoristaPeloId(idmotorista);
        motoristaSalvo.setFlagativo(flagativo);
        this.salvar(motoristaSalvo);
    }

    @Transactional
    public Motorista salvar(Motorista motorista) {

        //Caso cadastre um motorista com CPF que ja estava cadastrado porém foi deletado, o sistema só reativa o registro.
        Motorista motoristaCadastrado = motoristaRepository.findByCpf(motorista.getCpf());
        if (nonNull(motoristaCadastrado)) {
            reativarMotorista(motorista, motoristaCadastrado);
            return motoristaCadastrado;
        }

        validar(motorista);

        Pessoa pessoa = new Pessoa();
        alimentarObjetoPessoa(motorista, pessoa);

        Pessoa pessoaSalvo = pessoaService.salvar(pessoa);

        PessoaLogin pessoaLogin = new PessoaLogin();
        pessoaLogin.setIdpessoatenant(pessoaSalvo.getIdpessoa());
        BeanUtils.copyProperties(pessoaSalvo, pessoaLogin, "idpessoa");

        pessoaLoginService.salvar(pessoaLogin);//vai salvar no schema public

        motorista.setPessoa(pessoaSalvo);

        return motoristaRepository.save(motorista);
    }

    private void alimentarObjetoPessoa(Motorista motorista, Pessoa pessoa) {

        pessoa.setCpf(motorista.getCpf());
        pessoa.setEmail(motorista.getCpf() + "@3gbrasil.com.br");
        pessoa.setFlagativo(motorista.getFlagativo());
        pessoa.setNome(motorista.getNome());
        pessoa.setTipousuario(TipoUsuario.MOTORISTA);
        pessoa.setFone(motorista.getTelefone());
        pessoa.setIdempresapadrao(1l); //TODO - Verificar para informar da tela a empresa padrão
        pessoa.setTenantid(TenantContext.getCurrentTenant());

        //Quando for cadastrado o motorista gera uma senha aleatória para que crie um usuário normalmente.
        //Caso o motorista precise acessar o sistema, teremos que alterar e passar a senha para o mesmo.
        String senha = GeradorSenha.gerar(6);
        pessoa.setSenhausuario(new BCryptPasswordEncoder().encode(senha));

        Grupo grupo = grupoService.findByIdGrupo(GrupoEnum.MOTORISTAS.getCodigo());
        pessoa.addGrupo(grupo);
    }

    @Transactional
    public void deletar(Long idmotorista) {
        Motorista motorista = motoristaService.buscarMotoristaPeloId(idmotorista);

        if (motorista != null) {

            Pessoa pessoa = motorista.getPessoa();
            pessoa.setFlagdel(true);
            pessoa.setFlagativo(false);
            pessoaService.salvar(pessoa);//marca como deletado

            PessoaLogin pessoaLogin = pessoaLoginService.findByEmail(pessoa.getEmail());

            if (pessoaLogin != null) {
                pessoaLogin.setFlagdel(true);
                pessoaLogin.setFlagativo(false);
                pessoaLoginService.salvar(pessoaLogin);
            }

            motorista.setFlagdel(true);
            motorista.setFlagativo(false);
            motoristaRepository.save(motorista);
        }
    }

    private Motorista buscarMotoristaPeloId(Long idmotorista) {
        Motorista motoristaSalvo = findByIdMotorista(idmotorista);
        if (isNull(motoristaSalvo)) {
            throw new EmptyResultDataAccessException(1);
        }
        return motoristaSalvo;
    }

    private void validarCPF(Motorista motorista) {
        if (!ValidarCpfCnpj.isValidCPF(motorista.getCpf())) {
            validaErro.addErro("cpf.invalido",
                    "Método: validarCPF / Classe: MotoristaService.java");
        }


        if (cpfJaExistente(motorista)) {
            validaErro.addErro("cpf.existente",
                    "Método: validarCPF / Classe: MotoristaService.java");
        }
    }

    private boolean cpfJaExistente(Motorista motorista) {

        if (motorista.novoMotorista()) {
            Motorista motoristaSalvo = findByCpfAtivo(motorista.getCpf());
            return nonNull(motoristaSalvo);
        } else {
            Motorista motoristaPesquisa = findByIdMotorista(motorista.getIdmotorista());
            if (!motorista.getCpf().equals(motoristaPesquisa.getCpf())) {
                return !motoristaRepository.findAllByCpf(motorista.getCpf()).isEmpty();
            }

            return false;
        }
    }

    private void validar(Motorista motorista){
        validarCPF(motorista);

        validaErro.trataErros();
    }

    @Transactional
    public void atualizarPessoaLogin(Motorista motoristaSalvo){
        PessoaLogin pessoaLogin = pessoaLoginService.findByEmail(motoristaSalvo.getPessoa().getEmail());
        Pessoa pessoa = motoristaSalvo.getPessoa();

        //setar novo email de acordo com o cpf
        pessoa.setEmail(motoristaSalvo.getCpf() + "@3gbrasil.com.br");

        BeanUtils.copyProperties(motoristaSalvo, pessoa, "idpessoa", "idpessoatenant", "tipousuario", "tenantid");
        BeanUtils.copyProperties(pessoa, pessoaLogin, "idpessoa");

        pessoaLogin.setIdpessoatenant(pessoa.getIdpessoa());

        pessoaService.salvar(pessoa);
        pessoaLoginService.salvar(pessoaLogin);
    }

    private void reativarMotorista(Motorista novoMotorista, Motorista motoristaCadastrado){
        validar(novoMotorista);

        novoMotorista.setIdmotorista(motoristaCadastrado.getIdmotorista());
        novoMotorista.setFlagdel(false);

        BeanUtils.copyProperties(novoMotorista, motoristaCadastrado, "idmotorista", "pessoa");

        atualizarPessoaLogin(motoristaCadastrado);

    }

}
