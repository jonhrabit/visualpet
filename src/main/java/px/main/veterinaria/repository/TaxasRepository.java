package px.main.veterinaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.Taxa;

@Repository
public interface TaxasRepository extends JpaRepository<Taxa, Integer> {

	@Query("select u from Taxa u where u.tipo=?1 and u.vezes=?2")
	Taxa findByTipoAndVezes(Integer tipo, Integer vezes);
	
	@Query("select u.descricao from Taxa u GROUP BY u.descricao")
	List<String> findAllDescricao();
	
	List<Taxa> findByTipo(Integer tipo);

}
