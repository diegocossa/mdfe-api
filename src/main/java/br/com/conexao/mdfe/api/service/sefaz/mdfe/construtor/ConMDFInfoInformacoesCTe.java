package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.InfDoc;
import com.fincatto.documentofiscal.mdfe3.classes.nota.InfoEntregaParcial;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoInformacoesCTe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class ConMDFInfoInformacoesCTe {

    @Autowired
    private ConMDFInfoPerigosos conMDFInfoPerigosos;

    @Autowired
    private ConMDFInfoInformacoesDFeTipoUnidadeTransporte conMDFInfoInformacoesDFeTipoUnidadeTransporte;

    public List<MDFInfoInformacoesCTe> convert(List<InfDoc> infCTes) {

        if (isNull(infCTes)) {
            return null;
        }

        List<MDFInfoInformacoesCTe> listaCtes = new ArrayList<>();

        for (InfDoc cte : infCTes) {

            MDFInfoInformacoesCTe ctes = new MDFInfoInformacoesCTe();

            ctes.setChaveCTe(cte.getChavedoc());
            /* ctes.setIndicadorReentrega(cte.getIndReentrega());*/ //não implementado por ser opcional

            if (cte.getInfunidtransplist() != null) {
                ctes.setInformacoesUnidadeTransporte(conMDFInfoInformacoesDFeTipoUnidadeTransporte.convert(cte.getInfunidtransplist()));
            }

            if (cte.getPericulosidadelist() != null) {
                ctes.setPerigosos(conMDFInfoPerigosos.convert(cte.getPericulosidadelist()));
            }

            if (nonNull(cte.getSegcodbarra())) {
                if (!cte.getSegcodbarra().isEmpty()) {
                    ctes.setSegCodBarra(trim(cte.getSegcodbarra()));
                }
            }

            if ((nonNull(cte.getQtdparcial()) || (nonNull(cte.getQtdtotal())))) {

                InfoEntregaParcial parcial = new InfoEntregaParcial();
                if (nonNull(cte.getQtdparcial())) {
                    parcial.setQtdParcial(String.valueOf(cte.getQtdparcial()));
                }

                if (nonNull(cte.getQtdtotal())) {
                    parcial.setQtdTotal(String.valueOf(cte.getQtdtotal()));
                }

                ctes.setInfoEntregaParcial(parcial);
            }

            listaCtes.add(ctes);
        }

        return listaCtes;
    }

}
