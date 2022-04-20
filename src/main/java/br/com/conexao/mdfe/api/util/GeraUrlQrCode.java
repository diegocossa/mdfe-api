package br.com.conexao.mdfe.api.util;

import br.com.conexao.mdfe.api.config.property.MdfeProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeraUrlQrCode {

    @Autowired
    private MdfeProperty mdfeProperty;

    public String gerar(String chave, String ambiente){
        return mdfeProperty.getUrlQrCode().getURL().concat(chave).concat("&tpAmb=").concat(ambiente);
    }
}
