package px.main.seguranca.servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import px.main.seguranca.modelos.Usuario;
import px.main.seguranca.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	UsuarioRepository usuarioRepository;

	public List<Usuario> All() {
		return usuarioRepository.findAll();
	}

	public Usuario salvar(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	public boolean existeForLogin(String login) {
		if (usuarioRepository.findByLogin(login).isEmpty()) {
			return false;
		} else {
			return false;
		}
	}
	public Usuario get(Integer id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isEmpty()) {
			return new Usuario();
		}
		return usuario.get();
	}
	public Usuario get(String login) {
		Optional<Usuario> usuario = usuarioRepository.findByLogin(login);
		if (usuario.isEmpty()) {
			return new Usuario();
		}
		return usuario.get();
	}
	public boolean deletar(Integer id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isEmpty()) {
			return false;
		}
		usuarioRepository.deleteById(id);
		return true;
	}
	public String getSenhaPublic(String login) {
		return usuarioRepository.getSenhaPublica(login);
	}

}
