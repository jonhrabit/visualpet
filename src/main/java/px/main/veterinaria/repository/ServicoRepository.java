package px.main.veterinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Integer> {
	
}
