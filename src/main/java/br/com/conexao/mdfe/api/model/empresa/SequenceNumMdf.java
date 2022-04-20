package br.com.conexao.mdfe.api.model.empresa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "sequence_nummdf")
@Getter
@Setter
public class SequenceNumMdf {

    @Id
    private String idsequence;

    private Long idempresa;

}
