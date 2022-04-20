package br.com.conexao.mdfe.api.resource.mdfe;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.olap4j.impl.ArrayMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.ion.Decimal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/xmls")
public class XMLResource {

    @Autowired
    private ValidaErro validaErro;

    //TODO - Ajustar função abaixo para possibilidade de buscar os dados do json dinamicamente sem ter que abrir os nós

    @PostMapping("/inf")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity<Map<String,String>> cadastrar(@RequestParam("file") MultipartFile file) throws IOException {

        try {
            InputStream inputStream = file.getInputStream();
            String textoConvertido = IOUtils.toString(inputStream);

            JSONObject xmlJSONObj = XML.toJSONObject(textoConvertido);
            String jsonPrettyPrintString = xmlJSONObj.toString(4);

            JSONObject obj = new JSONObject(jsonPrettyPrintString);
            JSONObject nfeProc = obj.getJSONObject("nfeProc");
            JSONObject nfe = nfeProc.getJSONObject("NFe");

            //Chave da nota
            JSONObject protNFe = nfeProc.getJSONObject("protNFe");
            JSONObject infProt = protNFe.getJSONObject("infProt");

            JSONObject infNFe = nfe.getJSONObject("infNFe");

            //Valor total nota
            JSONObject total = infNFe.getJSONObject("total");
            JSONObject ICMSTot = total.getJSONObject("ICMSTot");

            Map<String, String> lista = new ArrayMap<>();
            //Peso total nota
            JSONObject transp = infNFe.getJSONObject("transp");
            try {
                JSONObject vol = transp.getJSONObject("vol");
                lista.put("pesobruto", vol.get("pesoB").toString());
            } catch (JSONException e) {
                JSONArray vol = transp.getJSONArray("vol");

                Double pesoB = 0.0;
                List<JSONObject> lvol = new ArrayList(vol.toList());

                for (int i = 0; i < lvol.size(); i++) {
                    Map<String, Object> hashVol = new HashMap((Map) lvol.get(i));
                    pesoB = pesoB + Double.parseDouble(hashVol.get("pesoB").toString());
                }

                lista.put("pesobruto", pesoB.toString());
            }


            lista.put("chave", infProt.get("chNFe").toString());
            lista.put("valornfe", ICMSTot.get("vNF").toString());


            return ResponseEntity.ok(lista);
        } catch (Exception e){
            validaErro.addErro("erro.informacoes-xml", "Falha ao buscar as informações do XML " + e.getMessage());

            throw new MdfeException(validaErro);
        }
    }

}
