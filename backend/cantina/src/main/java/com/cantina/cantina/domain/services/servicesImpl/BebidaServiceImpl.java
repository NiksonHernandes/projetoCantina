package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.BebidaRepository;
import com.cantina.cantina.domain.models.Bebida;
import com.cantina.cantina.domain.models.dtos.BebidaDTO;
import com.cantina.cantina.domain.services.BebidaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BebidaServiceImpl implements BebidaService {

    @Autowired
    private BebidaRepository _bebidaRepository;

    @Override
    @Transactional
    public BebidaDTO createBebida(BebidaDTO bebidaDTO) {
        if (bebidaDTO.getNomeBebida() == null || bebidaDTO.getValorBebida() == null ||
                bebidaDTO.getQntEstoqueBebida() == null || bebidaDTO.getBebidaDisponivel() == null){
            throw new IllegalArgumentException("Campos vazios.");
        }

        Bebida bebidaModel = new Bebida();
        bebidaModel.setNomeBebida(bebidaDTO.getNomeBebida());
        bebidaModel.setValorBebida(bebidaDTO.getValorBebida());
        bebidaModel.setQntEstoqueBebida(bebidaDTO.getQntEstoqueBebida());
        bebidaModel.setDescricaoBebida(bebidaDTO.getDescricaoBebida());
        bebidaModel.setBebidaDisponivel(bebidaDTO.getBebidaDisponivel());
        bebidaModel.setLinkBebida(bebidaDTO.getLinkBebida());

        bebidaModel = _bebidaRepository.save(bebidaModel);

        return BebidaDTO.toDTO(bebidaModel);
    }

    @Override
    @Transactional
    public void deleteBebida(Long bebidaId) {
        Bebida bebida = _bebidaRepository.findById(bebidaId)
                .orElseThrow(() -> new IllegalArgumentException("O ID da bebida não foi encontrado."));

        _bebidaRepository.delete(bebida);
    }

    @Override
    public BebidaDTO getBebida(Long bebidaId) {
        Bebida bebida = _bebidaRepository.findById(bebidaId)
                .orElseThrow(() -> new IllegalArgumentException("O ID da bebida não foi encontrado."));

        return BebidaDTO.toDTO(bebida);
    }

    @Override
    public List<BebidaDTO> listBebida() {
        List<Bebida> bebidaList = _bebidaRepository.findAll();

        if (bebidaList.isEmpty()) {
            throw new IllegalArgumentException("A lista de bebidas está vazia.");
        }

        return BebidaDTO.toListDTO(bebidaList);
    }

    @Override
    @Transactional
    public BebidaDTO updateBebida(BebidaDTO bebidaDTO) {
        Bebida bebida = _bebidaRepository.findById(bebidaDTO.getBebidaId())
                .orElseThrow(() -> new IllegalArgumentException("O ID da bebida não foi encontrado."));

        if (bebidaDTO.getNomeBebida() == null || bebidaDTO.getValorBebida() == null ||
                bebidaDTO.getQntEstoqueBebida() == null || bebidaDTO.getBebidaDisponivel() == null){
            throw new IllegalArgumentException("Campos vazios.");
        }

        Bebida bebidaModel = bebida;
        bebidaModel.setNomeBebida(bebidaDTO.getNomeBebida());
        bebidaModel.setValorBebida(bebidaDTO.getValorBebida());
        bebidaModel.setQntEstoqueBebida(bebidaDTO.getQntEstoqueBebida());
        bebidaModel.setDescricaoBebida(bebidaDTO.getDescricaoBebida());
        bebidaModel.setBebidaDisponivel(bebidaDTO.getBebidaDisponivel());
        bebidaModel.setLinkBebida(bebidaDTO.getLinkBebida());

        bebidaModel = _bebidaRepository.save(bebidaModel);

        return BebidaDTO.toDTO(bebidaModel);
    }

}
