package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.InfPercurso;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoIdentificacaoUfPercurso;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ConMDFInfoIdentificacaoUfPercurso extends MDFInfoIdentificacaoUfPercurso {

    public List<MDFInfoIdentificacaoUfPercurso> convert(List<InfPercurso> percursos){

        if (isNull(percursos)) {
            return null;
        }

        List<MDFInfoIdentificacaoUfPercurso> listaUf = new ArrayList<>();

        for (InfPercurso uf : percursos) {

            MDFInfoIdentificacaoUfPercurso percurso = new MDFInfoIdentificacaoUfPercurso();

            percurso.setUfPercurso(DFUnidadeFederativa.valueOfCodigo(uf.getUfper().getCodigo()));
            listaUf.add(percurso);
        }

        return listaUf;
    }
}
