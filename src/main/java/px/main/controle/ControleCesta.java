package px.main.controle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import px.main.veterinaria.modelos.Atendimento;
import px.main.veterinaria.modelos.Caixa;
import px.main.veterinaria.modelos.Cesta;
import px.main.veterinaria.modelos.Pacote;
import px.main.veterinaria.modelos.ProdutoAtendimento;
import px.main.veterinaria.modelos.ProdutoCesta;
import px.main.veterinaria.modelos.Taxa;
import px.main.veterinaria.servicos.AtendimentoService;
import px.main.veterinaria.servicos.CaixaService;
import px.main.veterinaria.servicos.CestaService;
import px.main.veterinaria.servicos.ClienteService;
import px.main.veterinaria.servicos.PacoteService;
import px.main.veterinaria.servicos.ProdutoService;
import px.main.veterinaria.view.CestaConsultaView;
import px.main.veterinaria.view.TicketView;

@Controller
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequestMapping("/cesta")
public class ControleCesta {

	Cesta cesta = new Cesta();

	@Autowired
	ClienteService clienteService;
	@Autowired
	AtendimentoService atendimentoService;
	@Autowired
	ProdutoService produtoService;
	@Autowired
	CestaService cestaService;
	@Autowired
	CaixaService caixaService;
	@Autowired
	PacoteService pacoteService;

	@GetMapping("/{id}")
	public ModelAndView cesta(@PathVariable Integer id) {
		ModelAndView model = new ModelAndView("produto/cestaForm");
		Cesta cesta = cestaService.get(id);
		model.addObject("cesta", cesta);
		return model;
	}

	@RequestMapping("/produtocesta/{id}")
	public @ResponseBody List<Cesta> historico(@PathVariable Integer id) {
		return cestaService.findJoinByProdutosCestaId(id);
	}

	@RequestMapping("/relatorio/{id}")
	public @ResponseBody List<ProdutoCesta> relatorio(@PathVariable Integer id) {
		return cestaService.relatorio(id);
	}

	@RequestMapping("/compras/{ano}")
	public @ResponseBody List<Cesta> comprasAno(@PathVariable Integer ano) {
		return cestaService.comprasAno(ano);
	}

	@RequestMapping("/ticket/{ano}")
	public @ResponseBody List<TicketView> ticketAno(@PathVariable Integer ano) {
		List<Cesta> lista = new ArrayList<Cesta>();
		lista = cestaService.comprasAno(ano);
		return TicketView.getLista(lista);
	}

	@RequestMapping(value = "")
	public ModelAndView cesta() {
		ModelAndView model = new ModelAndView("produto/cesta");
		model.addObject("listaProdutos", produtoService.estoque());
		model.addObject("listaClientes", clienteService.All());
		model.addObject("idCliente", cesta.getCliente().getId());
		model.addObject("listaTipos", produtoService.listaTipos());
		model.addObject("listaFormaPagamentos", caixaService.listaTaxas());
		cesta.getCaixas().clear();
		return model;
	}

	@RequestMapping(value = "/aquisicao")
	public ModelAndView aquisicao() {
		ModelAndView model = new ModelAndView("produto/aquisicao");
		model.addObject("aquisicao", produtoService.aquisicao());
		return model;
	}

	@RequestMapping(value = "/reset")
	public ModelAndView reset() {
		cesta = new Cesta();
		return cesta();
	}

	@GetMapping("/addservico")
	public @ResponseBody Integer addAtendimento(Integer id) {
		Atendimento atendimento = atendimentoService.get(id);
		Integer count = 0;
		if (!cesta.getProdutos().isEmpty())
			for (ProdutoCesta p : cesta.getProdutos()) {
				if (p.getIdProdutoOriginal() != null)
					if (p.getIdProdutoOriginal().equals(id))
						return 0;
			}
		cesta.setCliente(atendimento.getPet().getTutor());
		ProdutoCesta produtoCesta = new ProdutoCesta();
		produtoCesta.setNome(atendimento.getServico().getTexto() + " de " + atendimento.getPet().getNome());
		produtoCesta.setTipo("servico");
		produtoCesta.setCodigo(atendimento.getServico().getTexto());
		produtoCesta.setIdProdutoOriginal(atendimento.getId());
		produtoCesta.setQuantidade(1);
		produtoCesta.setPrecoVenda(atendimento.getValor());
		produtoCesta.setCesta(cesta);
		cesta.getProdutos().add(produtoCesta);
		count += 1;
		if (atendimento.isVenda()) {
			for (ProdutoAtendimento produtoAtendimento : atendimento.getProdutos()) {
				searchProdutos(produtoAtendimento.getIdProdutoOriginal(), produtoAtendimento.getQuantidade());
				count += 1;
			}
		}
		return count;
	}

	@GetMapping("/addpacote/{id}")
	public @ResponseBody Boolean addPacote(@PathVariable Integer id) {
		Pacote pacote = pacoteService.get(id).get();
		cesta.setCliente(pacote.getCliente());
		ProdutoCesta produtoCesta = new ProdutoCesta();
		produtoCesta.setNome("Pacote de " + pacote.getCliente().getNome());
		produtoCesta.setTipo("pacote");
		produtoCesta.setCodigo("");
		produtoCesta.setIdProdutoOriginal(pacote.getId());
		produtoCesta.setQuantidade(1);
		produtoCesta.setPrecoVenda(pacote.getValor());
		produtoCesta.setCesta(cesta);
		cesta.getProdutos().add(produtoCesta);
		return true;
	}

	@GetMapping("/pagar")
	public @ResponseBody Cesta pagar(@RequestParam Integer forma, @RequestParam Integer clienteId,
			@RequestParam Integer vezes, @RequestParam BigDecimal valor) {
		if (cesta.getProdutos().isEmpty()) {
			return null;
		} else {
			cesta.setData(new Date());
			cesta.setCliente(clienteService.get(clienteId));
			cesta.setTipo("comum");

			cesta.getCaixas().add(caixaService.pagamento(valor, vezes, forma, cesta));

			if (cesta.isQuitado()) {

				cesta = cestaService.salvar(cesta);
				cestaService.baixa(cesta);
				for (Caixa caixa : cesta.getCaixas()) {
					caixaService.salvar(caixa);
				}
				cesta = new Cesta();
			}
			return cesta;
		}
	}

	public BigDecimal total() {
		BigDecimal total = BigDecimal.ZERO;
		for (ProdutoCesta produto : cesta.getProdutos()) {
			total = total.add(produto.getPrecoVenda().multiply(new BigDecimal(produto.getQuantidade())));
		}
		return total;
	}

	@GetMapping("/add")
	public @ResponseBody boolean searchProdutos(Integer id, Integer quantidade) {
		ProdutoCesta prod = produtoService.getEstoque(id);
		if (prod.getIdProdutoOriginal() == null)
			return false;
		if (cesta.contemProduto(id)) {
			cesta.produtoUp(id, quantidade, produtoService.get(id).getQuantidade());
			return true;
		} else {
			ProdutoCesta clone = prod.clone();
			clone.setQuantidade(quantidade);
			clone.setIdProdutoOriginal(id);
			clone.setCesta(cesta);
			cesta.getProdutos().add(clone);
		}
		return true;
	}

	@GetMapping("/remover")
	public @ResponseBody boolean removerProdutos(Integer id) {
		if (!cesta.getProdutos().isEmpty())
			for (ProdutoCesta p : cesta.getProdutos()) {
				if (p.getIdProdutoOriginal().equals(id)) {
					cesta.getProdutos().remove(p);
					break;
				}
			}
		return true;
	}

	@GetMapping("/down")
	public @ResponseBody boolean downProdutos(Integer id) {
		cesta.produtoDown(id, 1);
		return true;
	}

	@GetMapping("/atualizar")
	public ResponseEntity<?> atualizar() {
		return ResponseEntity.ok(cesta.getProdutos());
	}

	@RequestMapping("/consulta")
	public ModelAndView consultar(CestaConsultaView cestaConsultaView) {
		ModelAndView model = new ModelAndView("produto/cestaconsulta");
		model.addObject("view", cestaConsultaView);
		model.addObject("listaClientes", clienteService.All());
		model.addObject("listaTipos", produtoService.listaTipos());
		return model;
	}

	@PostMapping("/consultar")
	public ModelAndView consulta(CestaConsultaView cestaConsultaView) {
		cestaConsultaView = cestaService.consultar(cestaConsultaView);
		return consultar(cestaConsultaView);
	}

	@RequestMapping(value = "/vezes/{tipo}")
	public @ResponseBody List<Taxa> getTaxasPorTipo(@PathVariable Integer tipo) {
		return caixaService.getTaxasPorTipo(tipo);
	}

	@RequestMapping(value = "/taxas")
	public @ResponseBody List<Taxa> getTaxasLista() {
		return caixaService.listaTaxas();
	}

	@RequestMapping("/rest/{id}")
	public @ResponseBody Cesta get(@PathVariable Integer id) {
		return cestaService.get(id);
	}
}
