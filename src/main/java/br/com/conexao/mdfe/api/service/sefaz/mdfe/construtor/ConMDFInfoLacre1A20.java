package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoLacre1A20;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoLacre1A20 {

    public List<MDFInfoLacre1A20> convert(Set<String> lacres) {

        if (isNull(lacres)) {
            return null;
        }

        List<MDFInfoLacre1A20> listaLacres1A20 = new ArrayList<>();

        for (String lacre : lacres) {
            MDFInfoLacre1A20 lacre1A20 = new MDFInfoLacre1A20();

            lacre1A20.setNumeroDoLacre(trim(lacre));
            listaLacres1A20.add(lacre1A20);
        }

        return listaLacres1A20;

    }
}
