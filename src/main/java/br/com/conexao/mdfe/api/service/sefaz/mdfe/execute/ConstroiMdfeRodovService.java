package br.com.conexao.mdfe.api.service.sefaz.mdfe.execute;

import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.mdfe.generica.InfDoc;
import br.com.conexao.mdfe.api.model.mdfe.generica.InfMunDescarga;
import br.com.conexao.mdfe.api.model.mdfe.generica.TipoDocEnum;
import br.com.conexao.mdfe.api.model.mdfe.generica.Totalizadores;
import br.com.conexao.mdfe.api.service.empresa.EmpresaService;
import br.com.conexao.mdfe.api.service.sefaz.mdfe.GetIdLote;
import br.com.conexao.mdfe.api.service.sefaz.mdfe.construtor.*;
import br.com.conexao.mdfe.api.tenant.TenantIdEmpresaContext;
import br.com.conexao.mdfe.api.util.GeraUrlQrCode;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.mdfe3.classes.def.*;
import com.fincatto.documentofiscal.mdfe3.classes.lote.envio.MDFEnvioLote;
import com.fincatto.documentofiscal.mdfe3.classes.nota.*;
import com.fincatto.documentofiscal.mdfe3.utils.MDFGeraChave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import static br.com.conexao.mdfe.api.util.StringUtils.trim;
import static java.util.Objects.isNull;

@Service
public class ConstroiMdfeRodovService {

    @Autowired
    private ConMDFInfoIdentificacaoUfPercurso conMDFInfoIdentificacaoUfPercurso;

    @Autowired
    private ConMDFInfoIdentificacaoMunicipioCarregamento conMDFInfoIdentificacaoMunicipioCarregamento;

    @Autowired
    private ConMDFInfoLacre1A20 conMDFInfoLacre1A20;

    @Autowired
    private ConMDFInfoModalRodoviarioInfCIOT conMDFInfoModalRodoviarioInfCIOT;

    @Autowired
    private ConMDFInfoModalRodoviarioInfContratante conMDFInfoModalRodoviarioInfContratante;

    @Autowired
    private ConMDFInfoModalRodoviarioPedagio conMDFInfoModalRodoviarioPedagio;

    @Autowired
    private ConMDFInfoModalRodoviarioVeiculoReboque conMDFInfoModalRodoviarioVeiculoReboque;

    @Autowired
    private ConMDFInfoModalRodoviarioVeiculoCondutor conMDFInfoModalRodoviarioVeiculoCondutor;

    @Autowired
    private ConMDFInfoLacre1A60 conMDFInfoLacre1A60;

    @Autowired
    private ConMDFInfoAutorizacaoDownload conMDFInfoAutorizacaoDownload;

    @Autowired
    private ConMDFInfoInformacoesDocumentos conMDFInfoInformacoesDocumentos;

    @Autowired
    private ConMDFInfoSeguro conMDFInfoSeguro;

    @Autowired
    private GetIdLote getIdLote;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ConsultaStatusSefazMdfe consultaStatusSefazMdfe;

    @Autowired
    private GeraUrlQrCode geraUrlQrCode;

    @Value("${info.app.version:unknown}")
    private String version;

    public MDFEnvioLote construir(MDFe rodov) {

        //Ambiente e tipo emissão
        Empresa empresa = empresaService.findByIdEmpresa(TenantIdEmpresaContext.getCurrentTenant());

        Map<String, String> retornoConsulta = consultaStatusSefazMdfe.consultar();

        //Informacoes identificacao mdfe
        MDFInfoIdentificacao infoIdentificacao = new MDFInfoIdentificacao();
        infoIdentificacao.setModalidadeFrete(MDFModalidadeTransporte.RODOVIARIO);
        infoIdentificacao.setProcessoEmissao(MDFProcessoEmissao.EMISSOR_CONTRIBUINTE);
        infoIdentificacao.setVersaoProcessoEmissao(version);

        //infoIdentificacao.setAmbiente(DFAmbiente.HOMOLOGACAO); // TODO - Remover essa linha e descomentar linha abaixo para produção.
        infoIdentificacao.setAmbiente(empresa.getTipoambiente());
        infoIdentificacao.setTipoEmissao(empresa.getTpemis());

        //Gravar no rodov qual foi o tipo de emissão (NORMAL ou CONTINGENCIA)
        rodov.setTpemis(empresa.getTpemis().getDescricao());

        infoIdentificacao.setCodigoUF(empresa.getUf());
        infoIdentificacao.setDataEmissao(rodov.getDhemi());
        infoIdentificacao.setDataHoraDoInicioViagem(rodov.getDhiniviagem());

        //Somente envia 1 quando é true, quando é 0 false nao envia a tag
        if (empresa.getFlagcanalverde()) {
            infoIdentificacao.setIndicadorCanalVerde("1");
        }

        //Somente envia 1 quando é true, quando é 0 false nao envia a tag
        if (rodov.isIndcarregaposterior()) {
            infoIdentificacao.setIndicadorInclusaoCargaPosterior("1");
        }

        infoIdentificacao.setMunicipioCarregamentos(
                conMDFInfoIdentificacaoMunicipioCarregamento.convert(rodov.getInfmuncarregalist()));

        infoIdentificacao.setUnidadeFederativaInicio(DFUnidadeFederativa.valueOf(rodov.getUfini()));
        infoIdentificacao.setUnidadeFederativaFim(DFUnidadeFederativa.valueOf(rodov.getUffim()));
        infoIdentificacao.setIdentificacaoUfPercursos(conMDFInfoIdentificacaoUfPercurso.convert(rodov.getInfpercursolist()));

        infoIdentificacao.setTipoEmitente(MDFTipoEmitente.valueOfCodigo(String.valueOf(empresa.getTipoemitente().getCodigo())));
        infoIdentificacao.setTipoTranportador(empresa.getTpTransp());
        infoIdentificacao.setSerie(rodov.getSerie());

        //Endereco emitente
        MDFInfoEmitenteEndereco endereco = new MDFInfoEmitenteEndereco();
        endereco.setTelefone(trim(empresa.getFone()));
        endereco.setNumero(trim(empresa.getNumero()));
        endereco.setComplemento(trim(empresa.getComplemento()));
        endereco.setDescricaoMunicipio(trim(empresa.getMunicipio()));
        endereco.setEmail(trim(empresa.getEmail()));
        endereco.setSiglaUF(trim(empresa.getUf().getCodigo()));
        endereco.setCodigoMunicipio(String.valueOf(empresa.getCodigomunicipio()));
        endereco.setCep(String.valueOf(empresa.getCep()));
        endereco.setBairro(trim(empresa.getBairro()));
        endereco.setLogradouro(trim(empresa.getLogradouro()));

        //Emitente
        MDFInfoEmitente emitente = new MDFInfoEmitente();

        if (trim(empresa.getCnpj()).length() == 14) {
            emitente.setCnpj(trim(empresa.getCnpj()));
        } else {
            emitente.setCpf(trim(empresa.getCnpj()));
        }

        emitente.setInscricaoEstadual(trim(empresa.getInscricaoestadual()));
        emitente.setRazaoSocial(trim(empresa.getRazaosocial()));
        emitente.setNomeFantasia(trim(empresa.getFantasia()));
        emitente.setEndereco(endereco);

        //Veiculo Tracao
        MDFInfoModalRodoviarioVeiculoTracao veiculoTracao = new MDFInfoModalRodoviarioVeiculoTracao();
        veiculoTracao.setCapacidadeKG(String.valueOf(rodov.getVeiculotracao().getCapacidadekg()));
        veiculoTracao.setCapacidadeM3(String.valueOf(rodov.getVeiculotracao().getCapacidadem3()));
        veiculoTracao.setCodigoInterno(String.valueOf(rodov.getVeiculotracao().getIdveiculo()));
        veiculoTracao.setCondutor(conMDFInfoModalRodoviarioVeiculoCondutor.convert(rodov.getCondutorlist()));
        veiculoTracao.setPlaca(trim(rodov.getVeiculotracao().getPlaca()));
        veiculoTracao.setRenavam(trim(rodov.getVeiculotracao().getRenavam()));
        veiculoTracao.setTara(String.valueOf(rodov.getVeiculotracao().getTara()));
        veiculoTracao.setTipoRodado(MDFTipoRodado.valueOf(rodov.getVeiculotracao().getTiporodado().name()));
        veiculoTracao.setTipoCarroceria(MDFTipoCarroceria.valueOf(rodov.getVeiculotracao().getTipocarroceria().name()));
        veiculoTracao.setUnidadeFederativa(DFUnidadeFederativa.valueOf(rodov.getVeiculotracao().getUf().name()));

        //Modal Rodoviario ANTT
        MDFInfoModalRodoviarioANTT mdfInfoModalRodoviarioANTT = new MDFInfoModalRodoviarioANTT();
        mdfInfoModalRodoviarioANTT.setInfCIOT(conMDFInfoModalRodoviarioInfCIOT.convert(rodov.getInfciotlist()));
        mdfInfoModalRodoviarioANTT.setInfContratante(conMDFInfoModalRodoviarioInfContratante.convert(rodov.getInfcontratantelist()));
        mdfInfoModalRodoviarioANTT.setRntrc(trim(rodov.getVeiculotracao().getProprietario().getRntrc()));

        if (rodov.getValepedagiolist() != null && rodov.getValepedagiolist().size() != 0) {
            mdfInfoModalRodoviarioANTT.setValePedagio(conMDFInfoModalRodoviarioPedagio.convert(rodov.getValepedagiolist()));
        }

        //Modal Rodoviario
        MDFInfoModalRodoviario modalRodov = new MDFInfoModalRodoviario();
        modalRodov.setLacres(conMDFInfoLacre1A20.convert(rodov.getLacrerodoviariolist()));
        modalRodov.setMdfInfoModalRodoviarioANTT(mdfInfoModalRodoviarioANTT);

        if (rodov.getVeiculoreboquelist() != null && rodov.getVeiculoreboquelist().size() != 0) {
            modalRodov.setVeiculoReboques(conMDFInfoModalRodoviarioVeiculoReboque.convert(rodov.getVeiculoreboquelist()));
        }
        modalRodov.setVeiculoTracao(veiculoTracao);

        //Informacoes Modal Rodoviario
        MDFInfoModal infoModal = new MDFInfoModal();
        infoModal.setVersao("3.00");
        infoModal.setRodoviario(modalRodov);

        //Adicionais
        MDFInfoInformacoesAdicionais informacoesAdicionais = new MDFInfoInformacoesAdicionais();
        informacoesAdicionais.setInformacoesAdicionaisInteresseFisco(trim(rodov.getInfadfisco()));
        informacoesAdicionais.setInformacoesComplementaresInteresseContribuinte(trim(rodov.getInfcpl()));

        //Totalizador
        alimentarTotalizador(rodov);

        //Totais
        MDFInfoTotal total = new MDFInfoTotal();
        total.setPesoCarga(rodov.getTotalizadores().getQcarga());
        total.setQtdeCTe(rodov.getTotalizadores().getQcte());
        total.setQtdeMDFe(rodov.getTotalizadores().getQmdfe());
        total.setQtdeNFe(rodov.getTotalizadores().getQnfe());
        total.setUnidadeMedidaPesoBrutoCarga(rodov.getTotalizadores().getCunid());
        total.setValorTotalCarga(rodov.getTotalizadores().getVcarga());

        // Info MDFE
        MDFInfo mdfInfo = new MDFInfo();
        mdfInfo.setVersao("3.00");
        mdfInfo.setIdentificacao(infoIdentificacao);

        //Seguros
        mdfInfo.setSeguro(conMDFInfoSeguro.convert(rodov.getSeglist()));
        mdfInfo.setLacres(conMDFInfoLacre1A60.convert(rodov.getLacrelist()));
        mdfInfo.setInformacoesAdicionais(informacoesAdicionais);
        mdfInfo.setInformacoesDocumentos(conMDFInfoInformacoesDocumentos.convert(rodov.getInfmundescargalist()));
        mdfInfo.setInfoTotal(total);
        mdfInfo.setAutorizacaoDownload(conMDFInfoAutorizacaoDownload.convert(rodov.getAutorizadosxmllist()));
        mdfInfo.setMdfInfoModal(infoModal);
        mdfInfo.setEmitente(emitente);

        //Montagem Final
        com.fincatto.documentofiscal.mdfe3.classes.nota.MDFe mdFe = new com.fincatto.documentofiscal.mdfe3.classes.nota.MDFe();
        mdFe.setInfo(mdfInfo);

        //Gerador chave
        MDFGeraChave geraChave = new MDFGeraChave(mdFe);

        infoIdentificacao.setNumero(Integer.parseInt(trim(rodov.getNmdf())));
        infoIdentificacao.setCodigoNumerico(trim(geraChave.geraCodigoRandomico()));
        infoIdentificacao.setDigitoVerificador(geraChave.getDV());
        mdfInfo.setIdentificador(trim(geraChave.getChaveAcesso()));

        //Setar o codigo de QrCode
        MdfInfoSuplementar infoSuplementar = new MdfInfoSuplementar();
        infoSuplementar.setCodigoQrCodeMDfe(geraUrlQrCode.gerar(geraChave.getChaveAcesso(),empresa.getTipoambiente().getCodigo()));

        mdFe.setInformacoesSuplementares(infoSuplementar);

        //Setar responsavel tecnico
        MdfInfoResposavelTecnico resposavelTecnico = new MdfInfoResposavelTecnico();
        resposavelTecnico.setCnpjResponsavel("29877631000175");
        resposavelTecnico.setNomeResponsavel("Diego Ricardo Cossa");
        resposavelTecnico.setEmail("cossa@datavoid.com.br");
        resposavelTecnico.setFone("46988019975");

        mdfInfo.setInformacoesResposavelTecnico(resposavelTecnico);

        //Setar dados de info
        mdFe.setInfo(mdfInfo);

        MDFEnvioLote mdfEnvioLote = new MDFEnvioLote();

        if (isNull(rodov.getIdlote())) {
            mdfEnvioLote.setIdLote(String.valueOf(getIdLote.buscarProximoId()));
        } else {
            mdfEnvioLote.setIdLote(rodov.getIdlote());
        }

        mdfEnvioLote.setVersao("3.00");
        mdfEnvioLote.setMdfe(mdFe);

        return mdfEnvioLote;
    }

    private void alimentarTotalizador(MDFe rodov) {

        BigDecimal pesoCarga = BigDecimal.ZERO;
        BigDecimal valTotalCarga = BigDecimal.ZERO;
        Long qtdCTe = 0L;
        Long qtdNFe = 0L;
        Long qtdMDFe = 0L;

        for (InfMunDescarga listaMunDesc : rodov.getInfmundescargalist()) {

            qtdCTe =+ listaMunDesc.getInfdoclist().parallelStream().filter( infDoc -> infDoc.getTipodoc() == TipoDocEnum.CTE).count();
            qtdMDFe =+ listaMunDesc.getInfdoclist().parallelStream().filter( infDoc -> infDoc.getTipodoc() == TipoDocEnum.MDFE).count();
            qtdNFe =+ listaMunDesc.getInfdoclist().parallelStream().filter( infDoc -> infDoc.getTipodoc() == TipoDocEnum.NFE).count();

            for (InfDoc listDoc : listaMunDesc.getInfdoclist()) {

                pesoCarga = pesoCarga.add(BigDecimal.valueOf(listDoc.getPeso()));
                valTotalCarga = valTotalCarga.add(BigDecimal.valueOf(listDoc.getValor()));

            }

        }

        //Só deve carregar novo totalizador caso não exista, senão duplica linha
        if (isNull(rodov.getTotalizadores())) {
            rodov.setTotalizadores(new Totalizadores());
            rodov.getTotalizadores().setMdfe(rodov);
        }

        rodov.getTotalizadores().setQcarga(pesoCarga);
        rodov.getTotalizadores().setCunid(MDFUnidadeMedidaPesoBrutoCarga.KG);
        rodov.getTotalizadores().setVcarga(valTotalCarga);

        rodov.getTotalizadores().setQcte(String.valueOf(qtdCTe));
        rodov.getTotalizadores().setQmdfe(String.valueOf(qtdMDFe));
        rodov.getTotalizadores().setQnfe(String.valueOf(qtdNFe));
    }

}
