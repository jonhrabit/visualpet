package px.main.veterinaria.servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import px.main.veterinaria.modelos.Pacote;
import px.main.veterinaria.repository.PacoteRepository;

@Service
public class PacoteService {

	@Autowired
	PacoteRepository pacoteRepository;
	@Autowired
	AtendimentoService atendimentoService;
	@Autowired
	ClienteService clienteService;

	public List<Pacote> findByCliente(Integer cliente) {
		return pacoteRepository.findbyCliente(cliente);
	}

	public Optional<Pacote> get(Integer id) {
		return pacoteRepository.findById(id);
	}

	public Pacote salvar(Pacote pacote) {
		return pacoteRepository.save(pacote);
	}

	public void deletar(Integer id) {
		atendimentoService.excluirPacote(id);
		pacoteRepository.deleteById(id);
	}

	public List<Pacote> all() {
		return pacoteRepository.findAll();
	}

	public Pacote baixa(Integer id) {
		Pacote pacote = this.get(id).get();
		pacote.setPago(true);
		clienteService.anotar(pacote.getCliente().getId(), "Pacote", "Pacote de Serviços", pacote.getValor(),
				pacote.getId());
		return this.salvar(pacote);
	}

}
