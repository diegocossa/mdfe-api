package br.com.conexao.mdfe.api.async;

import br.com.conexao.mdfe.api.model.pessoa.RecuperacaoSenha;
import br.com.conexao.mdfe.api.service.email.MailClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsyncEmailComponent {

    private static final Logger logger = LoggerFactory.getLogger(AsyncEmailComponent.class);

    @Autowired
    private MailClientService mailClientService;

    @Async
    public void asyncEnviarEmailDAMDFE(String caminhoDoPDF, List<String> email) {
        try {
            logger.info("Inicio do envio de email, DAMDFE");

            mailClientService.enviarEmailDAMDFE(caminhoDoPDF, email);

            logger.info("Fim do envio de email, DAMDFE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void asyncEnviarEmailRecuperacaoSenha(RecuperacaoSenha recuperacaoSenha) {
        try {
            logger.info("Inicio do envio de email recuperação senha >" + recuperacaoSenha.getEmail());

            mailClientService.prepareAndSendEmailRecuperacaoSenha(recuperacaoSenha);

            logger.info("Fim do envio de email recuperação de senha >" + recuperacaoSenha.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
