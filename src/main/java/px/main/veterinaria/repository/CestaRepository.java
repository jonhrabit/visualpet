package px.main.veterinaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.Cesta;

@Repository
public interface CestaRepository extends JpaRepository<Cesta, Integer> {

	public List<Cesta> findByDataAndNfNull(Date data);

	public List<Cesta> findByData(Date data);

	@Query("select u from Cesta u " + "where u.data BETWEEN ?1 and ?2 ")
	List<Cesta> searchBetween(Date data1, Date data2);

	@Query("select u from Cesta u, ProdutoCesta p " + "where u.data BETWEEN ?1 and ?2 AND p.tipo=?3")
	List<Cesta> search(Date data1, Date data2, String tipo);

	@Query("select u from Cesta u INNER JOIN ProdutoCesta p ON p.cesta=u where p.idProdutoOriginal=?1 and p.tipo!='servico'")
	List<Cesta> findJoinByProdutosCestaId(Integer id);

	public List<Cesta> findByDataBetween(Date inicialDate, Date finalDate);

	@Query("select u from Cesta u where YEAR(u.data)=?1")
	public List<Cesta> findCestasAno(Integer ano);

}
