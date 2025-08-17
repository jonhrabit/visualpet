package px.main.controle;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import px.main.seguranca.servicos.UsuarioLogado;
import px.main.veterinaria.modelos.Prontuario;
import px.main.veterinaria.servicos.PetService;
import px.main.veterinaria.servicos.ProntuarioService;

@Controller
@RequestMapping("/prontuario")
public class ControleProntuario {

	@Autowired
	ProntuarioService prontuarioService;
	@Autowired
	PetService petService;

	@RequestMapping("")
	public ModelAndView novo(Prontuario prontuario) {
		ModelAndView model = new ModelAndView("prontuario/formulario");
		model.addObject("listapet", petService.All());
		if (prontuario.getUsuario() == null)
			prontuario.setUsuario(UsuarioLogado.get());
		model.addObject("prontuario", prontuario);
		return model;
	}

	@GetMapping("/pet/{id}")
	public ModelAndView novoByNome(@PathVariable Integer id) {
		ModelAndView model = new ModelAndView("prontuario/formulario");
		model.addObject("listapet", petService.All());
		model.addObject("prontuario", prontuarioService.gerarByPet(id));
		return model;
	}

	@GetMapping("/{id}")
	public ModelAndView prontuario(@PathVariable Integer id) {
		return novo(prontuarioService.get(id));
	}

	@GetMapping("/{id}/receita")
	public ModelAndView receita(@PathVariable Integer id) {
		ModelAndView model = new ModelAndView("prontuario/receita");
		Prontuario prontuario = prontuarioService.get(id);
		if (prontuario.getPet() == null) {
			model.addObject("mensagem", "Prontuário não possui pet associado.");
			return model;
		}
		model.addObject("prontuario", prontuario);
		return model;
	}

	@RequestMapping(value = "/lista")
	public ModelAndView lista() {
		ModelAndView model = new ModelAndView("prontuario/lista");
		model.addObject("prontuarios", prontuarioService.all());
		return model;
	}

	@PostMapping("/salvar")
	public ModelAndView salvar(@Valid Prontuario prontuario, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(prontuario);
		}
		if ((UsuarioLogado.get() == prontuario.getUsuario()) || (prontuario.getUsuario() == null)) {
			if (prontuario.getUsuario() == null)
				prontuario.setUsuario(UsuarioLogado.get());
			if (prontuario.getData() == null)
				prontuario.setData(new Date());
			prontuario = prontuarioService.salvar(prontuario);
			ModelAndView model = new ModelAndView("redirect:/prontuario/" + prontuario.getId());
			attributes.addFlashAttribute("mensagem", "Prontuário salvo com sucesso.");
			return model;
		} else {
			ModelAndView model = new ModelAndView("redirect:/prontuario/");
			model.addObject("prontuario", prontuario);
			attributes.addFlashAttribute("mensagem",
					"É necessário ser o signatário do prontuario para salvar alterações.");
			return model;
		}
	}

	@RequestMapping(value = "/deletar/{id}")
	public @ResponseBody boolean delete(@PathVariable Integer id) {
		return prontuarioService.deletar(id);
	}
}
