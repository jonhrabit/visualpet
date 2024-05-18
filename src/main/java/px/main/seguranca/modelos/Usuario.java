package px.main.seguranca.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@Basic(optional = false)
	@Column(nullable = false)
	private String login;

	@Basic(optional = false)
	@Column(nullable = false)
	private String nome;

	private String senha, setor, obs;
	private boolean enabled;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "usuario", orphanRemoval = true)
	private List<UsuarioRegra> regras;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "grupos_usuarios", joinColumns = { @JoinColumn(name = "grupo_id") }, inverseJoinColumns = {
			@JoinColumn(name = "usuario_id") })
	private List<Grupo> grupos;

	public Usuario(Integer id, String login, String nome, String senha, String setor, String obs, boolean enabled,
			List<UsuarioRegra> regras) {
		this.id = id;
		this.login = login;
		this.nome = nome;
		this.senha = senha;
		this.setor = setor;
		this.obs = obs;
		this.enabled = enabled;
		this.regras = regras;
		this.grupos = new ArrayList<Grupo>();
	}

	public Usuario() {
		this.id = 0;
		this.enabled = true;
		this.regras = new ArrayList<UsuarioRegra>();
		this.grupos = new ArrayList<Grupo>();

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isNovo() {
		if (this.id == 0)
			return true;
		return false;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.senha = bCryptPasswordEncoder.encode(senha);
	}

	public void setSenhaPublica(String senha) {
		this.senha = senha;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<UsuarioRegra> getRegras() {
		return regras;
	}

	public void setRegras(List<UsuarioRegra> regras) {
		this.regras = regras;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", login=" + login + ", nome=" + nome + ", senha=" + senha + ", setor=" + setor
				+ ", obs=" + obs + ", enabled=" + enabled + ", regras=" + regras + "]";
	}
}
