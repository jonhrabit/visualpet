package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import px.main.controle.Controle;

@Getter
@Setter
@Entity
@Table(name = "clientes")
public class Cliente {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@Basic(optional = false)
	@Column(nullable = false)
	private String nome;

	private String rg, cpf, endereco, bairro, cidadeUF, telefone, celular, email, responsavel2, telefone2, responsavel3,
			telefone3, Cep;

	@Column(columnDefinition = "text")
	private String obs;

	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Pet> pets;

	@OneToMany(mappedBy = "cliente")
	@JsonIgnore
	private List<Cesta> compras;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ClienteInformacao> informacoes;

	public Cliente(Integer id, String nome, String rg, String cpf, String endereco, String bairro, String cidadeUF,
			String telefone, String celular, String email, String responsavel2, String telefone2, String responsavel3,
			String telefone3, Date data, List<Pet> pets, List<ClienteInformacao> informacoes, String obs) {
		super();
		this.id = id;
		this.nome = nome;
		this.rg = rg;
		this.cpf = cpf;
		this.endereco = endereco;
		this.bairro = bairro;
		this.cidadeUF = cidadeUF;
		this.telefone = telefone;
		this.celular = celular;
		this.email = email;
		this.responsavel2 = responsavel2;
		this.telefone2 = telefone2;
		this.responsavel3 = responsavel3;
		this.telefone3 = telefone3;
		this.data = data;
		this.pets = pets;
		this.informacoes = informacoes;
		this.obs = obs;
	}

	public Cliente() {
		this.id = 0;
		this.data = new Date();
		this.pets = new ArrayList<Pet>();
		this.informacoes = new ArrayList<ClienteInformacao>();
	}

	public Cliente(String nome, String pet, String celular) {
		this.id = 0;
		this.data = new Date();
		this.nome = nome;
		this.celular = celular;
		this.pets = new ArrayList<Pet>();
		this.pets.add(new Pet(pet, this));
		this.pets.get(0).getInformacoes().add(new PetInformacao("Sistema",
				"Pet cadastrado pelo usuário " + Controle.usuarioAtivo(), BigDecimal.ZERO, this.pets.get(0), 0));
		this.informacoes = new ArrayList<ClienteInformacao>();
		this.informacoes.add(new ClienteInformacao("Sistema",
				"Cliente cadastrado pelo usuário " + Controle.usuarioAtivo(), BigDecimal.ZERO, this, 0));
		this.informacoes.add(new ClienteInformacao("Pet", "Pet " + pet, BigDecimal.ZERO, this, 0));
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nome=" + nome + ", rg=" + rg + ", cpf=" + cpf + ", endereco=" + endereco
				+ ", bairro=" + bairro + ", cidadeUF=" + cidadeUF + ", telefone=" + telefone + ", celular=" + celular
				+ ", data=" + data + "]";
	}

	public boolean contemPet(String nome) {
		for (Pet pet : this.pets) {
			if (pet.compararNomes(nome))
				return true;
		}
		return false;
	}

	public Pet getPet(String nome) {
		for (Pet pet : this.pets) {
			if (pet.compararNomes(nome))
				return pet;
		}
		return new Pet();
	}

	public boolean isNovo() {
		if (this.id == null)
			return true;
		if (this.id == 0)
			return true;
		return false;
	}

	public void nullFields() {
		if (this.getNome() != null && this.getNome().isBlank())
			this.nome = null;
		if (this.getRg() != null && this.getRg().isBlank())
			this.rg = null;
		if (this.getCpf() != null && this.getCpf().isBlank())
			this.cpf = null;
		if (this.getEndereco() != null && this.getEndereco().isBlank())
			this.endereco = null;
		if (this.getBairro() != null && this.getBairro().isBlank())
			this.bairro = null;
		if (this.getCidadeUF() != null && this.getCidadeUF().isBlank())
			this.cidadeUF = null;
		if (this.getTelefone() != null && this.getTelefone().isBlank())
			this.telefone = null;
		if (this.getCelular() != null && this.getCelular().isBlank())
			this.celular = null;
		if (this.getEmail() != null && this.getEmail().isBlank())
			this.email = null;
	}

}
