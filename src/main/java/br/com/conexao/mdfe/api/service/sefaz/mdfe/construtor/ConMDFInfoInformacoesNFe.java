package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.InfDoc;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoInformacoesNFe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class ConMDFInfoInformacoesNFe {

    @Autowired
    private ConMDFInfoPerigosos conMDFInfoPerigosos;

    @Autowired
    private ConMDFInfoInformacoesDFeTipoUnidadeTransporte conMDFInfoInformacoesDFeTipoUnidadeTransporte;

    public List<MDFInfoInformacoesNFe> convert(List<InfDoc> nfes){

        if (isNull(nfes)) {
            return null;
        }

        List<MDFInfoInformacoesNFe> listaNfeTransp = new ArrayList<>();

        for (InfDoc nfe : nfes) {
            MDFInfoInformacoesNFe infNfes = new MDFInfoInformacoesNFe();

            infNfes.setChaveNFe(trim(nfe.getChavedoc()));

            //não implementado
            /*if (nfe.getIndReentrega() != null){
                infNfes.setIndicadorReentrega(nfe.getIndReentrega());
            }*/

            if (nfe.getInfunidtransplist() != null) {
                infNfes.setInformacoesUnidadeTransporte(
                        conMDFInfoInformacoesDFeTipoUnidadeTransporte.convert(nfe.getInfunidtransplist()));
            }

            if (nfe.getPericulosidadelist() != null) {
                infNfes.setPerigosos(conMDFInfoPerigosos.convert(nfe.getPericulosidadelist()));
            }

            if (nonNull(nfe.getSegcodbarra())) {
                if (!nfe.getSegcodbarra().isEmpty()) {
                    infNfes.setSegCodBarra(trim(nfe.getSegcodbarra()));
                }
            }

            listaNfeTransp.add(infNfes);
        }

        return listaNfeTransp;

    }

}
