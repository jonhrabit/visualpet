package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
public class Informacao {
	@Id
	@GeneratedValue(strategy = IDENTITY)
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
