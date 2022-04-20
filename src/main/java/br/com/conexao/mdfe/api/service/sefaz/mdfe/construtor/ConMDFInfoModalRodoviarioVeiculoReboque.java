package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario.VeiculoReboque;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.mdfe3.classes.def.MDFTipoCarroceria;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoModalRodoviarioVeiculoReboque;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoModalRodoviarioVeiculoReboque {

    public List<MDFInfoModalRodoviarioVeiculoReboque> convert(List<VeiculoReboque> reboques) {

        if (isNull(reboques)) {
            return null;
        }

        List<MDFInfoModalRodoviarioVeiculoReboque> listModalReboque = new ArrayList<>();

        for (VeiculoReboque reboq : reboques) {
            MDFInfoModalRodoviarioVeiculoReboque reboque = new MDFInfoModalRodoviarioVeiculoReboque();

            reboque.setCapacidadeKG(String.valueOf(reboq.getVeiculo().getCapacidadekg()));

            if (reboq.getVeiculo().getCapacidadem3() != null) {
                reboque.setCapacidadeM3(String.valueOf(reboq.getVeiculo().getCapacidadem3()));
            }

            if (!reboq.getVeiculo().getRenavam().isEmpty()) {
                reboque.setRenavam(trim(reboq.getVeiculo().getRenavam()));
            }

            reboque.setCodigoInterno(String.valueOf(reboq.getVeiculo().getIdveiculo()));
            reboque.setPlaca(trim(reboq.getVeiculo().getPlaca()));
            reboque.setTara(String.valueOf(reboq.getVeiculo().getTara()));
            reboque.setTipoCarroceria(MDFTipoCarroceria.valueOf(reboq.getVeiculo().getTipocarroceria().name()));
            reboque.setUnidadeFederativa(DFUnidadeFederativa.valueOf(reboq.getVeiculo().getUf().name()));
            listModalReboque.add(reboque);
        }

        return listModalReboque;
    }

}
