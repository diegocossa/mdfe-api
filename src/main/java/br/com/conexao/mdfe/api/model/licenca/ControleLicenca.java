package br.com.conexao.mdfe.api.model.licenca;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ControleLicenca {

    public Boolean isVenceMenos10Dias(LocalDateTime dataValidadeLicenca){
        Boolean licencaVencendo=false;
        LocalDate dataAtual = LocalDate.now();

        if(ChronoUnit.DAYS.between(dataAtual, dataValidadeLicenca.toLocalDate()) < 10) {
            licencaVencendo = true;
        }

        return licencaVencendo;
    }

    public Boolean isVencida(LocalDateTime dataValidadeLicenca){
        LocalDate dataAtual = LocalDate.now();

        return (ChronoUnit.DAYS.between(dataAtual, dataValidadeLicenca.toLocalDate()) < 0);
    }
}
