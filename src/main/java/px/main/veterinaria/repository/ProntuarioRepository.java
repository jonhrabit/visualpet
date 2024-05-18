package px.main.veterinaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.Prontuario;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Integer> {

	@Query("select u from Prontuario u where u.pet.nome like %?1%")
	public List<Prontuario> findByPetLike(String pet);

	public List<Prontuario> findAll();

}
