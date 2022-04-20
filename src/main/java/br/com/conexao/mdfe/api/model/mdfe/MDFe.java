package br.com.conexao.mdfe.api.model.mdfe;

import br.com.conexao.mdfe.api.model.mdfe.generica.*;
import br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario.*;
import br.com.conexao.mdfe.api.model.tenancy.TenancyListaFilter;
import br.com.conexao.mdfe.api.model.veiculo.Veiculo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "mdfe")
@Table(name = "mdfe")
@Getter
@Setter
public class MDFe extends TenancyListaFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idmdfe;

    private String idlote;// character varying(20),
    private String chave;// character varying(48),
    private String numrecibo;// character varying(30),
    private String numprotocolo;// character varying(30),
    private String ambienteenvio;// character varying (30),
    private String tpemis;
    private String statusenvio;/// character varying(10),
    private String statusmdfe;
    private String motivoenvio;// character varying(50),
    private String ambienterecibo;// character varying (30),
    private String statusrecibo;// character varying(10),
    private String motivorecibo;// character varying(50),
    private String retornorecibo;// character varying (255),
    private Integer serie;// integer,

    private String nmdf;// character varying(9),

    private String cmdf;// character varying(8),
    private String cdv;// character varying(1),

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dhemi;// timestamp without time zone,
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dhiniviagem;// timestamp without time zone,
    private boolean indcanalverde = false;//boolean
    private boolean indcarregaposterior = false;// boolean
    private String ufini;// character varying(2),
    private String uffim;// character varying(2),

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] xml;
    private String versaomodal;// character varying(4),
    private String versaolayout;// character varying(4),
    private String infadfisco;// character varying(2000),
    private String infcpl;// character varying(5000),

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "idveiculotracao")
    private Veiculo veiculotracao;

    @Enumerated(value = EnumType.STRING)
    private SituacaoMDFe situacao = SituacaoMDFe.GRAVADO;

    @OneToMany(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private List<InfMunCarrega> infmuncarregalist = new ArrayList<>();

    @OneToMany(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private List<InfPercurso> infpercursolist = new ArrayList<>();

    @OneToMany(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private List<InfMunDescarga> infmundescargalist = new ArrayList<>();

    @OneToMany(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private List<Seg> seglist = new ArrayList<>();

    @OneToOne(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private Totalizadores totalizadores;

    @ElementCollection
    @CollectionTable(
            name="lacre",
            joinColumns=@JoinColumn(name="idmdfe")
    )
    @Column(name="nlacre")
    private Set<String> lacrelist = new HashSet<>();

    @OneToMany(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private List<AutorizadosXml> autorizadosxmllist = new ArrayList<>();

    @OneToMany(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private List<InfCiot> infciotlist = new ArrayList<>();

    @OneToMany(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private List<ValePedagio> valepedagiolist = new ArrayList<>();

    @OneToMany(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private List<InfContratante> infcontratantelist= new ArrayList<>();

    @OneToMany(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private List<Condutor> condutorlist= new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name="lacre_rodoviario",
            joinColumns=@JoinColumn(name="idmdfe")
    )
    @Column(name="nlacre")
    private Set<String> lacrerodoviariolist= new HashSet<>();

    @OneToMany(mappedBy = "mdfe", cascade = CascadeType.ALL)
    @JsonManagedReference("mdfe")
    private List<VeiculoReboque> veiculoreboquelist= new ArrayList<>();

    private LocalDateTime dtultimaconsultarecibo;

}
