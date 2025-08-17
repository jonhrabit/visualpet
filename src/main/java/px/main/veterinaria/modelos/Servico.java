package px.main.veterinaria.modelos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;
import px.main.veterinaria.modelos.enumeracoes.ServicoTipo;

@Data
@ToString
@Entity
@Table(name = "servicos")
public class Servico {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
