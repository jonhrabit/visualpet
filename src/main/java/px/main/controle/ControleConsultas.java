package px.main.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import px.main.veterinaria.modelos.Caixa;
import px.main.veterinaria.query.modelos.AtendimentosPorCliente;
import px.main.veterinaria.query.modelos.AtendimentosPorServico;
import px.main.veterinaria.query.modelos.RelatorioAtendimentosPorServico;
import px.main.veterinaria.query.modelos.Vendas;
import px.main.veterinaria.servicos.AtendimentoService;
import px.main.veterinaria.servicos.CaixaService;
import px.main.veterinaria.servicos.CestaService;

@Controller
@RequestMapping("/consultas")
public class ControleConsultas {
	@Autowired
	AtendimentoService atendimentoService;
	@Autowired
	CestaService cestaService;
	@Autowired
	CaixaService caixaService;

	@GetMapping("")
	public ModelAndView getConsulta() {
		ModelAndView model = new ModelAndView("relatorio/relatorio");
		return model;
	}

	@GetMapping("/atendimentosporservico/{mes}/{ano}")
	public @ResponseBody List<AtendimentosPorServico> getAtendimentosPorServico(@PathVariable Integer mes,
			@PathVariable Integer ano) {
		return atendimentoService.getListaPorServico(mes, ano);
	}
	@GetMapping("/vendas/{mes}/{ano}")
	public @ResponseBody Vendas getVendas(@PathVariable Integer mes, @PathVariable Integer ano) {
		return cestaService.relProdutosMes(mes, ano);
	}

	@GetMapping("/atendimentosporservico/trimestre/{mes}/{ano}")
	public @ResponseBody RelatorioAtendimentosPorServico getTrimestreAtendimentosPorServico(@PathVariable Integer mes,
			@PathVariable Integer ano) {
		return atendimentoService.getRelatorioPorServico(mes, ano);
	}

	@GetMapping("/atendimentosporcliente/{mes}/{ano}")
	public @ResponseBody List<AtendimentosPorCliente> getAtendimentosPorCliente(@PathVariable Integer mes,
			@PathVariable Integer ano) {
		return atendimentoService.getListaPorCliente(mes, ano);
	}

	@GetMapping("/caixasemcodigo")
	public @ResponseBody List<Caixa> getCaixaNotCod() {
		return caixaService.buscarNotCodigoVenda();
	}
	@GetMapping("/relatorio/{mes}/{ano}")
	public @ResponseBody List<Caixa> getRelatorio(@PathVariable Integer mes, @PathVariable Integer ano) {
		
		return caixaService.buscarNotCodigoVenda();
	}

}
