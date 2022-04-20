package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.Periculosidade;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoPerigosos;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoPerigosos {

    public List<MDFInfoPerigosos> convert(List<Periculosidade> periculosidadeList){

        if (isNull(periculosidadeList)){
            return null;
        }

        List<MDFInfoPerigosos> listaPerigosos = new ArrayList<>();

        for (Periculosidade per : periculosidadeList) {
            MDFInfoPerigosos perigo = new MDFInfoPerigosos();

            perigo.setClasseRisco(trim(per.getXclarisco()));
            perigo.setGrupoEmbalagem(trim(per.getGremb()));
            perigo.setNomeEmbarque(trim(per.getXnomeae()));
            perigo.setNumeroONU(trim(per.getNonu()));
            perigo.setQtdeTipoVolume(trim(per.getQvoltipo()));
            perigo.setqTotProd(trim(per.getQtotprod()));
            listaPerigosos.add(perigo);
        }

        return listaPerigosos;

    }

}
