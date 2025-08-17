package px.main.veterinaria.modelos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import px.main.veterinaria.modelos.enumeracoes.SituacaoAtendimento;

@Data
@AllArgsConstructor
@Entity
@Table(name = "atendimentos")
public class Atendimento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pet_id")
	private Pet pet;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date agenda;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date datainicio;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date datafim;

	@ManyToOne
	@JoinColumn(name = "servico")
	private Servico servico;

	private String texto;
	private SituacaoAtendimento situacao;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pacote_id")
	private Pacote pacote;

	@OneToMany(mappedBy = "atendimento", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ProdutoAtendimento> produtos;

	private boolean venda;

	@Basic(optional = false)
	@Column(nullable = false)
	private BigDecimal valor;

	public Atendimento() {
		this.id = 0;
		this.valor = new BigDecimal(0);
		Calendar c = Calendar.getInstance();
		this.situacao = SituacaoAtendimento.AGENDA;
		this.agenda = c.getTime();
		this.pet = new Pet();
		this.produtos = new ArrayList<ProdutoAtendimento>();
	}

	public Atendimento(Date agenda, Pet pet, Servico servico, BigDecimal valor) {
		this.id = 0;
		this.valor = valor;
		this.situacao = SituacaoAtendimento.AGENDA;
		this.servico = servico;
		this.agenda = agenda;
		this.pet = pet;
		this.venda = false;
	}

	public Integer getAgendaMes() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.agenda);
		return c.get(Calendar.MONTH);
	}

	public boolean isFinalizado() {
		if (this.situacao.equals(SituacaoAtendimento.FINALIZADO))
			return true;
		return false;
	}

	public boolean contemProduto(Integer id) {
		if (this.produtos.isEmpty())
			return false;
		for (ProdutoAtendimento produto : this.getProdutos()) {
			if (produto.getIdProdutoOriginal().equals(id))
				return true;
		}
		return false;
	}

	public ProdutoAtendimento getProdutoForIdOriginal(Integer id) {
		for (ProdutoAtendimento produto : this.getProdutos()) {
			if (produto.getIdProdutoOriginal().equals(id))
				return produto;
		}
		return null;
	}

	public boolean finalizar(BigDecimal valor, Integer formaPagamento) {
		this.datafim = new Date();
		this.situacao = SituacaoAtendimento.FINALIZADO;
		this.valor = valor;
		return true;
	}

	public BigDecimal getTotalProdutos() {
		BigDecimal soma = BigDecimal.ZERO;
		for (ProdutoAtendimento prod : this.produtos) {
			soma = soma.add(prod.getPreco().multiply(new BigDecimal(prod.getQuantidade())));
		}
		return soma;
	}

	@Override
	public String toString() {
		return "Atendimento [id=" + id + ", pet=" + pet + ", agenda=" + agenda + ", servico=" + servico + ", situacao="
				+ situacao + ", produtos=" + produtos + ", valor=" + valor + "]";
	}

	public boolean isNovo() {
		if ((this.id == 0) || (this.id == null))
			return true;
		return false;
	}

}
