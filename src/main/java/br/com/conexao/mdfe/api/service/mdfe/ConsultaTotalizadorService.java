package br.com.conexao.mdfe.api.service.mdfe;

import br.com.conexao.mdfe.api.repository.mdfe.MDFeRepository;
import br.com.conexao.mdfe.api.repository.mdfe.filter.MdfeTotalizadorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaTotalizadorService {

    @Autowired
    private MDFeRepository mdFeRepository;

    public List<MdfeTotalizadorFilter> findTotalizadores(String ambienteenvio){
        return mdFeRepository.getTotalizador(ambienteenvio);
    }

}
