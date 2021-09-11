import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;

import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.sax.*;

public class App {
    public static void main(String[] args) throws Exception {
        String texto = "";

        try {
            // Escanear Arquivo XML
            File arquivo = new File("arquivoXML.xml");
            Scanner scanner = new Scanner(arquivo);

            // Validação linha a linha
            while (scanner.hasNextLine()) {
                texto = texto + coversorXMLJSON(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erro na leitura do arquivo!");
            e.printStackTrace();
        }

        try {
            // Criar Arquivo JSON
            File file = new File("arquivoJson.json");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("arquivoJson.json");
            fileWriter.write(texto);
            fileWriter.close();

        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo!");
            e.printStackTrace();
        }
        
    }

    public static String coversorXMLJSON(String entrada) {
        String texto = "";

        if (entrada.matches("^<[A-Z]+>$")) {
            texto = entrada.replace("<", "{\"");

            texto = entrada.contains("CATALOG") ? texto.replace(">", "\":[\n") : texto.replace(">", "\":{\n");
        }

        if (entrada.matches("<[A-Z]+>.*<\\/[A-Z]+>")) {
            texto = entrada.replaceFirst("<", "\"").replaceFirst(">", "\":\"").replaceFirst("<\\/[A-Z]+>", "\",\n");
        }

        if (entrada.matches("<\\/[A-Z]+>")) {
            texto = entrada.contains("/CATALOG") ? entrada.replaceFirst("<\\/[A-Z]+>", "]\n}")
                    : entrada.replaceFirst("<\\/[A-Z]+>", "},\n},\n");
        }
        return texto;
    }

}