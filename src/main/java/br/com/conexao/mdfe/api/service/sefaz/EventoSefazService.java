package br.com.conexao.mdfe.api.service.sefaz;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.mdfe.NovoCondutor;
import br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario.Condutor;
import br.com.conexao.mdfe.api.model.motorista.Motorista;
import br.com.conexao.mdfe.api.service.mdfe.MdfeLancamentoService;
import br.com.conexao.mdfe.api.service.motorista.MotoristaService;
import br.com.conexao.mdfe.api.service.sefaz.mdfe.execute.IncluirCondutor;
import br.com.conexao.mdfe.api.util.ValidarCpfCnpj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Service
public class EventoSefazService {

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private MdfeLancamentoService mdfeLancamentoService;

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private IncluirCondutor incluirCondutor;

    public MDFe prepararCondutorEEnviarEvento(@PathVariable Long idmdfe, @PathVariable Long idmotorista) {
        MDFe rodov = mdfeLancamentoService.findByIdMdfe(idmdfe);
        Motorista motorista = motoristaService.findByIdMotorista(idmotorista);
        Condutor novoCondutor = new Condutor();
        novoCondutor.setMotorista(motorista);
        novoCondutor.setMdfe(rodov);

        if (!rodov.getCondutorlist().contains(novoCondutor) && (rodov.getCondutorlist().size() < 3)) {
            rodov.getCondutorlist().add(novoCondutor);
            Map<String, String> retornoConsulta = incluirCondutor.incluir(rodov, novoCondutor);
        } else {
            validaErro.addErro("erro.motorista-ja-incluso", "O motorista já está contido" +
                    "na lista de motoristas deste manifesto.");
            throw new MdfeException(validaErro);
        }
        return rodov;
    }

    public void validarCPFCondutor(NovoCondutor condutor) {

        if (!ValidarCpfCnpj.isValidCPF(condutor.getCpf())) {
            validaErro.addErro("cpf.invalido", "Método: incluirCondutor - Objeto: EventoSefazResource.java ");
            throw new MdfeException(validaErro);
        }

    }

}
