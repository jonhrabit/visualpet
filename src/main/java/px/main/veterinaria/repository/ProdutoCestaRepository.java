package px.main.veterinaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.ProdutoCesta;

@Repository
public interface ProdutoCestaRepository extends JpaRepository<ProdutoCesta, Integer> {
	public List<ProdutoCesta> findByCodigoOrderByNomeAsc(String codigo);

	@Query("Select p from ProdutoCesta p where p.nome like %?1%")
	public List<ProdutoCesta> findByNomeLikeOrderByNomeAsc(String nome);

	@Query("Select p from ProdutoCesta p where p.idProdutoOriginal=?1 and tipo!='servico'")
	public List<ProdutoCesta> findByIdProdutoOriginal(Integer idProdutoOriginal);

}
