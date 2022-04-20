package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.InfUnidTransp;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoInformacoesDFeTipoUnidadeTransporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoInformacoesDFeTipoUnidadeTransporte {

    @Autowired
    private ConMDFInfoLacre1A20 conMDFInfoLacre1A20;

    @Autowired
    private ConMDFInfoInformacoesDFeTipoUnidadeCarga conMDFInfoInformacoesDFeTipoUnidadeCarga;

    public List<MDFInfoInformacoesDFeTipoUnidadeTransporte> convert(List<InfUnidTransp> infUnidTransps){

        if (isNull(infUnidTransps)) {
            return null;
        }

        List<MDFInfoInformacoesDFeTipoUnidadeTransporte> listaUnidTrans = new ArrayList<>();

        for (InfUnidTransp unid : infUnidTransps) {

            MDFInfoInformacoesDFeTipoUnidadeTransporte unidTrans = new MDFInfoInformacoesDFeTipoUnidadeTransporte();

            unidTrans.setIdUnidTransp(trim(unid.getIdunidtransp()));

            if (unid.getInfunidcargalist() != null) {
                unidTrans.setInfUnidCarga(conMDFInfoInformacoesDFeTipoUnidadeCarga.convert(unid.getInfunidcargalist()));
            }

            if (unid.getLacreunidtransplist() != null) {
                unidTrans.setLacUnidTransp(conMDFInfoLacre1A20.convert(unid.getLacreunidtransplist()));
            }

            unidTrans.setQtdRateada(String.valueOf(unid.getQtdrateada()));
            unidTrans.setTipoUnidadeTransporte(unid.getTpunidtransp());
            listaUnidTrans.add(unidTrans);
        }

        return listaUnidTrans;

    }

}
