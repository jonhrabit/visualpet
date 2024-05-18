package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;
import px.main.veterinaria.modelos.enumeracoes.ServicoTipo;

@Data
@ToString
@Entity
@Table(name = "servicos")
public class Servico {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	private String texto;
	private ServicoTipo tipo;

	@OneToMany(mappedBy = "servico")
	@ToString.Exclude
	@JsonIgnore
	private List<Atendimento> atendimentos;

	Servico() {
		this.texto = "";
		this.tipo = ServicoTipo.ESTETICA;
	}

}
