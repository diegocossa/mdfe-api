# MDF-e API

Métodos e rotas disponíveis da API.

# Permissões { /permissoes }
    + Get (@Authorization = CONSULTAR_PERMISSOES)
        + Todas as permissões
                
    + Get { /grupo } (@Authorization = CONSULTAR_PERMISSOES)
        + Grupos válidos
            + CADASTRO
            + UTILITARIO
            + MOVIMENTACAO
    
            
# Grupos { /grupos }
    + Get (@Authorization = CONSULTAR_GRUPOS)
        + Todos os grupos
        
    + Get { /idgrupo } (@Authorization = CONSULTAR_GRUPOS)
        + Buscar grupo específico
            
    + Post (@Authorization = CADASTRAR_GRUPOS)
        + Cadastrar grupo
        
    + Put { /idgrupo } (@Authorization = CADASTRAR_GRUPOS)
        + Alterar grupo
        
    + Delete { /idgrupo} (@Authorization = CADASTRAR_GRUPOS)
            + Deletar grupo


# Empresas { /empresas }
    + Get (@Authorization = CONSULTAR_EMPRESAS)
        + Todas as empresas
        
    + Get { /idempresa } (@Authorization = CONSULTAR_EMPRESAS)
            + Buscar empresa específica
        
    + Post (@Authorization = CADASTRAR_EMPRESAS)
        + Cadastrar empresa
        
    + Put { /idempresa } (@Authorization = CADASTRAR_EMPRESAS)
        + Alterar empresa
        
    + Delete { /idempresa } (@Authorization = CADASTRAR_EMPRESAS)
        + Deletar empresa
        
# Motoristas { /motoristas }
    + Get (@Authorization = CONSULTAR_MOTORISTAS)
        + Todos os motoristas
        
    + Get { /idmotorista } (@Authorization = CONSULTAR_MOTORISTAS)
            + Buscar motorista específico
        
    + Post (@Authorization = CADASTRAR_MOTORISTA)
        + Cadastrar motorista
            
    + Put { /idmotorista } (@Authorization = CADASTRAR_MOTORISTA)
        + Alterar empresa
        
    + Put { /idmotorista/ativo } (@Authorization = CADASTRAR_MOTORISTA)
            + Alterar status ativo do motorista 
                + Values: {true, false}        
        
    + Delete { /idmotorista } (@Authorization = CADASTRAR_MOTORISTA)
        + Deletar motorista   
        
# Veículos { /veiculos}
    + Get (@Authorization = CONSULTAR_VEICULOS)
        + Todos os veículos
        
    + Get { /idveiculo } (@Authorization = CONSULTAR_VEICULOS)
        + Buscar veículo específico
            
    + Post (@Authorization = CADASTRAR_VEICULOS)
        + Cadastrar veículo
        
    + Put { /idveiculo } (@Authorization = CADASTRAR_VEICULOS)
        + Alterar veículo
        
    + Delete { /idveiculo} (@Authorization = CADASTRAR_VEICULOS)
            + Deletar veículo            

# CIOTS { /ciots }
    + Get (@Authorization = CONSULTAR_CIOTS)
        + Todos os CIOTS
        
    + Get { /idciot } (@Authorization = CONSULTAR_CIOTS)
        + Buscar CIOT específico
            
    + Post (@Authorization = CADASTRAR_CIOTS)
        + Cadastrar CIOT
        
    + Put { /idciot } (@Authorization = CADASTRAR_CIOTS)
        + Alterar CIOT
        
    + Delete { /idciot} (@Authorization = CADASTRAR_CIOTS)
            + Deletar CIOT
            
    
    Campos
    - @Generate Long idciot       
    - @NotNull Long ciot       
    - @NotNull String cpfcnpj      
    - Boolean flagdel default(false)
