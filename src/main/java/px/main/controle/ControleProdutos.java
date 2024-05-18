package px.main.controle;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import px.main.veterinaria.modelos.Estoque;
import px.main.veterinaria.modelos.Produto;
import px.main.veterinaria.servicos.CestaService;
import px.main.veterinaria.servicos.EstoqueService;
import px.main.veterinaria.servicos.ProdutoService;

@Controller
@RequestMapping("/produto")
public class ControleProdutos {
	@Autowired
	ProdutoService produtoService;
	@Autowired
	CestaService cestaService;
	@Autowired
	EstoqueService estoqueService;

	@RequestMapping(value = "/lista/{consulta}")
	public ModelAndView lista(@PathVariable String consulta) {
		ModelAndView model = new ModelAndView("produto/lista");
		if (consulta.equals("todos")) {
			model.addObject("lista", produtoService.All());
		} else {
			model.addObject("lista", produtoService.consultaByNome(consulta));
		}
		return model;
	}

	@RequestMapping(value = "/lista")
	public @ResponseBody List<Produto> lista() {
		return produtoService.All();
	}

	@RequestMapping(value = "/estoque/lista/{id}")
	public @ResponseBody List<Estoque> listaConsulta(@PathVariable Integer id) {
		Produto produto = produtoService.get(id);
		return produto.getEstoque();
	}

	@RequestMapping("")
	public ModelAndView novo(Produto produto) {
		ModelAndView model = new ModelAndView("produto/form");
		model.addObject("produto", produto);
		return model;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView produto(@PathVariable Integer id) {
		Produto prod = produtoService.get(id);
		return novo(prod);
	}

	@RequestMapping(value = "/deletar/{id}", method = RequestMethod.GET)
	public @ResponseBody boolean deletar(@PathVariable Integer id) {
		return produtoService.deletar(id);
	}

	@DeleteMapping("/{id}")
	public @ResponseBody String delete(@PathVariable Integer id) {
		produtoService.deletar(id);
		return "Produto excluído com sucesso.";
	}

	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Produto produto, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(produto);
		}
		produto = produtoService.salvar(produto);
		ModelAndView model = new ModelAndView("redirect:/produto/" + produto.getId());
		attributes.addFlashAttribute("mensagem", "Produto salvo com sucesso.");
		return model;
	}

	@RequestMapping(value = "/criarestoque")
	public @ResponseBody String criarEstoque() {
		List<Produto> produtos = produtoService.All();
		Integer i = 0;
		for (Produto produto : produtos) {
			estoqueService.salvar(new Estoque(0, produto.getQ(), produto.getPrecoCompra(), "Lote 1", "Importado",
					produto.getValidade(), new Date(), produto));
			i++;
		}

		return i + " operações realizadas com exito.";
	}

}
