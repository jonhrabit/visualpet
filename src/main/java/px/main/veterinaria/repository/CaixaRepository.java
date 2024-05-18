package px.main.veterinaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.Caixa;

@Repository
public interface CaixaRepository extends JpaRepository<Caixa, Integer> {

	public List<Caixa> findByDataBetween(final Date start, final Date end);

	public List<Caixa> findByRegistroBetween(final Date start, final Date end);

	public List<Caixa> findByDataBefore(final Date data);

	@Query("select u from Caixa u where u.data BETWEEN ?1 and ?2 and u.forma=?3 and u.texto like %?4% and u.tipo=?5")
	List<Caixa> searchByTipo(Date data1, Date data2, String forma, String pag, Integer tipo);

	@Query("select u from Caixa u where u.data BETWEEN ?1 and ?2 and u.texto like %?3% and u.tipo=?4")
	List<Caixa> searchByTipoNotForma(Date data1, Date data2, String pag, Integer tipo);

	@Query("select u from Caixa u where u.data BETWEEN ?1 and ?2 and u.forma=?3 and u.texto like %?4% Order By u.data ASC")
	List<Caixa> search(Date data1, Date data2, String forma, String pag);

	@Query("select u from Caixa u where u.data BETWEEN ?1 and ?2 and u.texto like %?3% Order By u.data ASC")
	List<Caixa> searchNotForma(Date data1, Date data2, String pag);

	@Query("SELECT SUM(u.valor) from Caixa u where u.data BETWEEN ?1 and ?2 and u.forma=?3 and u.texto like %?4%")
	double getTotal(Date data1, Date data2, Integer forma, String pag);

	@Query("SELECT SUM(u.valor) from Caixa u where u.data BETWEEN ?1 and ?2 and u.texto like %?3%")
	double getTotal(Date data1, Date data2, String pag);

	@Query("SELECT c from Caixa c WHERE c.codigoVenda is null AND c.tipo=1 AND c.forma>=2")
	List<Caixa> notCodigoVenda();

	List<Caixa> findByCodigoVenda(String codigoVenda);

	@Query("SELECT c.forma from Caixa c WHERE Year(data)=?1 GROUP BY c.forma")
	public List<String> formasDePagamentoDoAno(final Integer ano);

	@Query("SELECT c from Caixa c WHERE Year(data)=?1")
	public List<Caixa> findCaixaPorAno(final Integer ano);

	public List<Caixa> findByTipoAndValorBrutoIsNull(int tipo);
}
