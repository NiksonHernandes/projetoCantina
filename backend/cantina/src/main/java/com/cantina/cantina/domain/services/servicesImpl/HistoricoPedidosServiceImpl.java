package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.HistoricoPedidosRepository;
import com.cantina.cantina.data.repositories.UsuarioRepository;
import com.cantina.cantina.domain.models.HistoricoPedidos;
import com.cantina.cantina.domain.models.Usuario;
import com.cantina.cantina.domain.services.HistoricoPedidosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricoPedidosServiceImpl implements HistoricoPedidosService {

    @Autowired
    private HistoricoPedidosRepository _historicoPedidosRepository;


}
