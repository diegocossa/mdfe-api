package br.com.conexao.mdfe.api.service.certificado;

import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.certificado.Certificado;
import br.com.conexao.mdfe.api.repository.certificado.CertificadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CertificadoService {

    @Autowired
    private CertificadoRepository certificadoRepository;

    @Autowired
    private ValidaErro validaErro;

    @Transactional
    public void deletar(Long idcertificado) {
        certificadoRepository.delete(idcertificado);
    }

    @Transactional
    public Certificado salvar(MultipartFile file, String senha) {

        LocalDate dataInicio = null;
        LocalDate dataFim = null;

        CertificadoDataInicioFimValidade getDataInicioFimValidade
                = new CertificadoDataInicioFimValidade(file, senha, dataInicio, dataFim).invoke();

        dataInicio = getDataInicioFimValidade.getDataInicio();
        dataFim = getDataInicioFimValidade.getDataFim();

        validacoes(dataInicio, dataFim);//valida as informações
        validaErro.trataErros();//dispara exceptions caso tenha erro

        try {

            Certificado certificado = new Certificado();
            certificado.setSenha(senha);
            certificado.setArquivo(file.getBytes());
            certificado.setDtiniciovalidade(dataInicio);
            certificado.setDtfimvalidade(dataFim);

            return certificadoRepository.save(certificado);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Certificado findByIdCertificado(Long idcertificado) {
        return certificadoRepository.findByIdcertificado(idcertificado);
    }

    public Certificado findByIdempresa(Long idempresa) {
        return certificadoRepository.findByIdcertificado(certificadoRepository.buscarUltimoCertificadoInserido(idempresa));
    }

    public Certificado findUltimoCertificadoInserido(Long idempresa){
        return findByIdCertificado(certificadoRepository.buscarUltimoCertificadoInserido(idempresa));
    }

    private void validacoes(LocalDate dataInicio, LocalDate dataFim) {

        if (dataInicio == null) {
            validaErro.addErro("senha.certificado.invalida",
                    "Senha do certificado inválida, não foi possível válidar o certificado.");
        } else {
            if (LocalDate.now().isAfter(dataFim)) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                String dataFormatada = dataFim.format(formatter);

                validaErro.addErro("certificado.expirado",
                        "Certificado expirado em :" + dataFormatada, dataFormatada);
            }
        }
    }
}
