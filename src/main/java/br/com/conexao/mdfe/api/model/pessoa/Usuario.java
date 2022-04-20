package br.com.conexao.mdfe.api.model.pessoa;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpessoa;

    @Size(max = 11)
    @CPF
    private String cpf;

    @Enumerated(value = EnumType.STRING)
    private TipoUsuario tipousuario;

    @NotBlank
    @Size(max = 40)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 200)
    private String email;

    private String fone;

    private Boolean flagdel = false;

    private Boolean flagativo = true;
}
