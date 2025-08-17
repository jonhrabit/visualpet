package px.main.seguranca.modelos;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Basic(optional = false)
	@Column(nullable = false)
	private String login;

	@Basic(optional = false)
	@Column(nullable = false)
	private String nome;

	private String senha, setor, obs;
	private boolean enabled;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "usuario", orphanRemoval = true)
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

	public void setSenha(String senha) {
		this.senha = senha;
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
