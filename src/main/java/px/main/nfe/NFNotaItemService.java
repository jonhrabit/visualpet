package px.main.nfe;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaPIS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.documentofiscal.nfe400.classes.NFOrigem;
import com.fincatto.documentofiscal.nfe400.classes.NFProdutoCompoeValorNota;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItem;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImposto;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINSOutrasOperacoes;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN102;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoPIS;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoPISOutrasOperacoes;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemProduto;

import px.main.veterinaria.modelos.Cesta;
import px.main.veterinaria.modelos.ProdutoCesta;

@Service
public class NFNotaItemService {

	public static List<NFNotaInfoItem> gerarItens(Cesta cesta) {
		List<NFNotaInfoItem> lista = new ArrayList<NFNotaInfoItem>();
		int i = 1;
		for (ProdutoCesta produto : cesta.getProdutos()) {
			lista.add(getItem(i, produto));
			i++;
		}
		return lista;
	}

	private static NFNotaInfoItem getItem(int n, ProdutoCesta cp) {
		NFNotaInfoItemProduto produto = new NFNotaInfoItemProduto();
		produto.setCfop("5933");
		produto.setCodigo(String.format("%08d", cp.getIdProdutoOriginal()));
		produto.setCodigoDeBarras("SEM GTIN");
		produto.setCodigoDeBarrasTributavel("SEM GTIN");
		produto.setCompoeValorNota(NFProdutoCompoeValorNota.SIM);
		produto.setDescricao(ASCII(cp.getNome()));
		produto.setQuantidadeComercial(new BigDecimal(cp.getQuantidade()));
		produto.setQuantidadeTributavel(new BigDecimal(cp.getQuantidade()));
		produto.setValorUnitario(cp.getPrecoVenda());
		produto.setValorTotalBruto(cp.getPrecoVenda().multiply(new BigDecimal(cp.getQuantidade())));
		produto.setNcm("00000000");
		produto.setUnidadeComercial("UN");
		produto.setUnidadeTributavel("UN");
		produto.setValorUnitarioTributavel(cp.getPrecoVenda());

		NFNotaInfoItem item = new NFNotaInfoItem();
		item.setProduto(produto);
		item.setNumeroItem(n);

		item.setImposto(getImpostos());

		return item;
	}

	public static String ASCII(String str) {
		String t = "", temp = "";
		t = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		if (t.split("-").length > 1) {
			for (String p1 : t.split("-")) {
				temp = temp + p1;
			}
			t = temp;
			temp="";
			
		}
		if (t.split("\\(").length > 1) {
			for (String p2 : t.split("\\(")) {
				temp = temp + p2;
			}
			t = temp;
			temp="";

		}
		if (t.split("\\)").length > 1) {
			for (String p3 : t.split("\\)")) {
				temp = temp + p3;
			}
			t = temp;
			temp="";

		}
		return t;
	}
	public static String ASCII_esp(String str) {
		String t = "", temp = "";
		t = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		if (t.split("-").length > 1) {
			for (String p1 : t.split("-")) {
				temp = temp + p1;
			}
			t = temp;
			temp="";
			
		}
		if (t.split("\\(").length > 1) {
			for (String p2 : t.split("\\(")) {
				temp = temp + p2;
			}
			t = temp;
			temp="";

		}
		if (t.split(" ").length > 1) {
			for (String p3 : t.split(" ")) {
				temp = temp + p3;
			}
			t = temp;
			temp="";

		}
		if (t.split("\\)").length > 1) {
			for (String p3 : t.split("\\)")) {
				temp = temp + p3;
			}
			t = temp;
			temp="";

		}
		return t;
	}

	private static NFNotaInfoItemImposto getImpostos() {
		final NFNotaInfoItemImposto imposto = new NFNotaInfoItemImposto();
		imposto.setCofins(getNFNotaInfoItemImpostoCOFINS());
		imposto.setPis(getNFNotaInfoItemImpostoPIS());
		imposto.setIcms(getNFNotaInfoItemImpostoICMS());
		// imposto.setIssqn(getNFNotaInfoItemImpostoISSQN());
		imposto.setValorTotalTributos(BigDecimal.ZERO);
		return imposto;
	}

	private static NFNotaInfoItemImpostoCOFINS getNFNotaInfoItemImpostoCOFINS() {
		NFNotaInfoItemImpostoCOFINS cofins = new NFNotaInfoItemImpostoCOFINS();
		NFNotaInfoItemImpostoCOFINSOutrasOperacoes operacoes = new NFNotaInfoItemImpostoCOFINSOutrasOperacoes();
		operacoes.setSituacaoTributaria(NFNotaInfoSituacaoTributariaCOFINS.OUTRAS_OPERACOES);
		operacoes.setValorCOFINS(BigDecimal.ZERO);
		operacoes.setPercentualCOFINS(BigDecimal.ZERO);
		operacoes.setValorBaseCalculo(BigDecimal.ZERO);
		cofins.setOutrasOperacoes(operacoes);
		return cofins;
	}

	private static NFNotaInfoItemImpostoPIS getNFNotaInfoItemImpostoPIS() {
		final NFNotaInfoItemImpostoPIS pis = new NFNotaInfoItemImpostoPIS();
		NFNotaInfoItemImpostoPISOutrasOperacoes operacoes = new NFNotaInfoItemImpostoPISOutrasOperacoes();
		operacoes.setSituacaoTributaria(NFNotaInfoSituacaoTributariaPIS.OUTRAS_OPERACOES);
		operacoes.setPercentualAliquota(BigDecimal.ZERO);
		operacoes.setValorBaseCalculo(BigDecimal.ZERO);
		operacoes.setValorTributo(BigDecimal.ZERO);
		pis.setOutrasOperacoes(operacoes);
		return pis;
	}

	private static NFNotaInfoItemImpostoICMS getNFNotaInfoItemImpostoICMS() {
		final NFNotaInfoItemImpostoICMS icms = new NFNotaInfoItemImpostoICMS();
		final NFNotaInfoItemImpostoICMSSN102 icms102 = new NFNotaInfoItemImpostoICMSSN102();
		icms102.setOrigem(NFOrigem.NACIONAL);
		icms102.setSituacaoOperacaoSN(NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_SEM_PERMISSAO_CREDITO);
		icms.setIcmssn102(icms102);
		return icms;
	}

/* 	private static NFNotaInfoItemImpostoISSQN getNFNotaInfoItemImpostoISSQN() {
		final NFNotaInfoItemImpostoISSQN impostoISSQN = new NFNotaInfoItemImpostoISSQN();
		impostoISSQN.setIndicadorExigibilidadeISS(NFNotaInfoItemIndicadorExigibilidadeISS.NAO_INCIDENCIA);
		
		  impostoISSQN.setIndicadorIncentivoFiscal(
		  NFNotaInfoItemIndicadorIncentivoFiscal.SIM);
		  impostoISSQN.setCodigoMunicipio(4314902);
		  impostoISSQN.setItemListaServicos("25.01"); impostoISSQN.setValor(new
		  BigDecimal("999999999999.99")); impostoISSQN.setValorAliquota(new
		  BigDecimal("99.99")); impostoISSQN.setValorBaseCalculo(new
		  BigDecimal("999999999999.99"));
		  impostoISSQN.setCodigoMunicipioIncidenciaImposto("3813816");
		  impostoISSQN.setCodigoPais("8486");
		  impostoISSQN.setCodigoServico("VfsQTgAm60yAqyOMUOIp");
		  impostoISSQN.setNumeroProcesso("Sw4CSjke5lhAzlBrzFgKuNjtrRSVfO");
		  impostoISSQN.setValorDeducao(new BigDecimal("99999999999.99"));
		  impostoISSQN.setValorDescontoCondicionado(new BigDecimal("99999999999.99"));
		  impostoISSQN.setValorDescontoIncondicionado(new
		  BigDecimal("99999999999.99")); impostoISSQN.setValorOutro(new
		  BigDecimal("99999999999.99")); impostoISSQN.setValorRetencaoISS(new
		  BigDecimal("99999999999.99"));
		
		return impostoISSQN;
	} */

}
