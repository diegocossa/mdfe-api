package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.InfDoc;
import br.com.conexao.mdfe.api.model.mdfe.generica.InfMunDescarga;
import br.com.conexao.mdfe.api.model.mdfe.generica.TipoDocEnum;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoInformacoesMunicipioDescarga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoInformacoesMunicipioDescarga {

    @Autowired
    private ConMDFInfoInformacoesCTe conMDFInfoInformacoesCTe;

    @Autowired
    private ConMDFInfoInformacoesMDFe conMDFInfoInformacoesMDFe;

    @Autowired
    private ConMDFInfoInformacoesNFe conMDFInfoInformacoesNFe;


    public List<MDFInfoInformacoesMunicipioDescarga> convert(List<InfMunDescarga> descargas) {

        if (isNull(descargas)) {
            return null;
        }

        List<MDFInfoInformacoesMunicipioDescarga> listaInfMunDesc = new ArrayList<>();

        List<InfDoc> docsList;

        for (InfMunDescarga mun : descargas) {

            MDFInfoInformacoesMunicipioDescarga munDescarga = new MDFInfoInformacoesMunicipioDescarga();

            if (mun.getInfdoclist() != null) {

                docsList = mun.getInfdoclist().stream()
                        .filter(infDoc -> TipoDocEnum.CTE.equals(infDoc.getTipodoc())).collect(Collectors.toList());

                if (!docsList.isEmpty()) {
                    munDescarga.setInfCTe(conMDFInfoInformacoesCTe.convert(docsList));
                }
            }

            if (mun.getInfdoclist() != null) {

                docsList = mun.getInfdoclist().stream()
                        .filter(infDoc -> TipoDocEnum.MDFE.equals(infDoc.getTipodoc())).collect(Collectors.toList());

                if (!docsList.isEmpty()) {
                    munDescarga.setInfMDFeTransp(conMDFInfoInformacoesMDFe.convert(docsList));
                }
            }

            if (mun.getInfdoclist() != null) {

                docsList = mun.getInfdoclist().stream()
                        .filter(infDoc -> TipoDocEnum.NFE.equals(infDoc.getTipodoc())).collect(Collectors.toList());

                if (!docsList.isEmpty()) {
                    munDescarga.setInfNFe(conMDFInfoInformacoesNFe.convert(docsList));
                }
            }

            munDescarga.setMunicipioDescarga(trim(mun.getCmundescarga()));
            munDescarga.setxMunDescarga(trim(mun.getXmundescarga()));
            listaInfMunDesc.add(munDescarga);
        }

        return listaInfMunDesc;

    }

}
