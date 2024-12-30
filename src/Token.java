public class Token {
    public String token;
    public String lexema;

    public Token(String token, String lexema) {
        this.token = token;
        this.lexema = lexema;
    }

    public Token(String num, int i) {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    @Override
    public String toString() {
        return token;
    }
}