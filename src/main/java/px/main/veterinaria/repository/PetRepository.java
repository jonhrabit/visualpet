package px.main.veterinaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import px.main.veterinaria.modelos.Cliente;
import px.main.veterinaria.modelos.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
	public Pet findById(int id);

	@Query("Select c from Pet c where c.nome like %?1% order by c.nome")
	public List<Pet> findByNomeLikeOrderByNomeAsc(String nome);

	@Query("Select c from Pet c where c.nome=?1 and c.tutor.nome=?2")
	public Optional<Pet> findByNomeAndByTutor(String nome, String tutor);

	public List<Pet> findAllByOrderByNomeAsc();

	@Query("Select c from Pet c where c.tutor.id=?1")
	public List<Pet> findByTutorByOrderNome(Integer idTutor);

	@Query("Select c.tutor from Pet c where c.nome like %?1% order by c.nome")
	public List<Cliente> findTutorByNomePet(String nome);

	@Transactional
	@Modifying
	public void deleteById(Integer id);

}
