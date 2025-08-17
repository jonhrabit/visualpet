package px.main.veterinaria.modelos;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
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

@Data @AllArgsConstructor
@Entity
@Table(name = "prontuarios")
public class Prontuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
