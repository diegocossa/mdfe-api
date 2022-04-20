package br.com.conexao.mdfe.api.model.pessoa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pessoa", schema = "public")
@Getter
@Setter
public class PessoaLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpessoa;

    @NotNull
    private Long idpessoatenant;

    @NotBlank
    private String tenantid;

    @Size(max = 11)
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

    @JsonIgnore
    private String senhausuario;

    private Long idempresapadrao;

    private Boolean flagdel = false;

    private Boolean flagativo = true;
}
