package px.main.veterinaria.modelos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome, codigo, tipo, laboratorio, ncm;

	private Integer quantidade, fracao;
	private BigDecimal precoCompra;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date validade;

	@Column(columnDefinition = "text")
	private String descricao;

	private Integer quantidadeMinima, quantidadeMaxima;
	private BigDecimal precoVenda;

	@OneToMany(mappedBy = "produto")
	private List<Estoque> estoque;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isNovo() {
		if ((this.id == 0) || (this.id == null))
			return false;
		return true;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isVacina() {
		if (this.tipo.equals("vacina"))
			return true;
		return false;
	}

	public boolean isProduto() {
		if (this.tipo.equals("produto"))
			return true;
		return false;
	}

	public String getLaboratorio() {
		return laboratorio;
	}

	public void setLaboratorio(String laboratorio) {
		this.laboratorio = laboratorio;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQuantidade() {
		Integer i = 0;
		for (Estoque e : this.getEstoque()) {
			i = i + e.getQuantidade();
		}
		return i;
	}

	public Integer getQ() {
		return this.quantidade;
	}

	public Integer getQuantidadeMinima() {
		return quantidadeMinima;
	}

	public void setQuantidadeMinima(Integer quantidadeMinima) {
		this.quantidadeMinima = quantidadeMinima;
	}

	public Integer getQuantidadeMaxima() {
		return quantidadeMaxima;
	}

	public void setQuantidadeMaxima(Integer quantidadeMaxima) {
		this.quantidadeMaxima = quantidadeMaxima;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public String getNcm() {
		return ncm;
	}

	public void setNcm(String ncm) {
		this.ncm = ncm;
	}

	public Integer getFracao() {
		return this.fracao;
	}

	public void setFracao(Integer fracao) {
		this.fracao = fracao;
	}

	public Date getValidadeMenor() {
		if (this.estoque.isEmpty())
			return null;
		Date dataMenor = new Date();
		Boolean controle = true;
		for (Estoque e : this.estoque) {
			if (e.getValidade() != null) {
				if (dataMenor.compareTo(e.getValidade()) < 0) {
					dataMenor = e.getValidade();
					controle = false;
				}
			}
		}
		if (controle)
			return null;
		return dataMenor;
	}

	@Override
	public String toString() {
		return "Produto [nome=" + nome + ", laboratorio=" + laboratorio + "]";
	}

	public ProdutoCesta copyToProdutoCesta() {
		ProdutoCesta produtoCesta = new ProdutoCesta();
		produtoCesta.setNome(this.nome);
		produtoCesta.setTipo(this.tipo);
		produtoCesta.setLaboratorio(this.laboratorio);
		produtoCesta.setCodigo(this.codigo);
		produtoCesta.setIdProdutoOriginal(this.id);
		produtoCesta.setQuantidade(1);
		produtoCesta.setPrecoVenda(this.precoVenda);
		produtoCesta.setNcm(this.ncm);
		return produtoCesta;
	}

	public ProdutoAtendimento copyToProdutoAtendimento() {
		ProdutoAtendimento produto = new ProdutoAtendimento();
		produto.setNome(this.nome);
		produto.setIdProdutoOriginal(this.id);
		produto.setQuantidade(1);
		produto.setPreco(this.precoVenda);
		return produto;
	}

}
