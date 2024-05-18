package px.main.controle;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import px.main.seguranca.modelos.Regras;
import px.main.seguranca.modelos.Usuario;
import px.main.seguranca.modelos.UsuarioRegra;
import px.main.seguranca.servicos.UsuarioService;
import px.main.seguranca.views.Alterarsenha;

@Controller
@RequestMapping("/usuario")
public class ControleUsuarios {
	@Autowired
	UsuarioService usuarioService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = "/lista")
	public ModelAndView lista() {
		ModelAndView model = new ModelAndView("usuario/lista");
		model.addObject("lista", usuarioService.All());
		return model;
	}
	public ModelAndView lista(String mensagem) {
		ModelAndView model = new ModelAndView("usuario/lista");
		model.addObject("lista", usuarioService.All());
		model.addObject("mensagem", mensagem);
		return model;
	}

	@RequestMapping("")
	public ModelAndView novo(@Valid Usuario usuario) {
		ModelAndView model = new ModelAndView("usuario/formulario");
		model.addObject("usuario", usuario);
		model.addObject("listaRegras", Regras.values());
		return model;
	}
	public ModelAndView novo(@Valid Usuario usuario, String mensagem) {
		ModelAndView model = new ModelAndView("usuario/formulario");
		model.addObject("usuario", usuario);
		model.addObject("mensagem", mensagem);
		model.addObject("listaRegras", Regras.values());
		return model;
	}

	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Usuario usuario) {
		if ((usuario.getNome().equals(""))&&(usuario.getLogin().equals(""))) {
			return novo(usuario,"Informe o nome do usuário.");
		}
		for (UsuarioRegra regra : usuario.getRegras()) {
			regra.setUsuario(usuario);
		}
		if (usuario.isNovo()) {
			usuario.setSenha(usuario.getLogin(), bCryptPasswordEncoder);
		} else {
			usuario.setSenhaPublica(usuarioService.getSenhaPublic(usuario.getLogin()));
		}
		usuarioService.salvar(usuario);
		return novo(usuario,"Usuário salvo com sucesso");
	}

	@RequestMapping(value = "/salvarsenha", method = RequestMethod.POST)
	public ModelAndView alterar(@Valid Alterarsenha view) {
		if (view.CompararSenhas()) {
			Usuario user = usuarioService.get(Controle.usuarioAtivo());
			if (bCryptPasswordEncoder.matches(view.getSenha(), user.getSenha())) {
				user.setSenha(view.getNova(), bCryptPasswordEncoder);
				usuarioService.salvar(user);
				ModelAndView model = new ModelAndView("usuario/alterarsenha");
				model.addObject("view", view);
				model.addObject("m1", "Senha alterada com sucesso.");
				return model;
			} else {
				ModelAndView model = new ModelAndView("usuario/alterarsenha");
				model.addObject("view", view);
				model.addObject("m2", "Senha incorreta.");
				return model;
			}
		}
		ModelAndView model = new ModelAndView("usuario/alterarsenha");
		model.addObject("m2", "Nova Senha informada não confere.");
		model.addObject("view", new Alterarsenha());
		return model;
	}

	@RequestMapping(value = "/alterarsenha", method = RequestMethod.GET)
	public ModelAndView alterar() {
		ModelAndView model = new ModelAndView("usuario/alterarsenha");
		model.addObject("view", new Alterarsenha());
		return model;

	}

	@RequestMapping(value = "/reset/{id}", method = RequestMethod.GET)
	public ModelAndView reset(@PathVariable Integer id) {
		Usuario usuario = usuarioService.get(id);
		if (!usuario.isNovo()) {
			usuario.setSenha(usuario.getLogin(), bCryptPasswordEncoder);
			usuarioService.salvar(usuario);
		}
		return lista();

	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable Integer id) {
		usuarioService.deletar(id);
		return lista("Usuario excluído com sucesso.");

	}

	@RequestMapping(value = "/qdelete/{id}", method = RequestMethod.GET)
	public ModelAndView qdelete(@PathVariable Integer id) {
		Usuario usuario = usuarioService.get(id);
		if (usuario.isNovo()) {
			ModelAndView model = new ModelAndView("usuario/excluir");
			model.addObject("usuario", usuario);
			return model;
		}
		return lista();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView usuario(@PathVariable Integer id) {
		return novo(usuarioService.get(id));
	}

	@RequestMapping(value = "/existe/{login}", method = RequestMethod.GET)
	public @ResponseBody String LoginExiste(@PathVariable String login) {
		if (usuarioService.existeForLogin(login)) {
			return "ok";
		} else {
			return "not";
		}
	}

}
