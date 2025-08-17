package px.main.configuracao;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.extern.slf4j.Slf4j;
import px.main.seguranca.modelos.Usuario;
import px.main.seguranca.servicos.UsuarioService;

@Slf4j
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario user = usuarioService.get(username);
		if (user.isNovo())
			throw new UsernameNotFoundException(username);

		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		user.getRegras()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRegra()))
				.forEach(grantedAuthorities::add);

		log.info("User: " + user.getLogin() + " with roles: " + grantedAuthorities);

		// Return a User object with the username, password, and authorities
		return new User(user.getLogin(), user.getSenha(), grantedAuthorities);
	}

}
