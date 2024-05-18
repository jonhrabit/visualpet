package px.main.veterinaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import px.main.veterinaria.modelos.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	public Cliente findById(int id);
	@Query("Select c from Cliente c where c.nome like %?1% order by c.nome")
	public List<Cliente> findByNomeLikeOrderByNomeAsc(String nome);

	@Query("Select c from Cliente c where c.nome=?1 and (c.telefone=?2 or c.celular like ?3)")
	public Optional<Cliente> findByNomeAndByTelefoneAndByCelular(String nome,String telefone,String celular);
	
	public List<Cliente> findAllByOrderByNomeAsc();

	public Optional<Cliente> findByCelular(String celular);
	
	public Optional<Cliente> findByNome(String nome);

	@Query("Select c from Cliente c where c.nome like %?1% order by c.nome ASC")
	public List<Cliente> searchNome(String nome);
	
	public void deleteById(Integer id);
	
}
