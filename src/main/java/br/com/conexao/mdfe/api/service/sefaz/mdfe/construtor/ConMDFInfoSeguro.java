package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.Seg;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoSeguro;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoSeguroInfo;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoSeguroResponsavel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoSeguro {

    public List<MDFInfoSeguro> convert(List<Seg> seguros) {

        if (isNull(seguros)) {
            return null;
        }

        List<MDFInfoSeguro> listaSeguros = new ArrayList<>();

        MDFInfoSeguro seguro = new MDFInfoSeguro();

        for (Seg seg : seguros) {
            MDFInfoSeguroResponsavel mdfInfoSeguroResponsavel = new MDFInfoSeguroResponsavel();

            seguro.setApolice(trim(seg.getSeguradora().getNumeroapolice()));
            seguro.setAverbacao(new ArrayList<>(seg.getSegnumeroaverbacaolist()));

            if (trim(seg.getSeguradora().getCpfcnpjresponsavel()) != null && trim(seg.getSeguradora().getCpfcnpjresponsavel()).length() == 14) {
                mdfInfoSeguroResponsavel.setCnpj(trim(seg.getSeguradora().getCpfcnpjresponsavel()));
            }

            if (trim(seg.getSeguradora().getCpfcnpjresponsavel()) != null && trim(seg.getSeguradora().getCpfcnpjresponsavel()).length() == 11) {
                mdfInfoSeguroResponsavel.setCpf(trim(seg.getSeguradora().getCpfcnpjresponsavel()));
            }

            MDFInfoSeguroInfo mdfInfoSeguroInfo = new MDFInfoSeguroInfo();
            mdfInfoSeguroInfo.setCnpj(trim(seg.getSeguradora().getCnpjseguradora()));
            mdfInfoSeguroInfo.setSeguradora(trim(seg.getSeguradora().getNomeseguradora()));

            seguro.setInfo(mdfInfoSeguroInfo);

            mdfInfoSeguroResponsavel.setResponsavelSeguro(seg.getSeguradora().getResponsavel());
            seguro.setResponsavelSeguro(mdfInfoSeguroResponsavel);
            listaSeguros.add(seguro);
        }

        return listaSeguros;

    }

}
