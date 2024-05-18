package px.main.veterinaria.servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import px.main.controle.Controle;
import px.main.veterinaria.modelos.Pet;
import px.main.veterinaria.modelos.Prontuario;
import px.main.veterinaria.repository.ProntuarioRepository;

@Service
public class ProntuarioService {

	@Autowired
	ProntuarioRepository prontuarioRepository;

	@Autowired
	PetService petService;

	public Prontuario get(Integer id) {
		Optional<Prontuario> prontuario = prontuarioRepository.findById(id);
		if (!prontuario.isEmpty())
			return prontuario.get();
		return null;
	}

	public List<Prontuario> all() {
		return prontuarioRepository.findAll();
	}

	public Prontuario salvar(Prontuario prontuario) {
		boolean novo = prontuario.isNovo();
		prontuario = prontuarioRepository.save(prontuario);
		if (novo)
			petService.anotar(prontuario.getPet().getId(), "Prontuário",
					"Prontuário cadastrado pelo usuário " + Controle.usuarioAtivo() + " ID:" + prontuario.getId(),
					prontuario.getId());
		return prontuario;
	}

	public Prontuario gerarByPet(Integer idPet) {
		Pet pet = petService.get(idPet);
		Prontuario prontuario = new Prontuario();
		prontuario.setPet(pet);
		return prontuario;
	}

	public boolean deletar(Integer id) {
		prontuarioRepository.deleteById(id);
		return true;
	}

}
