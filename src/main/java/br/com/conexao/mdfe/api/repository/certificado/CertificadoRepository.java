package br.com.conexao.mdfe.api.repository.certificado;

import br.com.conexao.mdfe.api.model.certificado.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CertificadoRepository extends JpaRepository<Certificado, Long> {

    Certificado findByIdcertificado(Long idcertificado);
    Certificado findByIdempresa(Long idempresa);

    @Query("select max(c.idcertificado) from Certificado c where c.idempresa = :idempresa")
    Long buscarUltimoCertificadoInserido(@Param("idempresa") Long idempresa);
}