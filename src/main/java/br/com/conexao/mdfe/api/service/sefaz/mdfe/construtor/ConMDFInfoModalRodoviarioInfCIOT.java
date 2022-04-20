package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario.InfCiot;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoModalRodoviarioInfCIOT;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoModalRodoviarioInfCIOT {

    public List<MDFInfoModalRodoviarioInfCIOT> convert(List<InfCiot> ciots){

        if (isNull(ciots)) {
            return null;
        }

        List<MDFInfoModalRodoviarioInfCIOT> listCiot = new ArrayList<>();

        for (InfCiot c : ciots) {
            MDFInfoModalRodoviarioInfCIOT ciot = new MDFInfoModalRodoviarioInfCIOT();

            ciot.setCiot(trim(c.getCiot()));

            if (c.getCnpj() != null) {
                ciot.setCnpj(trim(c.getCnpj()));
            }

            if (c.getCpf() != null) {
                ciot.setCpf(trim(c.getCpf()));
            }

            listCiot.add(ciot);
        }

        return listCiot;
    }

}
