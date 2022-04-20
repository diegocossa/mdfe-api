package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoLacre1A60;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoLacre1A60 {

    public List<MDFInfoLacre1A60> convert(Set<String> lacres){

        if (isNull(lacres)){
            return null;
        }

        List<MDFInfoLacre1A60> listaLacres1A60 = new ArrayList<>();

        for (String lac : lacres) {
            MDFInfoLacre1A60 lacre1A60 = new MDFInfoLacre1A60();

            lacre1A60.setNumeroDoLacre(trim(lac));
            listaLacres1A60.add(lacre1A60);
        }

        return listaLacres1A60;

    }

}
