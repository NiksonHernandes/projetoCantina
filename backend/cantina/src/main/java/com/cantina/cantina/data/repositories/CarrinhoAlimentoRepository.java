package com.cantina.cantina.data.repositories;

import com.cantina.cantina.domain.models.CarrinhoAlimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoAlimentoRepository extends JpaRepository<CarrinhoAlimento, Long> {

//    Optional<AreaUser> deleteByArea_Id(UUID areaId);
//    boolean existsByArea_IdAndUser_Id(UUID areaId, UUID userId);
//    List<AreaUser> findByArea_Id(UUID areaId);
//    List<AreaUser> findByUser_Id(UUID userId);
//    Optional<AreaUser> findByUser_IdAndArea_Id(UUID userId, UUID areaId);

    Optional<CarrinhoAlimento> findByAlimento_IdAndCarrinho_Id(Long alimentoId, Long carrinhoId);
}
