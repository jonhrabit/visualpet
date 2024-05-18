package px.main.veterinaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.Produto;


@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	public Optional<Produto> findByCodigo(String codigo);
	public Optional<Produto> findByNcm(String ncm);
	

	  @Query("Select p from Produto p where p.nome like %?1%")
	public List<Produto> consultaByNome(String nome);
	
	  @Query("Select p from Produto p where p.quantidade >= 1 ORDER BY p.nome ASC")
	  public List<Produto> findByQuantidadeGreaterThanEqualOrderByNomeAsc();
	
	  @Query("Select p from Produto p where p.quantidade<p.quantidadeMinima ORDER BY p.nome ASC")
	  public List<Produto> Aquisicao();
	  
	  @Query("Select p.tipo from Produto p GROUP BY p.tipo")
	  public List<String> listaProdutos();
	  
	  public Page<Produto> findAll(Pageable pageable);
	  
	  @Query("Select p from Produto p where p.nome like %?1%")
	  public Page<Produto> consultar(String nome, Pageable pageable);
	  
	  @Query("Select p from Produto p where p.nome like %?1% AND p.tipo=?2")
	  public Page<Produto> consultar(String nome, String tipo, Pageable pageable);
}
