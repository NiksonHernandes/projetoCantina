package com.cantina.cantina.application.controllers;

import com.cantina.cantina.domain.services.HistoricoPedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/historicoPedidos")
public class HistoricoPedidosController {

    @Autowired
    private HistoricoPedidosService _historicoPedidosService;

}
