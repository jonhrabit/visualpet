package px.main.seguranca.modelos;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "grupos_regras")
public class GrupoRegra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "grupo_id")
	private Grupo grupo;

	private String regra;

	public GrupoRegra(Integer id, Grupo grupo, String regra) {
		this.id = id;
		this.grupo = grupo;
		this.regra = regra;
	}
	public GrupoRegra(String regra, Grupo grupo) {
		this.grupo = grupo;
		this.regra = regra;
	}

	public GrupoRegra() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public String getRegra() {
		return regra;
	}

	public void setRegra(String regra) {
		this.regra = regra;
	}
	
	@Override
	public boolean equals(Object obj) {
		 if (!(obj instanceof GrupoRegra))
	            return false;
	        if (obj == this)
	            return true;

	        GrupoRegra rhs = (GrupoRegra) obj;
	        if((id!= rhs.id) ||(regra!= rhs.regra)||(grupo!=rhs.grupo)){
	        	return false;
	        }
	        	return true;
	}
	
	

}
