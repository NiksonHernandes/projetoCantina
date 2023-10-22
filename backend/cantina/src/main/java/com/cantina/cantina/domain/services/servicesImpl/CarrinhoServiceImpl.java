package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.CarrinhoRepository;
import com.cantina.cantina.domain.services.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoServiceImpl implements CarrinhoService {

    @Autowired
    private CarrinhoRepository _carrinhoRepository;

}
