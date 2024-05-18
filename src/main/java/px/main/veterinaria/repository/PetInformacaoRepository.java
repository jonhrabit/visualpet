package px.main.veterinaria.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.PetInformacao;

@Repository
public interface PetInformacaoRepository extends JpaRepository<PetInformacao, Integer> {
	public Optional<PetInformacao> findById(Integer id);

	public List<PetInformacao> findByDataBetween(final Date start, final Date end);

	public List<PetInformacao> findByData(Date D);

	public void deleteByPet(Integer petId);

}
