package px.main.veterinaria.modelos;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PetInformacao extends Informacao {

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "pet_id")
	private Pet pet;

	public PetInformacao(String tipo, String texto, BigDecimal valor, Pet pet,Integer idLink) {
		super(tipo, texto, valor, idLink);
		this.pet = pet;
	}
	
	public PetInformacao() {
		super();
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	@Override
	public String toString() {
		return "PetInformacao [pet=" + pet.getNome() + ", tipo=" + this.getTipo() + ", texto=" + this.getTexto() + "]";
	}

}
