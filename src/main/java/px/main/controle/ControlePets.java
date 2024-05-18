package px.main.controle;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import px.main.veterinaria.modelos.Pet;
import px.main.veterinaria.modelos.PetInformacao;
import px.main.veterinaria.modelos.Vacina;
import px.main.veterinaria.repository.VacinaRepository;
import px.main.veterinaria.servicos.ClienteService;
import px.main.veterinaria.servicos.PetService;

@Controller
@RequestMapping("/pet")
public class ControlePets {

	@Value("${fotos.path}")
	private String pathFotos;

	@Autowired
	PetService petSVC;
	@Autowired
	ClienteService clienteService;

	@Autowired
	VacinaRepository vacinaRepository;

	@RequestMapping(value = "/lista")
	public ModelAndView lista() {
		ModelAndView model = new ModelAndView("pet/lista");
		model.addObject("pet", new Pet());
		model.addObject("lista", petSVC.All());
		return model;
	}

	public ModelAndView lista(String mensagem) {
		ModelAndView model = new ModelAndView("pet/lista");
		model.addObject("pet", new Pet());
		model.addObject("mensagem", mensagem);
		model.addObject("lista", petSVC.All());
		return model;
	}

	@RequestMapping(value = "/consulta", method = RequestMethod.POST)
	public ModelAndView consulta(Pet pet, BindingResult result, RedirectAttributes attributes) {
		ModelAndView model = new ModelAndView("pet/lista");
		model.addObject("pet", pet);
		model.addObject("lista", petSVC.buscaNome(pet.getNome()));
		return model;
	}

	@RequestMapping("")
	public ModelAndView novo(Pet pet) {
		ModelAndView model = new ModelAndView("pet/formulario");
		model.addObject("pet", pet);
		model.addObject("listaclientes", clienteService.All());
		model.addObject("anotacao", new PetInformacao());
		model.addObject("fotos", pet.getFotos(pathFotos));
		return model;
	}

	public ModelAndView novo(Pet pet, String mensagem) {
		ModelAndView model = new ModelAndView("pet/formulario");
		model.addObject("pet", pet);
		model.addObject("listaclientes", clienteService.All());
		model.addObject("anotacao", new PetInformacao());
		model.addObject("mensagem", mensagem);
		model.addObject("fotos", pet.getFotos(pathFotos));
		return model;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView pet(@PathVariable Integer id) {
		Pet pet = petSVC.get(id);
		return novo(pet);
	}

	public ModelAndView pet(Integer id, String mensagem) {
		Pet pet = petSVC.get(id);
		return novo(pet, mensagem);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable Integer id) {
		if (petSVC.delete(id)) {
			return lista("Pet excluído com exito.");
		} else {
			return lista("Não foi possível excluir o pet.");

		}
	}

	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Pet pet, BindingResult result, RedirectAttributes attributes) {
		if ((pet.getNome() == "") || (pet.getNome() == null)) {
			return novo(pet, "Informe pelo menos o nome do pet, não esquesa de selecionar o Tutor correto.");
		}

		if ((pet.isNovo()) && (petSVC.exist(pet))) {// TESTE SE JÁ UM PET COM AS MESMAS INFORMAÇÕES
			ModelAndView model = new ModelAndView("redirect:/pet/" + pet.getId());
			attributes.addFlashAttribute("mensagem",
					"Já existe um pet com estas informações, recuperamos ele para você.");
			return model;
		}

		pet = petSVC.salvar(pet);

		ModelAndView model = new ModelAndView("redirect:/pet/" + pet.getId());
		attributes.addFlashAttribute("mensagem", "Pet salvo com sucesso.");
		return model;
	}

	@RequestMapping(value = "/anotacao", method = RequestMethod.POST)
	public ModelAndView anotacao(PetInformacao petInformacao, RedirectAttributes attributes) {
		petSVC.anotar(petInformacao.getId(), petInformacao.getTipo(), petInformacao.getTexto(), 0);
		ModelAndView model = new ModelAndView("redirect:/pet/" + petInformacao.getId());
		attributes.addFlashAttribute("mensagem", "Anotação realizada com sucesso.");
		return model;
	}

	@RequestMapping(value = "/vacina/salvar", method = RequestMethod.GET)
	public ResponseEntity<?> salvarVacina(@DateTimeFormat(pattern = "yyyy-MM-dd") Date dia, String vacina,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date vigencia, Integer petId) {
		if ((vacina == "") || (vigencia == null) || (dia == null)) {
			return ResponseEntity.badRequest().body("Informe o nome da vacina.");
		}
		petSVC.vacinar(petId, dia, vacina, vigencia);
		return ResponseEntity.ok(petSVC.buscarByPetId(petId));
	}

	@RequestMapping(value = "/vacina/{data}", method = RequestMethod.GET)
	public ModelAndView salvarVacina(@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") Date data) {
		ModelAndView model = new ModelAndView("pet/vacinavencida");
		model.addObject("lista", vacinaRepository.vencidas(data));
		return model;
	}

	@RequestMapping(value = "/vacina/excluir/{petId}/{vacinaId}", method = RequestMethod.GET)
	public @ResponseBody List<Vacina> excluirVacina(@PathVariable Integer petId, @PathVariable Integer vacinaId) {
		petSVC.deleteVacina(petId, vacinaId);
		return petSVC.buscarByPetId(petId);
	}

	@RequestMapping(value = "/vacina/renovar", method = RequestMethod.GET)
	public @ResponseBody boolean renovarVacina(Integer id, Integer petId) {
		return petSVC.renovarVacina(id, petId);
	}

	@RequestMapping(value = "/consulta", method = RequestMethod.GET)
	public ResponseEntity<?> searchPetLike(String nome) {
		List<Pet> lista = petSVC.buscarByNomeLike(nome);
		if (lista.size() > 0) {
			return ResponseEntity.ok(lista);
		} else {
			return new ResponseEntity<>("Não Localizada", HttpStatus.NO_CONTENT);
		}
	}

	@RequestMapping(value = "/consulta_id", method = RequestMethod.GET)
	public ResponseEntity<?> searchTutorCelular(Integer idTutor) {
		List<Pet> lista = petSVC.buscarByTutorId(idTutor);
		if (lista.size() > 0) {
			return ResponseEntity.ok(lista);
		} else {
			return new ResponseEntity<>("Não Localizada", HttpStatus.NO_CONTENT);
		}
	}

	@RequestMapping(value = "/upfoto", method = RequestMethod.POST)
	public ModelAndView doUpload(@RequestParam("id") Integer id, @RequestParam("file") MultipartFile multipartFile) {

		File dir = new File(pathFotos + File.separator + id + File.separator + multipartFile.getOriginalFilename());

		if (!new File(pathFotos + File.separator + id + File.separator).exists())
			new File(pathFotos + File.separator + id + File.separator).mkdir();

		Pet pet = petSVC.get(id);
		try {
			multipartFile.transferTo(dir);
		} catch (IllegalStateException | IOException e) {
			return novo(pet, e.toString());
		}
		return novo(pet, "foto adicionada.");
	}

	@GetMapping("tutor/{id}")
	public @ResponseBody List<Pet> findByTutor(@PathVariable Integer id) {
		return petSVC.buscarByTutorId(id);
	}

}
