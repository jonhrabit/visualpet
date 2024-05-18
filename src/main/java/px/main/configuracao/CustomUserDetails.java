package px.main.configuracao;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import px.main.seguranca.modelos.Usuario;

public class CustomUserDetails extends Usuario implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private List<String> userRoles;

	public CustomUserDetails(Usuario u, List<String> userRoles) {
		this.userRoles = userRoles;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		String roles = StringUtils.collectionToCommaDelimitedString(userRoles);
		return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public String getPassword() {
		return super.getSenha();
	}

	public String getUsername() {
		return super.getLogin();
	}

}