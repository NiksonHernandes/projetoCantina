package com.cantina.cantina.data.repositories;

import com.cantina.cantina.domain.models.CarrinhoAlimento;
import com.cantina.cantina.domain.models.CarrinhoBebida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarrinhoBebidaRepository extends JpaRepository<CarrinhoBebida, Long> {

    //    Optional<AreaUser> deleteByArea_Id(UUID areaId);
    //    boolean existsByArea_IdAndUser_Id(UUID areaId, UUID userId);
    //    List<AreaUser> findByArea_Id(UUID areaId);
    //    List<AreaUser> findByUser_Id(UUID userId);
    //    Optional<AreaUser> findByUser_IdAndArea_Id(UUID userId, UUID areaId);

    Optional<CarrinhoBebida> findByBebida_IdAndCarrinho_Id(Long bebidaId, Long carrinhoId);
    List<CarrinhoBebida> findByCarrinho_Id(Long carrinhoId);

}
