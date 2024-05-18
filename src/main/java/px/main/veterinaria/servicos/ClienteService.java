package px.main.veterinaria.servicos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import px.main.controle.Controle;
import px.main.veterinaria.modelos.Cliente;
import px.main.veterinaria.modelos.ClienteInformacao;
import px.main.veterinaria.modelos.Pet;
import px.main.veterinaria.modelos.PetInformacao;
import px.main.veterinaria.repository.ClienteInformacaoRepository;
import px.main.veterinaria.repository.ClienteRepository;
import px.main.veterinaria.view.AtendimentoListaView;

@Service
public class ClienteService {
	@Autowired
	ClienteRepository clienteRepository;
	@Autowired
	PetService petService;
	@Autowired
	ClienteInformacaoRepository clienteInformacaoRepository;

	public Cliente anotar(Integer idCliente, String tipo, String texto, Integer IdLink) {
		Cliente cliente = this.get(idCliente);
		cliente.getInformacoes().add(new ClienteInformacao(tipo, texto, BigDecimal.ZERO, cliente, IdLink));
		System.out.println("Cliente anotação - " + texto);
		return clienteRepository.save(cliente);
	}

	public Cliente anotar(Integer idCliente, String tipo, String texto, BigDecimal valor, Integer IdLink) {
		Cliente cliente = this.get(idCliente);
		cliente.getInformacoes().add(new ClienteInformacao(tipo, texto, valor, cliente, IdLink));
		System.out.println("Cliente anotação");
		return clienteRepository.save(cliente);
	}

	public List<Cliente> All() {
		return clienteRepository.findAllByOrderByNomeAsc();
	}

	public Cliente get(Integer id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if (cliente.isEmpty()) {
			return new Cliente();
		} else {
			return cliente.get();
		}
	}

	public boolean delete(Integer id) {
		if (clienteRepository.findById(id).isEmpty())
			return false;

		for (Pet pet : clienteRepository.findById(id).get().getPets()) {
			petService.delete(pet.getId());
		}

		clienteRepository.deleteById(id);
		return true;
	}

	public boolean exist(Cliente cliente) {
		if (clienteRepository
				.findByNomeAndByTelefoneAndByCelular(cliente.getNome(), cliente.getTelefone(), cliente.getCelular())
				.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public Cliente salvar(Cliente cliente) {

		Cliente clienteAntigo = this.get(cliente.getId());
		if (cliente.isNovo()) {
			cliente.getInformacoes().add(new ClienteInformacao("Sistema",
					"Cadastro realizado por " + Controle.usuarioAtivo(), BigDecimal.ZERO, cliente, 0));

		} else {
			cliente.setInformacoes(clienteAntigo.getInformacoes());
			cliente.getInformacoes().add(new ClienteInformacao("Sistema",
					"Alteração realizada por " + Controle.usuarioAtivo(), BigDecimal.ZERO, cliente, 0));
		}
		for (Pet petCliente : cliente.getPets()) {
			petCliente.setTutor(cliente);
			if (!clienteAntigo.contemPet(petCliente.getNome()))
				cliente.getInformacoes()
						.add(new ClienteInformacao("Pet", "Pet " + petCliente.getNome(), BigDecimal.ZERO, cliente, 0));
		}
		return clienteRepository.save(cliente);
	}

	public List<Cliente> buscaNome(String nome) {
		return clienteRepository.searchNome(nome);
	}

	public Cliente criar(AtendimentoListaView atendimentoListaView) {
		Cliente cliente = this.get(atendimentoListaView.getIdTutor());
		if (cliente.isNovo()) {
			cliente = new Cliente(atendimentoListaView.getTutor(), atendimentoListaView.getPet(),
					atendimentoListaView.getCelular());
		} else {
			if (!cliente.contemPet(atendimentoListaView.getPet())) {
				Pet novoPet = new Pet(atendimentoListaView.getPet(), cliente);
				novoPet.getInformacoes().add(new PetInformacao("Sistema",
						"Pet cadastrado pelo usuário " + Controle.usuarioAtivo(), BigDecimal.ZERO, novoPet, 0));
				cliente.getInformacoes().add(new ClienteInformacao("Pet", "Pet " + atendimentoListaView.getPet(),
						BigDecimal.ZERO, cliente, 0));
				cliente.getPets().add(novoPet);
			}
		}
		return clienteRepository.save(cliente);
	}
}
