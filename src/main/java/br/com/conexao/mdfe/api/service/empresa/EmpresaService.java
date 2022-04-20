package br.com.conexao.mdfe.api.service.empresa;

import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.repository.empresa.EmpresaRepository;
import br.com.conexao.mdfe.api.util.ValidarCpfCnpj;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private SequenceNumMdfService sequenceNumMdfService;

    @Autowired
    private ValidaErro validaErro;

    public List<Empresa> findAll() {
        return empresaRepository.findAllByFlagdelIsFalse();
    }

    public Empresa findByIdEmpresa(Long idempresa) {
        return empresaRepository.findByIdempresaAndFlagdelIsFalse(idempresa);
    }

    @Transactional
    public Empresa atualizar(Long idempresa, Empresa empresa) {
        Empresa empresaSalva = buscarEmpresaPeloId(idempresa);

        validar(empresa);

        BeanUtils.copyProperties(empresa, empresaSalva, "idempresa");

        return empresaSalva;
    }

    @Transactional
    public Empresa salvar(Empresa empresa) {

        validar(empresa);

        Long idEmpresa = empresa.getIdempresa();

        Empresa empresaSalva = empresaRepository.save(empresa);

        if (isNull(idEmpresa)) {
            sequenceNumMdfService.criarSequenceNumMdf(empresaSalva);
        }

        return empresaSalva;
    }

    private void validar(Empresa empresa) {
        validarCNPJ(empresa.getCnpj());

        validaErro.trataErros();
    }

    @Transactional
    public void deletar(Long idempresa) {
        Empresa empresa = buscarEmpresaPeloId(idempresa);
        empresa.setFlagdel(true);
        this.salvar(empresa);
    }

    private Empresa buscarEmpresaPeloId(Long idempresa) {
        Empresa empresaSalva = findByIdEmpresa(idempresa);
        if (isNull(empresaSalva)) {
            throw new EmptyResultDataAccessException(1);
        }
        return empresaSalva;
    }

    private void validarCNPJ(String cnpj) {
        if (!ValidarCpfCnpj.isValidCNPJ(cnpj)) {
            validaErro.addErro("cnpj.invalido",
                    "Método: validarCNPJ / Classe: EmpresaService.java");
        }
    }

}
