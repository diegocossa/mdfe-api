package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.AutorizadosXml;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoAutorizacaoDownload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoAutorizacaoDownload {

    public List<MDFInfoAutorizacaoDownload> convert(List<AutorizadosXml> auts){

        if (isNull(auts)) {
            return null;
        }

        List<MDFInfoAutorizacaoDownload> listaAutorizacoes = new ArrayList<>();

        for (AutorizadosXml aut : auts) {

            MDFInfoAutorizacaoDownload autorizacao = new MDFInfoAutorizacaoDownload();

            if (aut.getCnpj() != null) {
                autorizacao.setCnpj(trim(aut.getCnpj()));
            }

            if (aut.getCpf() != null) {
                autorizacao.setCpf(trim(aut.getCpf()));
            }

            listaAutorizacoes.add(autorizacao);
        }

        return listaAutorizacoes;

    }

}
