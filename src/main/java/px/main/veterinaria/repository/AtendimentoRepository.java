package px.main.veterinaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import px.main.veterinaria.modelos.Atendimento;
import px.main.veterinaria.modelos.Servico;
import px.main.veterinaria.modelos.enumeracoes.ServicoTipo;
import px.main.veterinaria.modelos.enumeracoes.SituacaoAtendimento;
import px.main.veterinaria.query.modelos.AtendimentosPorCliente;
import px.main.veterinaria.query.modelos.AtendimentosPorServico;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {

	public List<Atendimento> findByDatafimNullOrderByAgendaAsc();

	@Query("Select c from Atendimento c where c.pet.nome=?1 and c.datainicio=?2")
	public Atendimento findByPetAndByDatainicio(String petnome, Date datainicio);

	@Query("select u from Atendimento u where u.agenda>=?1 and u.agenda<=?2 and u.servico=?3 and u.situacao=?4 ORDER BY u.agenda ASC")
	List<Atendimento> search(Date data1, Date data2, Servico servico, SituacaoAtendimento situacao);

	@Query("select u from Atendimento u where u.agenda>=?1 and u.agenda<=?2 and u.situacao=?3 ORDER BY u.agenda ASC")
	List<Atendimento> search(Date data1, Date data2, SituacaoAtendimento situacao);

	@Query("select u from Atendimento u where u.agenda>=?1 and u.agenda<=?2 and u.servico = ?3 ORDER BY u.agenda ASC")
	List<Atendimento> search(Date data1, Date data2, Servico servico);

	@Query("select u from Atendimento u where Year(u.agenda)=?1 ORDER BY u.agenda ASC")
	List<Atendimento> atendimentosAno(Integer ano);

	@Query("select u from Atendimento u where u.agenda>=?1 and u.agenda<=?2 ORDER BY u.agenda ASC")
	List<Atendimento> search(Date data1, Date data2);

	@Query("select u from Atendimento u where (u.agenda>=?1 and u.agenda<=?2) or (u.situacao<='1' and u.situacao!='4') ORDER BY u.agenda ASC")
	List<Atendimento> semanaAndNaoFinalizados(Date date, Date date2);

	@Query("select u from Atendimento u where ((u.agenda>=?1 and u.agenda<=?2) or (u.situacao<='1' and u.situacao!='4')) and (u.servico.tipo=?3) ORDER BY u.agenda ASC")
	List<Atendimento> semanaToService(Date date, Date date2, ServicoTipo servicoTipo);

	@Query("select u from Atendimento u where u.situacao>='4' ORDER BY u.agenda ASC")
	public List<Atendimento> findPendentes();

	@Query("SELECT "
			+ "    new px.main.veterinaria.query.modelos.AtendimentosPorCliente(v.pet.tutor.nome,v.pet.nome, COUNT(v)) "
			+ "FROM " + "    Atendimento v " + "WHERE v.agenda between ?1 and ?2 GROUP BY v.pet")
	public List<AtendimentosPorCliente> queryPorCliente(Date inicialDate, Date finalDate);

	@Query("SELECT new px.main.veterinaria.query.modelos.AtendimentosPorServico(COUNT(a.id),a.servico.texto) FROM Atendimento a"
			+ " where a.agenda between ?1 and ?2 and a.situacao=2 GROUP BY a.servico")
	public List<AtendimentosPorServico> queryAtendimentosPorServico(Date inicialDate, Date finalDate);

	@Transactional
	@Modifying
	@Query("DELETE FROM Atendimento a where a.pet.id=?1")
	public void deleteByPet(Integer id);

	@Query("SELECT u from Atendimento u where u.pacote.id=?1")
	public List<Atendimento> findByPacoteId(Integer pacote_id);

}
