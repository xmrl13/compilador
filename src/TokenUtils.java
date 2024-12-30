import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TokenUtils {

    public static void salvarTokensEmArquivo(List<Token> tokens, String caminhoArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (Token token : tokens) {
                writer.write(token.getToken() + " ");
            }
            System.out.println("Tokens salvos em: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os tokens no arquivo: " + e.getMessage());
        }
    }
}
