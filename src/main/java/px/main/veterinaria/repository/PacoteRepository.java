package px.main.veterinaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.Pacote;

@Repository
public interface PacoteRepository extends JpaRepository<Pacote, Integer> {

	@Query("select u from Pacote u where u.cliente.id=?1")
	List<Pacote> findbyCliente(Integer cliente);

}
