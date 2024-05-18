package px.main.veterinaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import px.main.veterinaria.modelos.Estoque;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {

	@Transactional
	@Modifying
	@Query("DELETE FROM Estoque e where e.produto.id=?1")
	public void deleteByProdutoId(Integer id);

}
