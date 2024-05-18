package px.main.controle;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import px.main.veterinaria.modelos.Atendimento;
import px.main.veterinaria.modelos.Cliente;
import px.main.veterinaria.modelos.Pet;
import px.main.veterinaria.modelos.ProdutoAtendimento;
import px.main.veterinaria.modelos.Servico;
import px.main.veterinaria.modelos.enumeracoes.ServicoTipo;
import px.main.veterinaria.modelos.enumeracoes.SituacaoAtendimento;
import px.main.veterinaria.repository.PetRepository;
import px.main.veterinaria.repository.ServicoRepository;
import px.main.veterinaria.servicos.AtendimentoService;
import px.main.veterinaria.servicos.ClienteService;
import px.main.veterinaria.servicos.ProdutoService;
import px.main.veterinaria.view.AtendimentoConsultaView;
import px.main.veterinaria.view.AtendimentoListaView;
import px.main.veterinaria.view.AtendimentoProdutosView;

@Controller
@RequestMapping("/atendimento")
public class ControleAtendimentos {

	@Autowired
	AtendimentoService atendimentoService;
	@Autowired
	ClienteService clienteService;
	@Autowired
	PetRepository petRepository;
	@Autowired
	ServicoRepository servicoRepository;
	@Autowired
	private ProdutoService produtoService;

	@RequestMapping(value = "/lista", method = RequestMethod.GET)
	public ModelAndView lista(AtendimentoListaView atendimentoListaView) {
		ModelAndView model = new ModelAndView("atendimento/listaatendimento");
		model.addObject("atendimentoView", atendimentoListaView);
		model.addObject("listaservico", servicoRepository.findAll());
		return model;
	}

	@GetMapping("/servicos")
	public @ResponseBody List<Servico> allServices() {
		return servicoRepository.findAll();
	}

	@RequestMapping(value = "/atualizar/{tipo}", method = RequestMethod.GET)
	public @ResponseBody List<Atendimento> atualizarLista(@PathVariable Integer tipo) {
		if (tipo == 99) {
			return atendimentoService.semana();
		} else {
			return atendimentoService.semana(ServicoTipo.get(tipo));
		}
	}

	@RequestMapping(value = "/lista/{id}", method = RequestMethod.GET)
	public ModelAndView filtro(@PathVariable Integer id, AtendimentoListaView atendimentoListaView) {
		atendimentoListaView.setIdTipo(id);
		return lista(atendimentoListaView);
	}

	@RequestMapping(value = "/consulta", method = RequestMethod.GET)
	public ModelAndView consulta(AtendimentoConsultaView atendimenentoConsultaView) {
		ModelAndView model = new ModelAndView("atendimento/lista");
		model.addObject("listaservico", servicoRepository.findAll());
		model.addObject("situacaoAt", SituacaoAtendimento.values());
		model.addObject("atendimentoConsultaView", atendimentoService.atualizar(atendimenentoConsultaView));
		return model;
	}

	public ModelAndView consulta(String mensagem) {
		ModelAndView model = new ModelAndView("atendimento/lista");
		AtendimentoConsultaView atendimentoConsultaView = new AtendimentoConsultaView();
		model.addObject("listaservico", servicoRepository.findAll());
		model.addObject("situacaoAt", SituacaoAtendimento.values());
		model.addObject("mensagem", mensagem);
		model.addObject("atendimentoConsultaView", atendimentoService.atualizar(atendimentoConsultaView));
		return model;
	}

	@RequestMapping(value = "/teste", method = RequestMethod.GET)
	public ModelAndView mns() {
		return consulta("teste de mensagem.");
	}

	@RequestMapping(value = "/pendentes", method = RequestMethod.GET)
	public ModelAndView pendentes() {
		ModelAndView model = new ModelAndView("atendimento/listapendentes");
		model.addObject("lista", atendimentoService.aReceber());
		return model;
	}

	@RequestMapping("")
	public ModelAndView novo(Atendimento atendimento) {
		ModelAndView model = new ModelAndView("atendimento/formulario");
		model.addObject("atendimento", atendimento);
		model.addObject("listapet", petRepository.findAllByOrderByNomeAsc());
		model.addObject("listaservico", servicoRepository.findAll());
		model.addObject("situacaoAt", SituacaoAtendimento.values());
		return model;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView atendimento(@PathVariable Integer id) {
		return novo(atendimentoService.get(id));
	}

	@RequestMapping(value = "/iniciar/{id}", method = RequestMethod.GET)
	public @ResponseBody boolean iniciar(@PathVariable Integer id) {
		atendimentoService.iniciar(id);
		return true;
	}

	@RequestMapping(value = "/finalizar/{id}", method = RequestMethod.GET)
	public @ResponseBody boolean finalizar(@PathVariable Integer id) {
		atendimentoService.finalizar(id, BigDecimal.ZERO, 4);
		return true;
	}

	@RequestMapping(value = "/confiar/{id}", method = RequestMethod.GET)
	public @ResponseBody boolean pendurar(@PathVariable Integer id) {
		atendimentoService.pendurar(id);
		return true;
	}

	@RequestMapping(value = "/cancelar/{id}", method = RequestMethod.GET)
	public @ResponseBody boolean cancelar(@PathVariable Integer id) {
		atendimentoService.cancelar(id);
		return true;
	}

	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Atendimento atendimento, BindingResult result, RedirectAttributes attributes) {
		atendimentoService.salvar(atendimento);
		ModelAndView model = new ModelAndView("redirect:/atendimento/" + atendimento.getId());
		attributes.addFlashAttribute("mensagem", "Atendimento salvo com sucesso.");
		return model;
	}

	@RequestMapping(value = "/salvarPratico", method = RequestMethod.GET)
	public ModelAndView salvarPratico(AtendimentoListaView atendimento, BindingResult result,
			RedirectAttributes attributes) {
		if ((atendimento.getData() != null) && (atendimento.getPet() != "") && (atendimento.getTutor() != "")
				&& (atendimento.getValor() != BigDecimal.ZERO)) {

			Cliente cliente = clienteService.criar(atendimento);
			Atendimento atendimentoCadastro = new Atendimento(atendimento.getData(),
					cliente.getPet(atendimento.getPet()), atendimento.getServico(), atendimento.getValor());
			atendimentoService.salvar(atendimentoCadastro);

			ModelAndView model = new ModelAndView("redirect:/atendimento/lista");
			attributes.addFlashAttribute("mensagem", "Atendimento salvo com sucesso.");
			return model;

		}
		ModelAndView model = new ModelAndView("redirect:/atendimento/lista");
		attributes.addFlashAttribute("mensagem", "Todos os campos são obrigatórios para cadastro rápido.");
		return model;
	}

	@RequestMapping(value = "/deletar/{id}", method = RequestMethod.GET)
	public ModelAndView deletar(@PathVariable Integer id, RedirectAttributes attributes) {
		atendimentoService.deletar(id);
		ModelAndView model = new ModelAndView("redirect:/atendimento/lista");
		attributes.addFlashAttribute("mensagem", "Atendimento cancelado.");
		return model;
	}

	@RequestMapping(value = "/produtos/{id}", method = RequestMethod.GET)
	public ModelAndView listaProdutos(@PathVariable Integer id) {
		Atendimento atendimento = atendimentoService.get(id);
		ModelAndView model = new ModelAndView("atendimento/produtosatendimento");
		model.addObject("atendimento", atendimento);
		model.addObject("listaProdutos", produtoService.estoque());
		model.addObject("listaTipos", produtoService.listaTipos());
		return model;
	}

	public ModelAndView listaProdutos(Integer id, String mensagem) {
		Atendimento atendimento = atendimentoService.get(id);
		ModelAndView model = new ModelAndView("atendimento/produtosatendimento");
		model.addObject("atendimento", atendimento);
		model.addObject("listaProdutos", produtoService.estoque());
		model.addObject("listaTipos", produtoService.listaTipos());
		model.addObject("mensagem", mensagem);
		return model;
	}

	@RequestMapping(value = "/getprodutos/{id}")
	public @ResponseBody List<ProdutoAtendimento> atualizar(@PathVariable Integer id) {
		return atendimentoService.get(id).getProdutos();
	}

	@RequestMapping(value = "/salvarprodutos", method = RequestMethod.POST)
	public ResponseEntity<?> salvarProdutos(@RequestBody AtendimentoProdutosView view) {
		Atendimento atendimento = atendimentoService.get(view.getId());

		Iterator<ProdutoAtendimento> itr = atendimento.getProdutos().iterator();

		while (itr.hasNext()) {
			ProdutoAtendimento t = itr.next();
			if (!view.contem(t.getIdProdutoOriginal())) {
				itr.remove();
			}
		}
		for (ProdutoAtendimento produtoview : view.getLista()) {
			if (atendimento.contemProduto(produtoview.getIdProdutoOriginal())) {
				atendimento.getProdutoForIdOriginal(produtoview.getIdProdutoOriginal())
						.setQuantidade(produtoview.getQuantidade());
			} else {
				atendimento.getProdutos()
						.add(new ProdutoAtendimento(0, produtoview.getNome(), produtoview.getIdProdutoOriginal(),
								produtoview.getQuantidade(), 0, produtoview.getPreco(), atendimento));
			}
		}
		atendimento = atendimentoService.salvarPdt(atendimento);
		return ResponseEntity.ok(atendimento.getProdutos());
	}

	@RequestMapping(value = "/baixarprodutos/{id}", method = RequestMethod.GET)
	public ModelAndView baixarProdutos(@PathVariable Integer id) {
		Atendimento atendimento = atendimentoService.get(id);
		for (ProdutoAtendimento prod : atendimento.getProdutos()) {
			produtoService.baixa(prod.getIdProdutoOriginal(), prod.getQuantidade() - prod.getRemovido());
			prod.setRemovido(prod.getQuantidade());
		}
		atendimentoService.salvarPdt(atendimento);
		return listaProdutos(atendimento.getId(), "Produtos baixados do estoque.");
	}

	@RequestMapping(value = "/venda", method = RequestMethod.GET)
	public @ResponseBody boolean atendimentoVenda(Integer id, boolean venda) {
		Atendimento atendimento = atendimentoService.get(id);
		atendimento.setVenda(venda);
		atendimentoService.salvar(atendimento);
		return venda;
	}

	@PostMapping
	public @ResponseBody String post(@RequestBody Atendimento atendimento) {
		Optional<Pet> pet = petRepository.findById(atendimento.getPet().getId());
		if (pet.isEmpty())
			return "Não foi localizado o Pet de id " + atendimento.getPet().getId();
		atendimento.setPet(pet.get());
		atendimento.setServico(servicoRepository.getOne(atendimento.getServico().getId()));
		atendimento = atendimentoService.salvar(atendimento);
		return "Atendimento registrado com sucesso(" + atendimento.getId() + ")";
	}

	@GetMapping("/get/{id}")
	public @ResponseBody Atendimento post(@PathVariable Integer id) {
		return atendimentoService.get(id);
	}

	@GetMapping("/all")
	public @ResponseBody List<Atendimento> getAll() {
		return atendimentoService.All();
	}

}
