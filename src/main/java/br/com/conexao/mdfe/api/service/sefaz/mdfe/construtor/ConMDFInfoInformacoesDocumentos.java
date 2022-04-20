package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.InfMunDescarga;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoInformacoesDocumentos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ConMDFInfoInformacoesDocumentos {

    @Autowired
    private ConMDFInfoInformacoesMunicipioDescarga conMDFInfoInformacoesMunicipioDescarga;

    public MDFInfoInformacoesDocumentos convert(List<InfMunDescarga> descargas){

        if (isNull(descargas)) {
            return null;
        }

        MDFInfoInformacoesDocumentos documentos = new MDFInfoInformacoesDocumentos();

        if (descargas != null) {
            documentos.setInformacoesMunicipioDescargas(conMDFInfoInformacoesMunicipioDescarga.convert(descargas));
        }

        return documentos;
    }
}
