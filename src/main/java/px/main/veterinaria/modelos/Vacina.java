package px.main.veterinaria.modelos;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
@Data @AllArgsConstructor @ToString
@Entity
@Table(name = "vacinas")
public class Vacina {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date aplicacao;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date vigencia;
	
	@Basic(optional = false)
	@Column(nullable = false)
	private String nome;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "pet_id")
    private Pet pet;
	
	private boolean renovada;

	public Vacina() {
		this.id = 0;
		this.renovada=false;
		this.aplicacao = new Date();
	}

}
