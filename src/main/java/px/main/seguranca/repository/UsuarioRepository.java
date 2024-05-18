package px.main.seguranca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import px.main.seguranca.modelos.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	public Optional<Usuario> findByLogin(String login);
	
	@Query("select u.senha from Usuario u where u.login =?1")
	public String getSenhaPublica(String login);
}
