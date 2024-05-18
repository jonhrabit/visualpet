package px.main.controle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import px.main.seguranca.modelos.Regras;
import px.main.seguranca.modelos.Usuario;
import px.main.seguranca.modelos.UsuarioRegra;
import px.main.seguranca.repository.UsuarioRepository;
import px.main.veterinaria.modelos.Extrato;
import px.main.veterinaria.servicos.CaixaService;
import px.main.veterinaria.servicos.ImportarArquivo;

@Controller
public class Controle {

	@Autowired
	CaixaService caixaService;

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Value("${fotos.path}")
	private String pathFotos;
	@Autowired
	BuildProperties buildProperties;

	@RequestMapping("/about")
	public ModelAndView about() {
		ModelAndView model = new ModelAndView("index");
		if (criarADM()) {
			model = new ModelAndView("adm");
		}
		model.addObject("software", buildProperties.getArtifact());
		model.addObject("versao", buildProperties.getVersion());
		return model;
	}

	@RequestMapping(value = "/csrf-token", method = RequestMethod.GET)
	public @ResponseBody String getCsrfToken(HttpServletRequest request) {
		CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		return token.getToken();
	}

	@RequestMapping("/home")
	public String hello() {
		return goLogin();
	}

	@RequestMapping("/login")
	public String goLogin() {
		return "login";
	}
	@RequestMapping("")
	public String index() {
		return goLogin();
	}

	@RequestMapping("/logout")
	public String logout() {
		return "login";
	}

	@RequestMapping("/teste")
	public String fotos() {
		return "teste";
	}

	@RequestMapping("/403")
	public String erro403() {
		return "403";
	}

	@RequestMapping("/404")
	public String erro404() {
		return "404";
	}

	@RequestMapping("/chat")
	public String chat() {
		return "chat/form_chat";
	}

	public boolean criarADM() {

		if (usuarioRepository.findAll().size() == 0) {
			Usuario usuario = new Usuario(0, "adm", "Administrador", bCryptPasswordEncoder.encode("adm"), "", "", true,
					new ArrayList<UsuarioRegra>());
			for (Regras regra : Regras.values()) {
				usuario.getRegras().add(new UsuarioRegra(0, usuario, regra.name()));
			}
			usuarioRepository.save(usuario);
			return true;
		}
		return false;
	}

	public static String usuarioAtivo() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String nome;
		if (principal instanceof UserDetails) {
			nome = ((UserDetails) principal).getUsername();
		} else {
			nome = principal.toString();
		}
		return nome;
	}

	@RequestMapping("/processamentos")
	public String processamento() {
		return "relatorio/geral";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody List<Extrato> comparar(@RequestParam("file") MultipartFile multipartFile) {
		String path = pathFotos + File.separator + multipartFile.getOriginalFilename();
		File dir = new File(path);
		try {
			multipartFile.transferTo(dir);
		} catch (IllegalStateException | IOException e) {
			return null;
		}
		try {
			List<Extrato> listaExtrato = ImportarArquivo.importarExtratos(new FileReader(path));
			return caixaService.compararListas(listaExtrato);
			
		} catch (FileNotFoundException e) {
			return null;
		}
	}
}
