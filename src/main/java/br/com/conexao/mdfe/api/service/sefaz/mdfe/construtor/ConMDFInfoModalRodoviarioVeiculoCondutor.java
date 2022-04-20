package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario.Condutor;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoModalRodoviarioVeiculoCondutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoModalRodoviarioVeiculoCondutor {

    public List<MDFInfoModalRodoviarioVeiculoCondutor> convert(List<Condutor> condutores){

        if (isNull(condutores)){
            return null;
        }

        List<MDFInfoModalRodoviarioVeiculoCondutor> listCondutores = new ArrayList<>();

        for (Condutor condut : condutores) {
            MDFInfoModalRodoviarioVeiculoCondutor condutor = new MDFInfoModalRodoviarioVeiculoCondutor();

            condutor.setCpf(trim(condut.getMotorista().getCpf()));
            condutor.setNomeCondutor(trim(condut.getMotorista().getNome()));
            listCondutores.add(condutor);
        }

        return listCondutores;
    }

}
