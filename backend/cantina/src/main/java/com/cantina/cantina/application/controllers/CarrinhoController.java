package com.cantina.cantina.application.controllers;

import com.cantina.cantina.domain.models.dtos.CarrinhoDTO;
import com.cantina.cantina.domain.models.dtos.SignUpDTO;
import com.cantina.cantina.domain.services.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService _carrinhoService;

    @PostMapping("/adicionar-alimento")
    public ResponseEntity<Object> adicionarAlimento(@RequestBody Map<String, Object> params) {
        try {
            long alimentoId = Long.parseLong(params.get("alimentoId").toString());
            int quantidadeAlimento = Integer.parseInt(params.get("quantidadeAlimento").toString());

            _carrinhoService.adicionarAlimentoNoCarrinho(alimentoId, quantidadeAlimento);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/adicionar-bebida")
    public ResponseEntity<Object> adicionarBebida(@RequestBody Map<String, Object> params) {
        try {
            long bebidaId = Long.parseLong(params.get("bebidaId").toString());
            int quantidadeBebida= Integer.parseInt(params.get("quantidadeBebida").toString());

            _carrinhoService.adicionarBebidaNoCarrinho(bebidaId, quantidadeBebida);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/delete-carrinho")
    public ResponseEntity<Object> deleteCarrinho(@RequestParam(value = "carrinhoId") Long carrinhoId) {
        try {
            _carrinhoService.deleteCarrinho(carrinhoId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/fechar-carrinho")
    public ResponseEntity<Object> fecharCarrinho(@RequestParam(value = "carrinhoId") Long carrinhoId) {
        try {
            _carrinhoService.fechaCarrinho(carrinhoId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/finalizar-pedido")
    public ResponseEntity<Object> finalizarPedido(@RequestBody CarrinhoDTO carrinhoDTO) {
        try {
            _carrinhoService.finalizarPedido(carrinhoDTO);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-carrinho")
    public ResponseEntity<Object> getCarrinho(@RequestParam(value = "carrinhoId") Long carrinhoId) {
        try {
            return ResponseEntity.ok(_carrinhoService.getCarrinho(carrinhoId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-carrinho-abertos")
    public ResponseEntity<Object> getCarrinhoAbertos() {
        try {
            return ResponseEntity.ok(_carrinhoService.getCarrinhoAbertos());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-carrinho-produtos")
    public ResponseEntity<Object> getCarrinhoProdutos(@RequestParam(value = "carrinhoId") Long carrinhoId) {
        try {
            return ResponseEntity.ok(_carrinhoService.getCarrinhoProdutos(carrinhoId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-carrinho-fechados")
    public ResponseEntity<Object> getCarrinhoFechados() {
        try {
            return ResponseEntity.ok(_carrinhoService.getCarrinhoFechados());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/list-carrinho")
    public ResponseEntity<Object> listCarrinho() {
        try {
            return ResponseEntity.ok(_carrinhoService.listCarrinho());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/opcao-pagamento")
    public ResponseEntity<Object> opcaoPagamento(@RequestBody Map<String, Object> params) {
        try {
            long carrinhoId = Long.parseLong(params.get("carrinhoId").toString());
            int opcao = Integer.parseInt(params.get("opcao").toString());

            _carrinhoService.opcaoPagamento(carrinhoId, opcao);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/remover-alimento")
    public ResponseEntity<Object> removerAlimento(@RequestBody Map<String, Object> params) {
        try {
            long alimentoId = Long.parseLong(params.get("alimentoId").toString());
            long carrinhoId = Long.parseLong(params.get("carrinhoId").toString());
            int quantidadeAlimento = Integer.parseInt(params.get("quantidadeAlimento").toString());

            _carrinhoService.removerAlimentoDoCarrinho(alimentoId, carrinhoId, quantidadeAlimento);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/remover-bebida")
    public ResponseEntity<Object> removerBebida(@RequestBody Map<String, Object> params) {
        try {
            long bebidaId = Long.parseLong(params.get("bebidaId").toString());
            long carrinhoId = Long.parseLong(params.get("carrinhoId").toString());
            int quantidadeBebida = Integer.parseInt(params.get("quantidadeBebida").toString());

            _carrinhoService.removerBebidaDoCarrinho(bebidaId, carrinhoId, quantidadeBebida);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/resetar-opcao")
    public ResponseEntity<Object> resetarOpcao(@RequestBody Map<String, Object> params) {
        try {
            long carrinhoId = Long.parseLong(params.get("carrinhoId").toString());

            _carrinhoService.resetarOpcao(carrinhoId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/verifica-is-carrinho-existe")
    public ResponseEntity<Object> verificaIsCarrinhoExiste() {
        try {
            return ResponseEntity.ok(_carrinhoService.verificaIsCarrinhoExiste());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
