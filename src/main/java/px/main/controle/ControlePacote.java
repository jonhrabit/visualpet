package px.main.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import px.main.veterinaria.modelos.Atendimento;
import px.main.veterinaria.modelos.Pacote;
import px.main.veterinaria.servicos.ClienteService;
import px.main.veterinaria.servicos.PacoteService;

@RestController
@RequestMapping("/pacote")
public class ControlePacote {
	@Autowired
	PacoteService pacoteService;
	@Autowired
	ClienteService clienteService;

	@GetMapping("")
	public ModelAndView formulario() {
		ModelAndView model = new ModelAndView("pacote/formulario");
		model.addObject("clientes", clienteService.All());
		model.addObject("lista", new ArrayList<Atendimento>());
		return model;
	}

	@GetMapping("/teste")
	public ModelAndView teste() {
		ModelAndView model = new ModelAndView("pacote/teste");
		return model;
	}

	@GetMapping("/lista")
	public ModelAndView lista() {
		ModelAndView model = new ModelAndView("pacote/lista");
		model.addObject("lista", pacoteService.all());
		return model;
	}

	@GetMapping("/{id}")
	public ModelAndView forrm(@PathVariable Integer id) {
		ModelAndView model = new ModelAndView("pacote/exibir");
		model.addObject("pacote", pacoteService.get(id).get());
		return model;
	}

	@GetMapping("/rest/{id}")
	public @ResponseBody Pacote get(@PathVariable Integer id) {
		Optional<Pacote> pacote = pacoteService.get(id);
		if (pacote.isEmpty())
			return null;
		return pacote.get();
	}

	@PutMapping
	public @ResponseBody Pacote put(Pacote pacoteNovo) {
		Optional<Pacote> optional = pacoteService.get(pacoteNovo.getId());
		if (optional.isEmpty())
			return new Pacote();
		Pacote pacote = optional.get();
		pacote.put(pacoteNovo);
		return pacoteService.salvar(pacote);
	}

	@PostMapping
	public @ResponseBody Pacote post(@RequestBody Pacote pacoteNovo) {
		return pacoteService.salvar(pacoteNovo);
	}

	@DeleteMapping("/{id}")
	public @ResponseBody String delete(@PathVariable Integer id) {
		pacoteService.deletar(id);
		return "Pacote escluído.";
	}

	@GetMapping("/all")
	public @ResponseBody List<Pacote> all() {
		return pacoteService.all();
	}
}
