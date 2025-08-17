package px.main.veterinaria.modelos;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
public class Informacao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	private String tipo,texto;
	private BigDecimal valor;
	private Integer IdLink;

	public Informacao(String tipo, String texto, BigDecimal valor, Integer idLink) {
		this.id=0;
		this.data = new Date();
		this.tipo = tipo;
		this.texto = texto;
		this.valor=valor;
		this.IdLink=idLink;
	}

	public Informacao() {
		this.id=0;
		this.data = new Date();
		this.valor=new BigDecimal(0);
	}	
}
