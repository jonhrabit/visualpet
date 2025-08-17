package px.main.veterinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.ClienteInformacao;

@Repository
public interface ClienteInformacaoRepository extends JpaRepository<ClienteInformacao, Integer> {
	public ClienteInformacao findById(int id);
	public ClienteInformacao findByClienteId(int clienteId);
}
