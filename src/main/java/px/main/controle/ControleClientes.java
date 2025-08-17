package px.main.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import px.main.veterinaria.modelos.Cliente;
import px.main.veterinaria.modelos.ClienteInformacao;
import px.main.veterinaria.servicos.ClienteService;
import px.main.veterinaria.servicos.PetService;

@Controller
@RequestMapping("/cliente")
public class ControleClientes {

	@Autowired
	ClienteService clienteSVC;
	
	@Autowired
	PetService petService;

	@RequestMapping(value = "/lista")
	public ModelAndView lista() {
		ModelAndView model = new ModelAndView("cliente/lista");
		model.addObject("cliente", new Cliente());
		model.addObject("lista", clienteSVC.All());
		return model;
	}

	@PostMapping("/consulta")
	public ModelAndView consulta(Cliente cliente, BindingResult result, RedirectAttributes attributes) {
		ModelAndView model = new ModelAndView("cliente/lista");
		model.addObject("lista", clienteSVC.buscaNome(cliente.getNome()));
		return model;
	}

	@RequestMapping("")
	public ModelAndView novo(Cliente cliente) {
		ModelAndView model = new ModelAndView("cliente/formulario");
		model.addObject("cliente", cliente);
		model.addObject("anotacao", new ClienteInformacao());
		return model;
	}
	public ModelAndView novo(Cliente cliente,String mensagem) {
		ModelAndView model = new ModelAndView("cliente/formulario");
		model.addObject("cliente", cliente);
		model.addObject("mensagem", mensagem);
		model.addObject("anotacao", new ClienteInformacao());
		return model;
	}

	@GetMapping("/{id}")
	public ModelAndView cliente(@PathVariable Integer id) {
		Cliente cliente = clienteSVC.get(id);
		return novo(cliente);
	}

	@PostMapping("/salvar")
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
		if (cliente.getNome().isBlank()) {// VALIDAÇÃO DO CLIENTE CADASTRADO
			return novo(cliente,"Informe o nome do Cliente.");
		} // CLIENTE OK

		if ((clienteSVC.exist(cliente))&&(cliente.getId()==0)) {
			ModelAndView model = new ModelAndView("redirect:/cliente/" + cliente.getId());
			attributes.addFlashAttribute("mensagem",
					"Já existe um tutor com estas informações, recuperamos ele para você.");
			return model;
		}

		cliente = clienteSVC.salvar(cliente);

		ModelAndView model = new ModelAndView("redirect:/cliente/" + cliente.getId());
		attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso.");
		return model;
	}

	@PostMapping("/anotacao")
	public ModelAndView anotacao(ClienteInformacao clienteInformacao, RedirectAttributes attributes) {
		clienteSVC.anotar(clienteInformacao.getId(), clienteInformacao.getTipo(), clienteInformacao.getTexto(),0);
		ModelAndView model = new ModelAndView("redirect:/cliente/" + clienteInformacao.getId());
		attributes.addFlashAttribute("mensagem", "Anotação realizada com sucesso.");
		return model;
	}

	@GetMapping("/del/{id}")
	public ModelAndView qdelete(@PathVariable Integer id) {
		ModelAndView model = new ModelAndView("cliente/excluir");
		model.addObject("cliente", clienteSVC.get(id));
		return model;
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable Integer id) {
		clienteSVC.delete(id);
		return lista();
	}
	@GetMapping("/consulta")
	public ResponseEntity<?> searchTutorNome(String nome) {
		List<Cliente> lista = clienteSVC.buscaNome(nome);
		if (lista.size() > 0) {
			return ResponseEntity.ok(lista);
		} else {
			return new ResponseEntity<>("Não Localizada", HttpStatus.NO_CONTENT);
		}
		
	}
	@GetMapping("/consulta_nome_pet")
	public ResponseEntity<?> searchTutorNomePet(String nome) {
		List<Cliente> lista = petService.buscarTutoresByNomePet(nome);
		if (lista.size() > 0) {
			return ResponseEntity.ok(lista);
		} else {
			return new ResponseEntity<>("Não Localizada", HttpStatus.NO_CONTENT);
		}
	}
}
