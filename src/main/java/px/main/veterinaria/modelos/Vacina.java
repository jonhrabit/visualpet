package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
@Data @AllArgsConstructor @ToString
@Entity
@Table(name = "vacinas")
public class Vacina {
	@Id
	@GeneratedValue(strategy = IDENTITY)
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
