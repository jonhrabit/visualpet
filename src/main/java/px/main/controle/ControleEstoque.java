package px.main.controle;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import px.main.seguranca.servicos.UsuarioLogado;
import px.main.veterinaria.modelos.Estoque;
import px.main.veterinaria.modelos.Produto;
import px.main.veterinaria.servicos.EstoqueService;
import px.main.veterinaria.servicos.ProdutoService;

@RestController
@RequestMapping("/estoque")
public class ControleEstoque {
	@Autowired
	EstoqueService estoqueService;
	@Autowired
	ProdutoService produtoService;

	@PutMapping
	public @ResponseBody String put(Estoque newEstoque) {
		Optional<Estoque> optional = estoqueService.get(newEstoque.getId());
		if (optional.isEmpty())
			return "Estoque não localizado.";
		Estoque estoque = optional.get();
		newEstoque.setUsuario(UsuarioLogado.get());
		newEstoque.setRegistro(new Date());
		estoque.put(newEstoque);
		estoqueService.salvar(estoque);
		return "Operações realizadas com exito.";
	}

	@PostMapping
	public @ResponseBody String post(Estoque newEstoque) {
		newEstoque.setUsuario(UsuarioLogado.get());
		newEstoque.setRegistro(new Date());
		estoqueService.salvar(newEstoque);
		return "Operações realizadas com exito.";
	}

	@DeleteMapping("/{id}")
	public @ResponseBody String delete(@PathVariable Integer id) {
		estoqueService.deletar(id);
		return "Estoque escluído.";
	}

	@GetMapping("/{id}")
	public @ResponseBody Estoque get(@PathVariable Integer id) {
		Optional<Estoque> estoque = estoqueService.get(id);
		if (estoque.isEmpty())
			return null;
		return estoque.get();
	}

	@GetMapping("/all")
	public @ResponseBody List<Estoque> all() {
		return estoqueService.all();
	}

	@PostMapping("/import")
	public @ResponseBody String doUpload(@RequestParam("file") MultipartFile multipartFile)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(multipartFile.getInputStream());
		String relatorio = "";
		System.out.println("------");
		NodeList lista = doc.getElementsByTagName("prod");
		for (int i = 0; i < lista.getLength(); i++) {
			System.out.println("Código: " + lista.item(i).getChildNodes().item(0).getTextContent());

			Produto prod = produtoService.get(lista.item(i).getChildNodes().item(0).getTextContent());
			if (prod.getId() != null) {
				prod.setNome(lista.item(i).getChildNodes().item(2).getTextContent()); //
				prod.setLaboratorio(doc.getElementsByTagName("emit").item(0).getChildNodes().item(2).getTextContent());
				produtoService.salvar(prod);

				estoqueService.salvar(new Estoque(0,
						prod.getFracao() * Integer.parseInt(lista.item(i).getChildNodes().item(6).getTextContent()),
						new BigDecimal(lista.item(i).getChildNodes().item(7).getTextContent()), "Lote",
						UsuarioLogado.get(), null, new Date(), prod));

				relatorio = relatorio + "<div>" + i + "<div/>";
				relatorio = relatorio + "<div>Nome: " + lista.item(i).getChildNodes().item(2).getTextContent()
						+ "</div>";
				relatorio = relatorio + "<div>Quantidade: " + lista.item(i).getChildNodes().item(6).getTextContent()
						+ "</div>";
				relatorio = relatorio + "<div>NCM: " + lista.item(i).getChildNodes().item(3).getTextContent()
						+ "</div>";
				relatorio = relatorio + "<div>Preço: " + lista.item(i).getChildNodes().item(7).getTextContent()
						+ "</div>";
				relatorio = relatorio + "<br/>";
			} else {
				relatorio = relatorio + "<div>NÃO LOCALIZADO - " + i + "<div/>";
				relatorio = relatorio + "<div>Nome: " + lista.item(i).getChildNodes().item(2).getTextContent()
						+ "</div>";
				relatorio = relatorio + "<div>Quantidade: " + lista.item(i).getChildNodes().item(6).getTextContent()
						+ "</div>";
				relatorio = relatorio + "<div>NCM: " + lista.item(i).getChildNodes().item(3).getTextContent()
						+ "</div>";
				relatorio = relatorio + "<div>Preço: " + lista.item(i).getChildNodes().item(7).getTextContent()
						+ "</div>";
				relatorio = relatorio + "<br/>";
			}

		}
		System.out.println(relatorio);
		return relatorio;
	}

}
