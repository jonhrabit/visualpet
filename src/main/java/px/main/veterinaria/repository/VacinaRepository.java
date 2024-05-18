package px.main.veterinaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import px.main.veterinaria.modelos.Vacina;

@Repository
public interface VacinaRepository extends JpaRepository<Vacina, Integer> {

	@Query("select v from Vacina v where v.vigencia<=?1 and v.renovada=false")
	public List<Vacina> vencidas(Date data);

	@Query("select v from Vacina v where v.vigencia BETWEEN ?1 and ?2 Order By v.vigencia")
	public List<Vacina> findByVigencia(Date data1, Date data2);

	@Query("select v from Vacina v where v.pet.id=?1 Order By v.aplicacao")
	List<Vacina> findByPetId(Integer petId);

	@Transactional
	@Modifying
	@Query("DELETE FROM Vacina a where a.pet.id=?1")
	public void deleteByPet(Integer id);

}
