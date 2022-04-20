package br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor;

import br.com.conexao.mdfe.api.model.mdfe.generica.InfMunCarrega;
import com.fincatto.documentofiscal.mdfe3.classes.nota.MDFInfoIdentificacaoMunicipioCarregamento;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConMDFInfoIdentificacaoMunicipioCarregamento {

    public List<MDFInfoIdentificacaoMunicipioCarregamento> convert(List<InfMunCarrega> carregamento) {

        if (isNull(carregamento)) {
            return null;
        }

        List<MDFInfoIdentificacaoMunicipioCarregamento> listaCidades = new ArrayList<>();

        for (InfMunCarrega mun : carregamento) {

            MDFInfoIdentificacaoMunicipioCarregamento municipio = new MDFInfoIdentificacaoMunicipioCarregamento();

            municipio.setCodigoMunicipioCarregamento(trim(mun.getCmuncarrega()));
            municipio.setNomeMunicipioCarregamento(trim(mun.getXmuncarrega()));
            listaCidades.add(municipio);
        }

        return listaCidades;

    }
}
