package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.AlimentoRepository;
import com.cantina.cantina.domain.models.Alimento;
import com.cantina.cantina.domain.models.dtos.AlimentoDTO;
import com.cantina.cantina.domain.services.AlimentoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlimentoServiceImpl implements AlimentoService {

    @Autowired
    private AlimentoRepository _alimentoRepository;

    @Override
    @Transactional
    public AlimentoDTO createAlimento(AlimentoDTO alimentoDTO) {
        if (alimentoDTO.getNomeAlimento() == null || alimentoDTO.getValorAlimento() == null ||
                alimentoDTO.getQntEstoqueAlimento() == null || alimentoDTO.getAlimentoDisponivel() == null){
            throw new IllegalArgumentException("Campos vazios.");
        }

        Alimento alimentoModel = new Alimento();
        alimentoModel.setNomeAlimento(alimentoDTO.getNomeAlimento());
        alimentoModel.setValorAlimento(alimentoDTO.getValorAlimento());
        alimentoModel.setQntEstoqueAlimento(alimentoDTO.getQntEstoqueAlimento());
        alimentoModel.setDescricaoAlimento(alimentoDTO.getDescricaoAlimento());
        alimentoModel.setAlimentoDisponivel(alimentoDTO.getAlimentoDisponivel());

        alimentoModel = _alimentoRepository.save(alimentoModel);

        return AlimentoDTO.toDTO(alimentoModel);
    }

    @Override
    @Transactional
    public void deleteAlimento(Long alimentoId) {
        Alimento alimento = _alimentoRepository.findById(alimentoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do alimento não foi encontrado."));

        _alimentoRepository.delete(alimento);
    }

    @Override
    public AlimentoDTO getAlimento(Long alimentoId) {
        Alimento alimento = _alimentoRepository.findById(alimentoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do alimento não foi encontrado."));

        return AlimentoDTO.toDTO(alimento);
    }

    @Override
    public List<AlimentoDTO> listAlimento() {
        List<Alimento> alimentoList = _alimentoRepository.findAll();

        if (alimentoList.isEmpty()) {
            throw new IllegalArgumentException("A lista de alimentos está vazia.");
        }

        return AlimentoDTO.toListDTO(alimentoList);
    }

    @Override
    @Transactional
    public AlimentoDTO updateAlimento(AlimentoDTO alimentoDTO) {
        Alimento alimento = _alimentoRepository.findById(alimentoDTO.getAlimentoId())
                .orElseThrow(() -> new IllegalArgumentException("O ID do alimento não foi encontrado."));

        if (alimentoDTO.getNomeAlimento() == null || alimentoDTO.getValorAlimento() == null ||
                alimentoDTO.getQntEstoqueAlimento() == null || alimentoDTO.getAlimentoDisponivel() == null){
            throw new IllegalArgumentException("Campos vazios.");
        }

        Alimento alimentoModel = alimento;
        alimentoModel.setNomeAlimento(alimentoDTO.getNomeAlimento());
        alimentoModel.setValorAlimento(alimentoDTO.getValorAlimento());
        alimentoModel.setQntEstoqueAlimento(alimentoDTO.getQntEstoqueAlimento());
        alimentoModel.setDescricaoAlimento(alimentoDTO.getDescricaoAlimento());
        alimentoModel.setAlimentoDisponivel(alimentoDTO.getAlimentoDisponivel());

        alimentoModel = _alimentoRepository.save(alimentoModel);

        return AlimentoDTO.toDTO(alimentoModel);
    }

}
