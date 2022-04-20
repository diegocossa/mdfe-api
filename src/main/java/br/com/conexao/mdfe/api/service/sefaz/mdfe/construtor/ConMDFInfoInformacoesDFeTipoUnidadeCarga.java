package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.InfUnidCarga;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoInformacoesDFeTipoUnidadeCarga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoInformacoesDFeTipoUnidadeCarga {

    @Autowired
    private ConMDFInfoLacre1A20 conMDFInfoLacre1A20;

    public List<MDFInfoInformacoesDFeTipoUnidadeCarga> convert(List<InfUnidCarga> infUnidCargas){

        if (isNull(infUnidCargas)) {
            return null;
        }

        List<MDFInfoInformacoesDFeTipoUnidadeCarga> listaInformacoes = new ArrayList<>();

        for (InfUnidCarga inf : infUnidCargas) {

            MDFInfoInformacoesDFeTipoUnidadeCarga informacoes = new MDFInfoInformacoesDFeTipoUnidadeCarga();

            informacoes.setIdUnidCarga(trim(inf.getIdunidcarga()));

            if (inf.getLacreunidcargalist() != null) {
                informacoes.setLacUnidCarga(conMDFInfoLacre1A20.convert(inf.getLacreunidcargalist()));
            }

            informacoes.setQtdRateada(String.valueOf(inf.getQtdrateada()));
            informacoes.setTipoUnidadeCarga(inf.getTpunidcarga());
            listaInformacoes.add(informacoes);
        }

        return listaInformacoes;

    }

}
