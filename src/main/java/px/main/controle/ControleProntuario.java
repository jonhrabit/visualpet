package px.main.controle;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
			prontuario.setUsuario(Controle.usuarioAtivo());
		model.addObject("prontuario", prontuario);
		return model;
	}

	@RequestMapping(value = "/pet/{id}", method = RequestMethod.GET)
	public ModelAndView novoByNome(@PathVariable Integer id) {
		ModelAndView model = new ModelAndView("prontuario/formulario");
		model.addObject("listapet", petService.All());
		model.addObject("prontuario", prontuarioService.gerarByPet(id));
		return model;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView prontuario(@PathVariable Integer id) {
		return novo(prontuarioService.get(id));
	}

	@RequestMapping(value = "/lista")
	public ModelAndView lista() {
		ModelAndView model = new ModelAndView("prontuario/lista");
		model.addObject("prontuarios", prontuarioService.all());
		return model;
	}

	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Prontuario prontuario, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(prontuario);
		}
		if ((Controle.usuarioAtivo() == prontuario.getUsuario()) || (prontuario.getUsuario() == null)) {
			if (prontuario.getUsuario() == null)
				prontuario.setUsuario(Controle.usuarioAtivo());
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
