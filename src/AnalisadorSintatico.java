import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class AnalisadorSintatico {

    private static Map<String, Map<String, String>> tabela = new HashMap<>();

    // Pilha para parsing preditivo
    private Stack<String> pilha = new Stack<>();

    // Inicializacao da tabela de reconhecimento sintatico
    private static void inicializarTabela() {

        tabela.put("S", Map.of(
                "def", "S -> MAIN $",
                "id", "S -> MAIN $",
                "{", "S -> MAIN $",
                "int", "S -> MAIN $",
                "if", "S -> MAIN $",
                ";", "S -> MAIN $",
                "print", "S -> MAIN $",
                "return", "S -> MAIN $",
                "$", "S -> MAIN $"

        ));

        tabela.put("MAIN", Map.of(
                "def", "MAIN -> FLIST",
                "id", "MAIN -> STMTLIST",
                "{", "MAIN -> STMTLIST",
                "int", "MAIN -> STMTLIST",
                "if", "MAIN -> STMTLIST",
                ";", "MAIN -> STMTLIST",
                "print", "MAIN -> STMTLIST",
                "return", "MAIN -> STMTLIST",
                "$", "MAIN -> ε"

        ));

        tabela.put("FLIST", Map.of(
                "def", "FLIST' -> FDEF FLIST'"
        ));

        tabela.put("FLIST'", Map.of(
                "def", "FLIST' -> FDEF FLIST'",
                "$", "MAIN -> ε"
        ));

        tabela.put("FDEF", Map.of(
                "def", "FDEF -> def id ( PARLIST ) { STMTLIST }"
        ));

        tabela.put("PARLIST", Map.of(
                ")", "PARLIST -> ε",
                "int", "PARLIST -> int id PARLIST'"
        ));

        tabela.put("PARLIST'", Map.of(
                ")", "PARLIST' -> ε",
                ",", "PARLIST' -> , int id PARLIST'"

        ));

        tabela.put("VARLIST", Map.of(
                "id", "VARLIST -> id VARLIST'"
        ));

        tabela.put("VARLIST'", Map.of(
                ",", "VARLIST' -> , id VARLIST'",
                ";", "VARLIST' -> ε"
        ));

        tabela.put("STMT", Map.of(
                "id", "STMT -> M_STMT",
                "{", "STMT -> M_STMT",
                "int", "STMT -> M_STMT",
                "if", "STMT -> M_STMT",
                ";", "STMT -> M_STMT",
                "print", "STMT -> M_STMT",
                "return", "STMT -> M_STMT"


        ));

        tabela.put("M_STMT", Map.of(
                "id", "M_STMT -> BASIC_STMT",
                "{", "M_STMT -> BASIC_STMT",
                "int", "M_STMT -> BASIC_STMT",
                "if", "M_STMT -> if ( EXPR ) M_STMT else M_STMT",
                ";", "M_STMT -> BASIC_STMT",
                "print", "M_STMT -> BASIC_STMT",
                "return", "M_STMT -> BASIC_STMT"

        ));

        tabela.put("BASIC_STMT", Map.of(
                "id", "BASIC_STMT -> id := EXPR ;",
                "{", "BASIC_STMT -> { STMTLIST }",
                "int", "BASIC_STMT -> int VARLIST ;",
                ";", "BASIC_STMT -> ;",
                "print", "BASIC_STMT -> print EXPR ;",
                "return", "BASIC_STMT -> return RETURNST' ;"
        ));

        tabela.put("RETURNST'", Map.of(
                "id", "RETURNST' -> EXPR",
                "(", "RETURNST' -> EXPR",
                ";", "RETURNST' -> ε",
                "num", "RETURNST' -> EXPR"
        ));

        tabela.put("STMTLIST", Map.of(
                "id", "STMTLIST -> STMT STMTLIST'",
                "{", "STMTLIST -> STMT STMTLIST'",
                "int", "STMTLIST -> STMT STMTLIST'",
                "if", "STMTLIST -> STMT STMTLIST'",
                ";", "STMTLIST -> STMT STMTLIST'",
                "print", "STMTLIST -> STMT STMTLIST'",
                "return", "STMTLIST -> STMT STMTLIST'"
        ));

        tabela.put("STMTLIST'", Map.of(
                "$", "STMTLIST' -> ε",
                "id", "STMTLIST' -> STMT STMTLIST'",
                "{", "STMTLIST' -> STMT STMTLIST'",
                "}", "STMTLIST' -> ε",
                "int", "STMTLIST' -> STMT STMTLIST'",
                "if", "STMTLIST' -> STMT STMTLIST'",
                ";", "STMTLIST' -> STMT STMTLIST'",
                "print", "STMTLIST' -> STMT STMTLIST'",
                "return", "STMTLIST' -> STMT STMTLIST'"
        ));

        tabela.put("EXPR", Map.of(
                "id", "EXPR -> NUMEXPR EXPR'",
                "(", "EXPR -> NUMEXPR EXPR'",
                "num", "EXPR -> NUMEXPR EXPR'"
        ));

        tabela.put("EXPR'", Map.of(
                ")", "EXPR' -> ε",
                ",", "EXPR' -> ε",
                ";", "EXPR' -> ε",
                "<", "EXPR' -> RELOP NUMEXPR",
                "<=", "EXPR' -> RELOP NUMEXPR",
                ">", "EXPR' -> RELOP NUMEXPR",
                ">=", "EXPR' -> RELOP NUMEXPR",
                "==", "EXPR' -> RELOP NUMEXPR",
                "<>", "EXPR' -> RELOP NUMEXPR"

        ));

        tabela.put("RELOP", Map.of(
                "<", "RELOP -> <",
                "<=", "RELOP -> <=",
                ">", "RELOP -> >",
                ">=", "RELOP -> >=",
                "==", "RELOP -> ==",
                "<>", "RELOP -> <>"
        ));

        tabela.put("NUMEXPR", Map.of(
                "id", "NUMEXPR -> TERM NUMEXPR'",
                "(", "NUMEXPR -> TERM NUMEXPR'",
                "num", "NUMEXPR -> TERM NUMEXPR'"
        ));

        tabela.put("NUMEXPR'", Map.ofEntries(
                Map.entry(")", "NUMEXPR' -> ε"),
                Map.entry(",", "NUMEXPR' -> ε"),
                Map.entry(";", "NUMEXPR' -> ε"),
                Map.entry("<", "NUMEXPR' -> ε"),
                Map.entry("<=", "NUMEXPR' -> ε"),
                Map.entry(">", "NUMEXPR' -> ε"),
                Map.entry(">=", "NUMEXPR' -> ε"),
                Map.entry("==", "NUMEXPR' -> ε"),
                Map.entry("<>", "NUMEXPR' -> ε"),
                Map.entry("+", "NUMEXPR' -> + TERM NUMEXPR'"),
                Map.entry("-", "NUMEXPR' -> - TERM NUMEXPR'")
        ));

        tabela.put("TERM", Map.of(
                "id", "TERM -> FACTOR TERM'",
                "(", "TERM -> FACTOR TERM'",
                "num", "TERM -> FACTOR TERM'"
        ));

        tabela.put("TERM'", Map.ofEntries(
                Map.entry(")", "TERM' -> ε"),
                Map.entry(",", "TERM' -> ε"),
                Map.entry(";", "TERM' -> ε"),
                Map.entry("<", "TERM' -> ε"),
                Map.entry("<=", "TERM' -> ε"),
                Map.entry(">", "TERM' -> ε"),
                Map.entry(">=", "TERM' -> ε"),
                Map.entry("==", "TERM' -> ε"),
                Map.entry("<>", "TERM' -> ε"),
                Map.entry("+", "TERM' -> ε"),
                Map.entry("-", "TERM' -> ε"),
                Map.entry("*", "TERM' -> * FACTOR TERM'"),
                Map.entry("/", "TERM' -> / FACTOR TERM'")
        ));


        tabela.put("FACTOR", Map.of(
                "id", "FACTOR -> id FACTOR'",
                "(", "FACTOR -> ( NUMEXPR )",
                "num", "FACTOR -> num"
        ));

        tabela.put("FACTOR'", Map.ofEntries(
                Map.entry("(", "FACTOR' -> ( ARGLIST )"),
                Map.entry(")", "FACTOR' -> ε"),
                Map.entry(",", "FACTOR' -> ε"),
                Map.entry(";", "FACTOR' -> ε"),
                Map.entry("<", "FACTOR' -> ε"),
                Map.entry("<=", "FACTOR' -> ε"),
                Map.entry(">", "FACTOR' -> ε"),
                Map.entry(">=", "FACTOR' -> ε"),
                Map.entry("==", "FACTOR' -> ε"),
                Map.entry("<>", "FACTOR' -> ε"),
                Map.entry("+", "FACTOR' -> ε"),
                Map.entry("-", "FACTOR' -> ε"),
                Map.entry("*", "FACTOR' -> ε"),
                Map.entry("/", "FACTOR' -> ε")
        ));

        tabela.put("ARGLIST", Map.of(
                "id", "ARGLIST -> EXPR ARGLIST'",
                "(", "ARGLIST -> EXPR ARGLIST'",
                ")", "ARGLIST -> ε",
                "num", "ARGLIST -> EXPR ARGLIST'"
        ));

        tabela.put("ARGLIST'", Map.of(
                ")", "ARGLIST' -> ε",
                ",", "ARGLIST' -> , EXPR ARGLIST'",
                "num", "ARGLIST' -> , EXPR ARGLIST'"
        ));
    }

    public AnalisadorSintatico() {
        inicializarTabela();
    }

    public void analisar(List<Token> tokens) {

        tokens.add(new Token("$", "$"));

        pilha.push("$");

        pilha.push("MAIN");

        int indice = 0;

        while (!pilha.isEmpty()) {

            String topo = pilha.peek();

            Token tokenAtual = tokens.get(indice);

            System.out.println("Pilha: " + pilha);
            System.out.println("Token atual: " + tokenAtual.getToken() + " Lexema atual : " + tokenAtual.getLexema());
            System.out.println(" ");

            // Marca tokens desconhecidos
            if (tokenAtual.getToken().equals("DESCONHECIDO")) {
                throw new RuntimeException("Erro léxico: token desconhecido encontrado -> " + tokenAtual.getLexema());
            }

            // Se for igual entao match, entao pop e avanca para o proximo token e proximo peek da pilha
            if (topo.equals(tokenAtual.getToken())) {
                System.out.println("Match: " + topo);
                pilha.pop();
                indice++;

            } else if (tabela.containsKey(topo)) {

                Map<String, String> producoes = tabela.get(topo);
                if (producoes.containsKey(tokenAtual.getToken())) {

                    pilha.pop();
                    String producao = producoes.get(tokenAtual.getToken());
                    System.out.println("Produção utilizada: " + topo + " -> " + producao);

                    // Separa cada producao e adiciona em ordem inversa na pilha
                    // E uma pilha, logo quem tiver que sair primeiro deve entrar por ultimo
                    // Necessario assim pois se token a e pilha [$, A, B] -> erro de parsing, B !-> a
                    String[] elementos = producao.split(" -> ")[1].trim().split(" ");
                    for (int i = elementos.length - 1; i >= 0; i--) {
                        if (!elementos[i].equals("ε")) {
                            pilha.push(elementos[i]);
                        }
                    }
                } else {

                    throw new RuntimeException("Erro sintático: não-terminal '" + topo + "' não possui entrada para o token '" + tokenAtual.getLexema() + "' (token: " + tokenAtual.getToken() + ").");
                }
            } else {

                throw new RuntimeException("Erro sintático: token inesperado '" + tokenAtual.getLexema() + "' (token: " + tokenAtual.getToken() + ") no topo '" + topo + "'.");
            }
        }

        if (indice < tokens.size() - 1) {
            throw new RuntimeException("Erro sintático: entrada não completamente consumida. Tokens restantes: " + tokens.subList(indice, tokens.size()));
        }
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java AnalisadorSintatico <caminhoArquivo>");
            return;
        }

        String caminhoArquivo = args[0];
        String arquivoSaida = "tokens.txt"; // Arquivo txt gerado para observacao dos tokens do lexer

        try {
            List<Token> tokens = AnalisadorLexico.getAsTokens(caminhoArquivo);

            TokenUtils.salvarTokensEmArquivo(tokens, arquivoSaida);

            System.out.println("Tokens gerados pelo analisador léxico:");
            for (Token token : tokens) {
                System.out.println("Token: " + token.getToken() + " Lexema: " + token.getLexema());
            }

            AnalisadorSintatico analisador = new AnalisadorSintatico();
            analisador.analisar(tokens);

            System.out.println("Análise sintática concluída com sucesso!");
        } catch (RuntimeException e) {
            System.err.println("Erro durante a análise: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }
}