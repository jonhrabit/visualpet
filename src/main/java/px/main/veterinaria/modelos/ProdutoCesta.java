package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "produtoscesta")
public class ProdutoCesta {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@Basic(optional = false)
	@Column(nullable = false)
	private String nome;
	private String tipo, laboratorio, codigo, ncm;

	private Integer idProdutoOriginal;
	private Integer quantidade;
	private BigDecimal precoVenda, precoCompra;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "cesta_id")
	@JsonIgnore
	private Cesta cesta;

	public ProdutoCesta(Integer id, String nome, String tipo, String laboratorio, String codigo,
			Integer idProdutoOriginal, Integer quantidade, BigDecimal precoVenda, BigDecimal precoCompra) {
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.laboratorio = laboratorio;
		this.codigo = codigo;
		this.idProdutoOriginal = idProdutoOriginal;
		this.quantidade = quantidade;
		this.precoVenda = precoVenda;
		this.precoCompra = precoCompra;
	}

	public ProdutoCesta() {
		this.id = 0;
		this.tipo = "produto";
		this.quantidade = 1;
		this.precoVenda = new BigDecimal(0);
		this.precoCompra = new BigDecimal(0);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean compareId(Integer id) {
		if ((this.id.equals(id)) || (this.idProdutoOriginal.equals(id)))
			return true;
		return false;
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

	public boolean isProduto() {
		if (this.tipo.equals("produto"))
			return true;
		return false;
	}

	public boolean isServico() {
		if (this.tipo.equals("servico"))
			return true;
		return false;
	}

	public boolean isVacina() {
		if (this.tipo.equals("vacina"))
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

	public Integer getIdProdutoOriginal() {
		return idProdutoOriginal;
	}

	public void setIdProdutoOriginal(Integer idProdutoOriginal) {
		this.idProdutoOriginal = idProdutoOriginal;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public void QuantidadeUp(Integer limite) {
		this.quantidade++;
		this.quantidade = limite;
	}

	public void QuantidadeUp(Integer v, Integer limite) {
		this.quantidade += v;
		if (this.quantidade > limite)
			this.quantidade = limite;
	}

	public void QuantidadeDown(Integer v) {
		this.quantidade -= v;
		if (this.quantidade < 0)
			this.quantidade = 0;
	}

	public void QuantidadeDown() {
		this.quantidade--;
		if (this.quantidade < 0)
			this.quantidade = 0;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public BigDecimal getPrecoCompra() {
		return precoCompra;
	}

	public void setPrecoCompra(BigDecimal precoCompra) {
		this.precoCompra = precoCompra;
	}

	public Cesta getCesta() {
		return cesta;
	}

	public void setCesta(Cesta cesta) {
		this.cesta = cesta;
	}

	public String getNcm() {
		return ncm;
	}

	public void setNcm(String ncm) {
		this.ncm = ncm;
	}

	@Override
	public String toString() {
		return "Produto [nome=" + nome + ", laboratorio=" + laboratorio + ", quantidade=" + quantidade + "]";
	}

	public ProdutoCesta clone() {
		ProdutoCesta produtoCesta = new ProdutoCesta();
		produtoCesta.setNome(this.nome);
		produtoCesta.setTipo(this.tipo);
		produtoCesta.setLaboratorio(this.laboratorio);
		produtoCesta.setCodigo(this.codigo);
		produtoCesta.setIdProdutoOriginal(this.idProdutoOriginal);
		produtoCesta.setQuantidade(this.quantidade);
		produtoCesta.setPrecoVenda(this.precoVenda);
		produtoCesta.setPrecoCompra(this.precoCompra);
		return produtoCesta;
	}
}
