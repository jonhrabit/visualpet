package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.CascadeType;
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

@Data @AllArgsConstructor
@Entity
@Table(name = "prontuarios")
public class Prontuario {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "pet_id")
	private Pet pet;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@Column(columnDefinition = "text")
	private String queixas, examesfisicos, terapeutica, exames, suspeitas;
	
	private String usuario;
	
	public Prontuario() {
		this.data=new Date();
		this.id=0;
	}

	public boolean isNovo() {
		if(this.id==0)
			return true;
		return false;
	}

}
