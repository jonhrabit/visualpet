package px.main.controle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import px.main.veterinaria.modelos.Caixa;
import px.main.veterinaria.modelos.ProdutoCesta;
import px.main.veterinaria.servicos.CaixaService;
import px.main.veterinaria.servicos.FeriadosService;
import px.main.veterinaria.view.CaixaView;

@Controller
@RequestMapping("/caixa")
public class ControleCaixa {

	@Autowired
	CaixaService caixaService;

	@RequestMapping(value = "/lista")
	public ModelAndView caixa() {
		return consulta(new CaixaView());
	}

	@RequestMapping(value = "/consulta", method = RequestMethod.GET)
	public ModelAndView consulta(CaixaView caixaView) {
		ModelAndView model = new ModelAndView("caixa/lista");
		caixaService.preencher(caixaView);
		model.addObject("listaFormaPagamentos", caixaService.listaTaxasForma());
		model.addObject("view", caixaView);
		return model;
	}

	@RequestMapping(value = "/consulta/{ano}", method = RequestMethod.GET)
	public @ResponseBody List<Caixa> consulta(@PathVariable Integer ano) {
		return caixaService.busca(ano);
	}

	public ModelAndView consulta(CaixaView caixaView, String mensagem) {
		ModelAndView model = new ModelAndView("caixa/lista");
		caixaService.preencher(caixaView);
		model.addObject("view", caixaView);
		model.addObject("listaFormaPagamentos", caixaService.listaTaxasForma());
		model.addObject("mensagem", mensagem);
		return model;
	}

	@RequestMapping(value = "/fechamento/{data}", method = RequestMethod.GET)
	public ModelAndView fechamento(@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") Date data) {
		ModelAndView model = new ModelAndView("caixa/fechamento");
		model.addObject("lista", caixaService.fechamento(data));
		model.addObject("creditos", caixaService.creditos(data));
		return model;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView atendimento(@PathVariable Integer id) {
		return novo(caixaService.get(id));
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable Integer id) {
		caixaService.deletar(id);
		return consulta(new CaixaView(), "Registro de caixa excluído com sucesso.");
	}

	@RequestMapping(value = "/taxas", method = RequestMethod.GET)
	public ModelAndView taxas() {
		ModelAndView model = new ModelAndView("caixa/taxas");
		model.addObject("listaTaxas", caixaService.listaTaxas());
		return model;
	}

	@RequestMapping(value = "/imprimir", method = RequestMethod.GET)
	public ModelAndView imprimir(CaixaView caixaView) {
		ModelAndView model = new ModelAndView("caixa/impressaolista");
		caixaService.preencher(caixaView);
		model.addObject("view", caixaView);
		return model;
	}

	@RequestMapping("")
	public ModelAndView novo(Caixa caixa) {
		ModelAndView model = new ModelAndView("caixa/formulario");
		model.addObject("caixa", caixa);
		model.addObject("listaFormaPagamentos", caixaService.listaTaxasForma());
		return model;
	}

	public ModelAndView novo(Caixa caixa, String mensagem) {
		ModelAndView model = new ModelAndView("caixa/formulario");
		model.addObject("caixa", caixa);
		model.addObject("listaFormaPagamentos", caixaService.listaTaxasForma());
		model.addObject("mensagem", mensagem);
		return model;
	}

	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public ModelAndView salvar(Caixa caixa) {
		if ((caixa.getValor() == null) || (caixa.getTexto().isBlank())) {
			return novo(caixa, "Os campos valor e texto devem estar preenchidos");
		}
		caixaService.salvar(caixa);
		return novo(caixa, "Registro realizado com o sucesso.");
	}

	@RequestMapping("/fix")
	public @ResponseBody String fixVendaBruta() {
		List<Caixa> lista = caixaService.findByValorBrutoIsNull();
		int i = 0;
		BigDecimal valor = BigDecimal.ZERO;
		for (Caixa caixa : lista) {
			i++;
			System.out.println(caixa.getId() + " - " + caixa.getTexto());
			for (ProdutoCesta pc : caixa.getCesta().getProdutos()) {
				valor = valor.add(pc.getPrecoVenda().multiply(new BigDecimal(pc.getQuantidade())));
			}
			caixa.setValorBruto(valor);
			caixaService.salvar(caixa);
			valor = BigDecimal.ZERO;
		}
		System.out.println(i + " registros alterados.");
		return i + " registros alterados.";
	}
	@RequestMapping(value="/feriados", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<LocalDate>> feriados() {
		FeriadosService feriados = new FeriadosService();
		return ResponseEntity.ok().body(feriados.getDatas());
	}

}
