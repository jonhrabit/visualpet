package px.main.veterinaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.PetInformacao;

@Repository
public interface PetInformacaoRepository extends JpaRepository<PetInformacao, Integer> {

	public List<PetInformacao> findByDataBetween(final Date start, final Date end);

	public List<PetInformacao> findByData(Date D);

	public void deleteByPetId(Integer petId);

}
