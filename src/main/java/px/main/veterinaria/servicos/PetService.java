package px.main.veterinaria.servicos;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import px.main.seguranca.servicos.UsuarioLogado;
import px.main.veterinaria.modelos.Cliente;
import px.main.veterinaria.modelos.Pet;
import px.main.veterinaria.modelos.PetInformacao;
import px.main.veterinaria.modelos.Vacina;
import px.main.veterinaria.repository.AtendimentoRepository;
import px.main.veterinaria.repository.PetRepository;
import px.main.veterinaria.repository.VacinaRepository;

@Service
public class PetService {
	@Autowired
	PetRepository petRepository;
	@Autowired
	VacinaRepository vacinaRepository;
	@Autowired
	AtendimentoRepository atendimentoRepository;

	public List<Pet> All() {
		return petRepository.findAllByOrderByNomeAsc();
	}

	public List<Pet> buscaNome(String nome) {
		return petRepository.findByNomeLikeOrderByNomeAsc(nome);
	}

	public Pet get(Integer id) {
		Optional<Pet> pet = petRepository.findById(id);
		if (pet.isEmpty()) {
			return new Pet();
		} else {
			return pet.get();
		}
	}

	public boolean exist(Pet pet) {
		Optional<Pet> petConsulta = petRepository.findByNomeAndByTutor(pet.getNome(), pet.getTutor().getNome());
		if (petConsulta.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean delete(Integer id) {
		if (petRepository.findById(id).isEmpty())
			return false;

		atendimentoRepository.deleteByPet(id);
		vacinaRepository.deleteByPet(id);
		petRepository.deleteById(id);
		return true;
	}

	public Pet salvar(Pet pet) {
		if (pet.isNovo()) {
			pet.getInformacoes().add(new PetInformacao("Sistema",
					"Pet cadastrado pelo usuário " + UsuarioLogado.get(), BigDecimal.ZERO, pet, 0));

		} else {
			Pet petAntigo = this.get(pet.getId());
			pet.setInformacoes(petAntigo.getInformacoes());
			pet.getInformacoes().add(new PetInformacao("Sistema",
					"Pet alterado pelo usuário " + UsuarioLogado.get(), BigDecimal.ZERO, pet, 0));
		}
		return petRepository.save(pet);
	}

	public Pet anotar(Integer idPet, String tipo, String texto, Integer idLink) {
		Pet pet = this.get(idPet);
		pet.getInformacoes().add(new PetInformacao(tipo, texto, BigDecimal.ZERO, pet, idLink));
		return petRepository.save(pet);
	}

	public Pet anotar(Integer idPet, String tipo, String texto, BigDecimal valor, Integer idLink) {
		Pet pet = this.get(idPet);
		pet.getInformacoes().add(new PetInformacao(tipo, texto, valor, pet, idLink));
		return petRepository.save(pet);
	}

	public Pet vacinar(Integer idPet, Date data, String vacinaNome, Date vigencia) {
		Pet pet = this.get(idPet);
		for (Vacina vac : pet.getVacinas()) {
			if (!vac.isRenovada()) {
				if (vac.getNome().equals(vacinaNome)) {
					vac.setRenovada(true);
					vacinaRepository.save(vac);
				}
			}
		}
		Vacina vacina = new Vacina(0, data, vigencia, vacinaNome, pet, false);
		vacinaRepository.save(vacina);
		SimpleDateFormat x = new SimpleDateFormat("dd/MM/yyyy");
		this.anotar(idPet, "Vacina", vacinaNome + " - Vigência até " + x.format(vigencia), 0);
		return petRepository.save(pet);
	}

	public boolean deleteVacina(Integer petId, Integer vacinaId) {
		Pet pet = this.get(petId);
		for (Vacina v : pet.getVacinas()) {
			if (v.getId().equals(vacinaId)) {
				this.anotar(petId, "Vacina", v.getNome() + " - excluida pelo usuário " + UsuarioLogado.get(), 0);
				vacinaRepository.delete(v);
				return true;
			}
		}
		return false;
	}

	public boolean renovarVacina(Integer vacinaId, Integer petId) {
		Pet pet = this.get(petId);
		for (Vacina v : pet.getVacinas()) {
			if (v.getId().equals(vacinaId)) {
				v.setRenovada(true);
				vacinaRepository.save(v);
				return true;
			}
		}
		return false;
	}

	public List<Pet> buscarByTutorId(Integer id) {
		return petRepository.findByTutorByOrderNome(id);
	}

	public List<Pet> buscarByNomeLike(String nome) {
		return petRepository.findByNomeLikeOrderByNomeAsc(nome);
	}

	public List<Cliente> buscarTutoresByNomePet(String nome) {
		return petRepository.findTutorByNomePet(nome);
	}

	public List<Vacina> buscarByPetId(Integer id) {
		return vacinaRepository.findByPetId(id);
	}

}
