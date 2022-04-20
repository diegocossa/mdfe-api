package br.com.conexao.mdfe.api.service.configuracao;

import br.com.conexao.mdfe.api.model.configuracao.Configuracao;
import br.com.conexao.mdfe.api.repository.configuracao.ConfiguracaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ConfiguracaoService {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Transactional
    public Configuracao salvar(Configuracao configuracao) {
        return configuracaoRepository.save(configuracao);
    }

    @Transactional
    public void deletar(Long idconfiguracao) {
        configuracaoRepository.delete(idconfiguracao);
    }

    @Transactional
    public Configuracao atualizar(Long idconfiguracao, Configuracao configuracaoSalvar) {
        Configuracao configuracao = findByIdconfiguracao(idconfiguracao);
        BeanUtils.copyProperties(configuracaoSalvar, configuracao, "idconfiguracao");
        return configuracao;
    }

    public Configuracao findByIdconfiguracao(Long idconfiguracao) {
        Configuracao configuracao = configuracaoRepository.findByIdconfiguracao(idconfiguracao);
        if (isNull(configuracao)) {
            throw new EmptyResultDataAccessException(1);
        }
        return configuracao;
    }

    public List<Configuracao> findAll() {
        return configuracaoRepository.findAllByChaveNotNull();
    }

    public List<Configuracao> buscarPelaEmpresa(Long idempresa) {
        return configuracaoRepository.findAllByIdempresa(idempresa);
    }
}
