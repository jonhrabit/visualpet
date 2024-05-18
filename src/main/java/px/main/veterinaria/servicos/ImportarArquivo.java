package px.main.veterinaria.servicos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import px.main.veterinaria.modelos.Extrato;

public class ImportarArquivo {

	public static List<Extrato> importarExtratos(Reader file) {
		List<Extrato> lista = new ArrayList<Extrato>();
		BufferedReader csvReader = new BufferedReader(file);
		String row = "";
		try {
			csvReader.readLine();
			while ((row = csvReader.readLine()) != null) {
				lista.add(new Extrato(row, ";"));
			}
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lista;
	}

}
