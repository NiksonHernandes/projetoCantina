package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.HistoricoPedidosRepository;
import com.cantina.cantina.domain.services.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricoPedidosServiceImpl implements CarrinhoService {

    @Autowired
    private HistoricoPedidosRepository _historicoPedidosRepository;

}
