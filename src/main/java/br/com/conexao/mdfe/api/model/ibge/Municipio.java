package br.com.conexao.mdfe.api.model.ibge;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "municipio", schema = "public")
@Getter
@Setter
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idmunicipio;

    private Long id;

    private String nome;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idestado")
    private Estado estado;
}
