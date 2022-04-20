package br.com.conexao.mdfe.api.service.ibge;

import br.com.conexao.mdfe.api.model.ibge.Estado;
import br.com.conexao.mdfe.api.model.ibge.Municipio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class IbgeService {

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private MunicipioService municipioService;

    private void sincronizarDadosApi(List<Estado> estados) {

        //delete todos os muncipios
        municipioService.deleteAll();

        //deleto todos os estados
        estadoService.deleteAll();

        //reinsere os estados
        estadoService.salvar(estados);

        //percore os estados buscando as cidades de cada um
        estados.forEach(estado -> {

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Municipio> request = new HttpEntity<>(new Municipio());
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            ResponseEntity<List<Municipio>> response = restTemplate.exchange("https://servicodados.ibge.gov.br/api/v1/localidades/estados/{UF}/municipios",
                    HttpMethod.GET,
                    request, new ParameterizedTypeReference<List<Municipio>>() {
                    }, estado.getId());

            if (response.getBody().size() > 0) {

                response.getBody().forEach(municipio -> {

                    municipio.setEstado(estado);
                    municipioService.salvar(municipio);

                });
            }
        });
    }

    private List<Estado> buscaEstadosIbge() {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Estado> request = new HttpEntity<>(new Estado());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<List<Estado>> response = restTemplate.exchange("https://servicodados.ibge.gov.br/api/v1/localidades/estados",
                HttpMethod.GET,
                request, new ParameterizedTypeReference<List<Estado>>() {
                });

        return response.getBody();
    }

    @Transactional
    public void sincronizar() {
        sincronizarDadosApi(buscaEstadosIbge());
    }
}
