package br.com.conexao.mdfe.api.service.email;

import br.com.conexao.mdfe.api.model.pessoa.RecuperacaoSenha;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MailClientService {

    private static final Logger logger = LoggerFactory.getLogger(MailClientService.class);

    @Autowired
    private AmazonSimpleEmailService amazonSimpleEmailService;

    @Autowired
    private Configuration config;

    //todo esse email nao pode ficar assim tem que usar o amazon por causa do limite de emails.
    public void enviarEmailDAMDFE(String caminhoPDF, List<String> emailDestinatario) {

        File file = new File(caminhoPDF);

        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(file.getPath()); // Obtem o caminho do arquivo
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Anexo");
        attachment.setName(StringUtils.substringAfter(file.getName(), "_")); // Obtem o nome do arquivo

        try {

            MultiPartEmail email = new MultiPartEmail();
            email.setDebug(true);
            email.setHostName("smtp.gmail.com");
            email.setAuthentication("drive3gbrasil@gmail.com", "#mdfe3##");
            email.setSSL(true);

            for (String e : emailDestinatario) {
                email.addTo(e); //pode ser qualquer email
            }

            email.setFrom("drive3gbrasil@gmail.com", "3G Brasil - MDFe"); //será passado o email que você fará a autenticação
            email.setSubject("DAMDFE");
            email.setMsg("Em anexo PDF com o Documento Auxliar de MDF-e!");

            email.attach(attachment);

            email.send();

        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    public void prepareAndSendEmailRecuperacaoSenha(RecuperacaoSenha recuperacaoSenha) throws Exception {

        Map<String, Object> model = new HashMap<>();
        model.put("link", "https://www.3gbrasil.com.br/sessions/new/password?id=" +
                recuperacaoSenha.getIdrecuperacaosenha() + "&chk=" + recuperacaoSenha.getHash());

        Template templateEmail = config.getTemplate("email-recuperacao-senha.ftl");

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(recuperacaoSenha.getEmail()))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8")
                                        .withData(FreeMarkerTemplateUtils.processTemplateIntoString(templateEmail, model)))
                                .withText(new Content()
                                        .withCharset("UTF-8").withData("")))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData("Recuperação de Senha")))
                .withSource(getEmailEnvio());
        try {
            amazonSimpleEmailService.sendEmail(request);
            logger.info("Email de recuperação de senha!");
        } catch (Exception ex) {
            logger.error("ERRO -> Email de recuperação de senha não enviado. Error message: " + ex.getMessage());
        }
    }

    public void prepareAndSendEmailDadosDeAcesso(String email, String senha, String nome) throws Exception {

        Map<String, Object> model = new HashMap<>();
        model.put("email", email);
        model.put("senha", senha);
        model.put("nome", nome);

        Template templateEmail = config.getTemplate("email-dados-acesso.ftl");

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(email))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8")
                                        .withData(FreeMarkerTemplateUtils.processTemplateIntoString(templateEmail, model)))
                                .withText(new Content()
                                        .withCharset("UTF-8").withData("")))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData("Dados de Acesso 3G Brasil - MDFe")))
                .withSource(getEmailEnvio());
        try {
            amazonSimpleEmailService.sendEmail(request);
            logger.info("Email de acesso enviado!");
        } catch (Exception ex) {
            logger.error("ERRO -> Email de dados de acesso não enviado. Error message: " + ex.getMessage());
        }
    }

    private String getEmailEnvio() throws UnsupportedEncodingException {
        return new InternetAddress("dv.condominios.notify@gmail.com", "3G Brasil - MDFe").toString();//todo trocar esse email
    }
}
