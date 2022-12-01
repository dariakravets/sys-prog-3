import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String input = reader.readLine();
            Lexer analyzer = new Lexer(input);
            analyzer.findAllLexemes();
            System.out.println("All lexemes found: " + analyzer.allWords);
            analyzer.printType();
        }
    }
}
