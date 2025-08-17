package px.main.seguranca.modelos;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
@Table(name = "grupos")
public class Grupo {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String nome;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "grupo")
	private List<GrupoRegra> regras;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "grupos_usuarios", joinColumns = { @JoinColumn(name = "grupo_id") }, inverseJoinColumns = { @JoinColumn(name = "usuario_id") })
	private List<Usuario> usuarios;

	public Grupo(int id, String nome, List<GrupoRegra> regras,
			List<Usuario> usuarios) {
		this.id = id;
		this.nome = nome;
		this.regras = regras;
		this.usuarios = usuarios;
	}

	public Grupo() {
		regras=new ArrayList<GrupoRegra>();
		usuarios=new ArrayList<Usuario>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<GrupoRegra> getRegras() {
		return regras;
	}

	public void setRegras(List<GrupoRegra> regras) {
		this.regras = regras;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public void getUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Grupo))
			return false;
		if (obj == this)
			return true;

		Grupo rhs = (Grupo) obj;
		if ((id != rhs.id) || (nome != rhs.nome) || (regras != rhs.regras)
				|| (usuarios != rhs.usuarios)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "{id:%s, nome:%s, regras:%s, usuarios:%s}".formatted(id,
                nome, regras, usuarios);
	}


	public boolean regrasContains(String v) {
		if(regras.size()==0) return false;
		for (GrupoRegra gr : regras) {
			if (gr.getRegra().equals(v)) {
				return true;
			}
		}
		return false;
	}
	public boolean usuariosContains(String v) {
		for (Usuario gr : usuarios) {
			if (gr.getLogin().equals(v)) {
				return true;
			}
		}
		return false;
	}

	public void regrasRemove(String v) {
		System.out.println(regras.size());
		for(int i=0;i<regras.size();i++){
			if (regras.get(i).getRegra().equals(v)) {
				System.out.println(regras.get(i).getRegra());
				this.regras.remove(i);
			}
		}
	}
	public void usuariosRemove(String v) {
		for(int i=0;i<usuarios.size();i++){
			if (usuarios.get(i).getLogin().equals(v)) {
				this.usuarios.remove(i);
			}
			
		}
	}

}
