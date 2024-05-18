package px.main.nfe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fincatto.documentofiscal.nfe400.classes.lote.consulta.NFLoteConsultaRetorno;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteEnvio;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteEnvioRetorno;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteEnvioRetornoDados;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada;
import com.fincatto.documentofiscal.utils.DFPersister;

@Service
public class NfeFilesService {

	@Value("${nfe.path}")
	private String pathNfe;

	final private String fileConfig = "/.cfg";

	public String getFileCfg() {
		return fileConfig;
	}

	public String getPathNfe() {
		return pathNfe;
	}

	public void gravarRetorno(NFLoteEnvioRetornoDados retorno) throws IOException {
		FileWriter fw = new FileWriter(
				new File(pathNfe + "/lotes/processamento/" + retorno.getLoteAssinado().getIdLote() + "-env-lot.xml"));
		String xmlGerado = retorno.getLoteAssinado().toString();
		fw.write(xmlGerado);
		fw.close();
		fw = new FileWriter(
				new File(pathNfe + "/lotes/processamento/" + retorno.getLoteAssinado().getIdLote() + "-rec.xml"));
		xmlGerado = retorno.getRetorno().toString();
		fw.write(xmlGerado);
		fw.close();
	}

	public NFLoteEnvioRetornoDados lerRetorno(String loteId) throws Exception {
		File myObj = new File(pathNfe + "/lotes/" + loteId + "-env-lot.xml");
		String loteAssinado = "";
		Scanner myReader = new Scanner(myObj);
		while (myReader.hasNextLine()) {
			loteAssinado = loteAssinado + myReader.nextLine();
		}
		myReader.close();
		myObj = new File(pathNfe + "/lotes/" + loteId + "-rec.xml");
		String retorno = "";
		myReader = new Scanner(myObj);
		while (myReader.hasNextLine()) {
			retorno = retorno + myReader.nextLine();
		}
		myReader.close();
		NFLoteEnvioRetornoDados dados = new NFLoteEnvioRetornoDados(
				new DFPersister().read(NFLoteEnvioRetorno.class, retorno),
				new DFPersister().read(NFLoteEnvio.class, loteAssinado));
		return dados;
	}

	public void deletarLote(String loteId) throws Exception {
		File env = new File(pathNfe + "/lotes/" + loteId + "-env-lot.xml");
		env.delete();
		File rec = new File(pathNfe + "/lotes/" + loteId + "-rec.xml");
		rec.delete();
	}

	public void gravarNotaProcessada(NFNotaProcessada nota) throws IOException {
		FileWriter fw = new FileWriter(
				new File(pathNfe + "/notas/" + nota.getNota().getInfo().getIdentificador() + ".xml"));
		String xmlGerado = nota.toString();
		fw.write(xmlGerado);
		fw.close();
	}

	public void gravarRespostaLote(NFLoteConsultaRetorno consultaRetorno) throws IOException {
		FileWriter fw = new FileWriter(
				new File(pathNfe + "/protocolos/" + consultaRetorno.getNumeroRecibo() + "-pro-rec.xml"));
		String xmlGerado = consultaRetorno.toString();
		fw.write(xmlGerado);
		fw.close();
	}

	public NFLoteConsultaRetorno lerRespostaLote(String recibo) throws Exception {
		File myObj = new File(pathNfe + "/protocolos/" + recibo + "-pro-rec.xml");
		String proc = "";
		Scanner myReader = new Scanner(myObj);
		while (myReader.hasNextLine()) {
			proc = proc + myReader.nextLine();
		}
		myReader.close();
		NFLoteConsultaRetorno dados = new DFPersister().read(NFLoteConsultaRetorno.class, proc);
		return dados;
	}

	public NFNotaProcessada lerNotaProcessada(String identificador) throws Exception {
		File myObj = new File(pathNfe + "/notas/" + identificador + ".xml");
		String data = "";
		Scanner myReader = new Scanner(myObj);
		while (myReader.hasNextLine()) {
			data = data + myReader.nextLine();
		}
		myReader.close();
		return new DFPersister().read(NFNotaProcessada.class, data);
	}

	public File lerNotaProcessadaXml(String identificador) throws Exception {
		return new File(pathNfe + "/notas/" + identificador + ".xml");
	}

	public String file(String identificador) throws Exception {
		return pathNfe + "/notas/" + identificador + ".xml";
	}

	public String[] processamentoLoteFiles() {
		File f = new File(pathNfe + "/lotes/");
		return f.list();
	}

	public String[] ConsultarNotas() {
		File f = new File(pathNfe + "/notas/");
		return f.list();
	}

	public String cfg(String parametro) throws FileNotFoundException {
		File myObj = new File(pathNfe + fileConfig);
		if (!myObj.exists()) {
			return "";
		}
		String data = "";
		Scanner myReader = new Scanner(myObj);
		while (myReader.hasNextLine()) {
			data = myReader.nextLine();
			if (data.split("=")[0].equals(parametro)) {
				myReader.close();
				return data.split("=")[1];
			}
		}
		myReader.close();
		return "";
	}

	public boolean cfgSalvar(String parametro, String valor) throws IOException {
		File myObj = new File(pathNfe + fileConfig);
		if (!myObj.exists()) {
			FileWriter file = new FileWriter(pathNfe + fileConfig);
			file.write(parametro + "=" + valor);
			file.close();

			System.out.println("File criado ->" + parametro + "=" + valor);
			return true;
		}
		String data = "", linha = "";
		Scanner myReader = new Scanner(myObj);
		boolean encontrado = false;
		while (myReader.hasNextLine()) {
			linha = myReader.nextLine();
			if (linha.split("=")[0].equals(parametro)) {
				linha = linha.split("=")[0] + "=" + valor + "\n";
				encontrado = true;
			}
			data = data + linha + "\n";
		}
		myReader.close();
		if (!encontrado) {
			data = data + parametro + "=" + valor;
		}
		FileWriter file = new FileWriter(pathNfe + fileConfig);
		file.write(data);
		file.close();
		System.out.println("Configuração alterada -> " + parametro + "=" + valor);
		return true;
	}
}
