import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum TokenType {
    // Keywords
    IF, ELSE, WHILE, FOR, FUNCTION, RETURN,

    // Data types
    INT, FLOAT, STRING, BOOLEAN,

    // Operators
    PLUS, MINUS, MULTIPLY, DIVIDE, MODULO,
    EQUAL, NOT_EQUAL, LESS_THAN, LESS_EQUAL, GREATER_THAN, GREATER_EQUAL,
    AND, OR, NOT,

    // Punctuation
    SEMICOLON, COMMA, COLON, DOT,

    // Parentheses and Brackets
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET,

    // Quotes
    DOUBLE_QUOTE, SINGLE_QUOTE,

    // Identifiers and literals
    IDENTIFIER, INTEGER_LITERAL, FLOAT_LITERAL, STRING_LITERAL, BOOLEAN_LITERAL,

    // Error?
    UNKNOWN
}

record Token(String token, TokenType type, int row, int col) {
};


public class Tokenizer {
    private static final Map<String, TokenType> tokenTypeMap = new HashMap<>();

    static {
        // Keywords
        tokenTypeMap.put("if", TokenType.IF);
        tokenTypeMap.put("else", TokenType.ELSE);
        tokenTypeMap.put("while", TokenType.WHILE);
        tokenTypeMap.put("for", TokenType.FOR);
        tokenTypeMap.put("function", TokenType.FUNCTION);
        tokenTypeMap.put("return", TokenType.RETURN);

        // Data types
        tokenTypeMap.put("int", TokenType.INT);
        tokenTypeMap.put("float", TokenType.FLOAT);
        tokenTypeMap.put("string", TokenType.STRING);
        tokenTypeMap.put("boolean", TokenType.BOOLEAN);

        // Operators
        tokenTypeMap.put("+", TokenType.PLUS);
        tokenTypeMap.put("-", TokenType.MINUS);
        tokenTypeMap.put("*", TokenType.MULTIPLY);
        tokenTypeMap.put("/", TokenType.DIVIDE);
        tokenTypeMap.put("%", TokenType.MODULO);
        tokenTypeMap.put("==", TokenType.EQUAL);
        tokenTypeMap.put("!=", TokenType.NOT_EQUAL);
        tokenTypeMap.put("<", TokenType.LESS_THAN);
        tokenTypeMap.put("<=", TokenType.LESS_EQUAL);
        tokenTypeMap.put(">", TokenType.GREATER_THAN);
        tokenTypeMap.put(">=", TokenType.GREATER_EQUAL);
        tokenTypeMap.put("&&", TokenType.AND);
        tokenTypeMap.put("||", TokenType.OR);
        tokenTypeMap.put("!", TokenType.NOT);

        // Punctuation
        tokenTypeMap.put(";", TokenType.SEMICOLON);
        tokenTypeMap.put(",", TokenType.COMMA);
        tokenTypeMap.put(":", TokenType.COLON);
        tokenTypeMap.put(".", TokenType.DOT);

        // Quotes
        tokenTypeMap.put("'", TokenType.SINGLE_QUOTE);
        tokenTypeMap.put("\"", TokenType.DOUBLE_QUOTE);

        // Parentheses and Brackets
        tokenTypeMap.put("(", TokenType.LEFT_PAREN);
        tokenTypeMap.put(")", TokenType.RIGHT_PAREN);
        tokenTypeMap.put("{", TokenType.LEFT_BRACE);
        tokenTypeMap.put("}", TokenType.RIGHT_BRACE);
        tokenTypeMap.put("[", TokenType.LEFT_BRACKET);
        tokenTypeMap.put("]", TokenType.RIGHT_BRACKET);

        // Assignment
        tokenTypeMap.put("=", TokenType.EQUAL);
    }

    public List<Token> tokens = new ArrayList<>();

    private void addToken(String token, int row, int col) {
        TokenType currentTokenType = TokenType.UNKNOWN;

        if (!token.isEmpty()) {
            if (!this.tokens.isEmpty()) {
                Token prevToken = this.tokens.get(this.tokens.size() - 1);
                if (prevToken.type() == TokenType.DOUBLE_QUOTE || prevToken.type() == TokenType.SINGLE_QUOTE) {
                    currentTokenType = TokenType.STRING_LITERAL;
                } else if (prevToken.type() == TokenType.STRING
                        || prevToken.type() == TokenType.INT
                        || prevToken.type() == TokenType.FLOAT
                        || prevToken.type() == TokenType.BOOLEAN) {
                    currentTokenType = TokenType.IDENTIFIER;
                }
            }
            if(Character.isDigit(token.toCharArray()[0])){
                currentTokenType = TokenType.INTEGER_LITERAL;
            }
            this.tokens.add(new Token(token, tokenTypeMap.getOrDefault(token, currentTokenType), row, col));
        }
    }

    public void tokenize(String input) {
        int row = 0;
        int col = 0;

        StringBuilder token = new StringBuilder();
        for (var ch : input.toCharArray()) {
            if (ch == '\n') {
                row += 1;
                col = 0;
            } else {
                col += 1;
            }
            if (Character.isWhitespace(ch)) {
                this.addToken(token.toString(), row, col);
                token.setLength(0);
            } else if (ch == '\'' || ch == '=' || ch == '"' || ch == ';') {
                if (!token.isEmpty()) {
                    this.addToken(token.toString(), row, col);
                    token.setLength(0);
                }
                this.addToken("" + ch, row, col);
            }
            if (!Character.isLetterOrDigit(ch)) {
                this.addToken(token.toString(), row, col);
                token.setLength(0);
            } else {
                token.append(ch);
            }
        }
        this.addToken(token.toString(), row, col);
        token.setLength(0);
    }
}
