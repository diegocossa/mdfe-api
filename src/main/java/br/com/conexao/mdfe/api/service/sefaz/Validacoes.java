package br.com.conexao.mdfe.api.service.sefaz;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeEvitaConsumoIndevidoException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.certificado.Certificado;
import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.service.empresa.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class Validacoes {

    private static final Logger logger = LoggerFactory.getLogger(Validacoes.class);

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ValidaErro validaErro;

    public void validarEmpresaTenant(Long idEmpresa) {
        if (!isNull(idEmpresa)) {
            if (isNull(empresaService.findByIdEmpresa(idEmpresa))) {
                validaErro.addErro("mdfe.empresa-invalida", "Empresa não encontrada para a empresa " + idEmpresa + ". Método: configurar - Objeto: ConexaoMDFeConfig.java");
            }
        } else
        {
            validaErro.addErro("mdfe.empresa-invalida", "Empresa não encontrada para a empresa " + idEmpresa + ". Método: configurar - Objeto: ConexaoMDFeConfig.java");
        }

        validaErro.trataErros();
    }

    public void validarDadosInternos(Long idEmpresa, Certificado certificado, Empresa empresa) {

        if (isNull(certificado)){
            validaErro.addErro("mdfe.certificado-invalido","Certificado digital não encontrado para a empresa " + idEmpresa +". Método: configurar - Objeto: ConexaoMDFeConfig.java");
        }

        if (isNull(empresa.getTipoambiente())){
            validaErro.addErro("mdfe.ambiente-invalido","Ambiente de operação não informado para a empresa " + idEmpresa +". Método: configurar - Objeto: ConexaoMDFeConfig.java");
        }

        if (isNull(empresa.getUf())){
            validaErro.addErro("mdfe.uf-invalida","Unidade federativa não informada para a empresa " + idEmpresa +". Método: configurar - Objeto: ConexaoMDFeConfig.java");
        }

        validaErro.trataErros();
    }

    public void validarLimiteConsultaRecibo (MDFe mdFe){

        //Função responsável por liberar consultas de Recibo de MDF-e a cada 1 minuto e meio.
        if (nonNull(mdFe.getDtultimaconsultarecibo())) {

            long diff = ChronoUnit.SECONDS.between(mdFe.getDtultimaconsultarecibo(), LocalDateTime.now());

            if (diff < 90) {

                logger.error("Bloqueio ao consultar recibo do MDF-e. Ação para evitar consumo indevido.");

                validaErro.addErro("consumo-indevido", "Para evitar consumo indevido, é permitido a consulta de recibo com intervado de 1,5 minutos", (90-diff));
                throw new MdfeEvitaConsumoIndevidoException(validaErro);
            }
        }
    }
}
