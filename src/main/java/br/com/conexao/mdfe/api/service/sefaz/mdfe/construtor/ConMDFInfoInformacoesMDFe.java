package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.InfDoc;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoInformacoesMDFe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoInformacoesMDFe {

    @Autowired
    private ConMDFInfoPerigosos conMDFInfoPerigosos;

    @Autowired
    private ConMDFInfoInformacoesDFeTipoUnidadeTransporte conMDFInfoInformacoesDFeTipoUnidadeTransporte;

    public List<MDFInfoInformacoesMDFe> convert(List<InfDoc> transps) {

        if (isNull(transps)) {
            return null;
        }

        List<MDFInfoInformacoesMDFe> listaMdfeTransp = new ArrayList<>();

        for (InfDoc mdf : transps) {

            MDFInfoInformacoesMDFe transportes = new MDFInfoInformacoesMDFe();

            transportes.setChaveMDFe(trim(mdf.getChavedoc()));
            //transportes.setIndicadorReentrega(mdf.getIndReentrega()); não implementado é opcional

            if (mdf.getInfunidtransplist() != null) {
                transportes.setInformacoesUnidadeTransporte(conMDFInfoInformacoesDFeTipoUnidadeTransporte.convert(mdf.getInfunidtransplist()));
            }

            if (mdf.getPericulosidadelist() != null) {
                transportes.setPerigosos(conMDFInfoPerigosos.convert(mdf.getPericulosidadelist()));
            }

            listaMdfeTransp.add(transportes);
        }

        return listaMdfeTransp;

    }

}
