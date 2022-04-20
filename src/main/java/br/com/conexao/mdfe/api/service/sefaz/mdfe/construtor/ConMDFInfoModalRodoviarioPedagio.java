package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario.ValePedagio;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoModalRodoviarioPedagio;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoModalRodoviarioPedagioDisp;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoModalRodoviarioPedagio {

    public MDFInfoModalRodoviarioPedagio convert(List<ValePedagio> valePeds){

        if (isNull(valePeds)){

            return null;
        }

        MDFInfoModalRodoviarioPedagio modalPedagio = new MDFInfoModalRodoviarioPedagio();

        List<MDFInfoModalRodoviarioPedagioDisp> dispositivos = new ArrayList<>();

        for (ValePedagio ped : valePeds) {
            MDFInfoModalRodoviarioPedagioDisp disp = new MDFInfoModalRodoviarioPedagioDisp();

            disp.setCnpjFornecedora(trim(ped.getCnpjforn()));

            if (ped.getCnpjpg() != null) {
                disp.setCnpjPagadora(trim(ped.getCnpjpg()));
            }

            if (ped.getCpfpg() != null) {
                disp.setCpfPagadora(trim(ped.getCpfpg()));
            }

            disp.setNumeroComprovante(trim(ped.getNcompra()));
            disp.setValor(ped.getVvaleped());
            dispositivos.add(disp);
        }

        modalPedagio.setDispositivos(dispositivos);

        return modalPedagio;
    }
}
