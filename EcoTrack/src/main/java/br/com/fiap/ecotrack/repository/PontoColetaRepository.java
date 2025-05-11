package br.com.fiap.ecotrack.repository;

import br.com.fiap.ecotrack.model.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {
    
    List<PontoColeta> findByAtivoTrue();
    
    List<PontoColeta> findByUsuarioId(Long usuarioId);
    
    @Query("SELECT p FROM PontoColeta p " +
           "JOIN p.materiaisAceitos mp " +
           "WHERE mp.material.id = :materialId AND p.ativo = true")
    List<PontoColeta> findByMaterialId(@Param("materialId") Long materialId);
    
    @Query("SELECT p FROM PontoColeta p " +
           "WHERE p.cidade = :cidade AND p.ativo = true")
    List<PontoColeta> findByCidade(@Param("cidade") String cidade);
    
    @Query(value = "SELECT * FROM TB_PONTO_COLETA p " +
                   "WHERE p.ativo = 1 " +
                   "AND SDO_WITHIN_DISTANCE(SDO_POINT_TYPE(p.longitude, p.latitude, NULL), " +
                   "SDO_POINT_TYPE(:longitude, :latitude, NULL), " +
                   "':distance=:raioKm unit=KM') = 'TRUE'", 
           nativeQuery = true)
    List<PontoColeta> findNearby(@Param("latitude") String latitude, 
                               @Param("longitude") String longitude, 
                               @Param("raioKm") double raioKm);
}
