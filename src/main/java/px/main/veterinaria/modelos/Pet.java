package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "pets")
public class Pet {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@Basic(optional = false)
	@Column(nullable = false)
	private String nome;

	private String especie, raca, pelagem, obs;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date nascimento;

	private int sexo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	private Cliente tutor;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet")
	@JsonIgnore
	private List<PetInformacao> informacoes;

	@OneToMany(mappedBy = "pet")
	@JsonIgnore
	private List<Vacina> vacinas;

	@OneToMany(mappedBy = "pet")
	@JsonIgnore
	private List<Atendimento> atendimento;

	public Pet(Integer id, String nome, String especie, String raca, String pelagem, String obs, Date nascimento,
			int sexo, Cliente tutor, List<PetInformacao> informacoes, List<Vacina> vacinas) {
		super();
		this.id = id;
		this.nome = nome;
		this.especie = especie;
		this.raca = raca;
		this.pelagem = pelagem;
		this.obs = obs;
		this.nascimento = nascimento;
		this.sexo = sexo;
		this.tutor = tutor;
		this.informacoes = informacoes;
		this.vacinas = vacinas;
	}

	public Pet() {
		this.id = 0;
		this.especie = "Cão";
		this.nascimento = null;
		this.informacoes = new ArrayList<PetInformacao>();
		this.vacinas = new ArrayList<Vacina>();
		this.atendimento = new ArrayList<Atendimento>();
	}

	public Pet(String nome, Cliente tutor) {
		this.id = 0;
		this.nascimento = null;
		this.especie = "Cão";
		this.nome = nome;
		this.tutor = tutor;
		this.informacoes = new ArrayList<PetInformacao>();
	}
	public boolean isNovo() {
		if ((this.id == 0) || (this.id == null))
			return true;
		return false;
	}
	@Override
	public String toString() {
		return "Pet [id=" + id + ", nome=" + nome + ", especie=" + especie + ", raca=" + raca + ", pelagem=" + pelagem
				+ ", obs=" + obs + ", nascimento=" + nascimento + ", sexo=" + sexo + ", tutor=" + tutor + "]";
	}

	public boolean compararNomes(String nome) {
		return this.nome.equals(nome);
	}

	public String[] getFotos(String pathFotos) {
		File f = new File(pathFotos + File.separator + this.id);
		String[] nomes = f.list();
		return nomes;
	}

}
