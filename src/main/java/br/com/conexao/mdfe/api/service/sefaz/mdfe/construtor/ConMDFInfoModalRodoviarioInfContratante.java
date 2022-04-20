package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario.InfContratante;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoModalRodoviarioInfContratante;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoModalRodoviarioInfContratante {

    public List<MDFInfoModalRodoviarioInfContratante> convert(List<InfContratante> infContratantes){

        if (isNull(infContratantes)) {
            return null;
        }

        List<MDFInfoModalRodoviarioInfContratante> listContratante = new ArrayList<>();

        for (InfContratante info : infContratantes) {
            MDFInfoModalRodoviarioInfContratante inf = new MDFInfoModalRodoviarioInfContratante();

            if (info.getCnpj() != null) {
                inf.setCnpj(trim(info.getCnpj()));
            }

            if (info.getCpf() != null) {
                inf.setCpf(trim(info.getCpf()));
            }

            listContratante.add(inf);
        }

        return listContratante;

    }

}
