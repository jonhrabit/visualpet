package px.main.veterinaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.ProdutoAtendimento;

@Repository
public interface ProdutoAtendimentoRepository extends JpaRepository<ProdutoAtendimento, Integer> {
	@Query("Select p from ProdutoCesta p where p.nome like %?1%")
	public List<ProdutoAtendimento> findByNomeLikeOrderByNomeAsc(String nome);

}
