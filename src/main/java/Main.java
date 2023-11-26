public class Main {
    public static void main(String[] args){
        Tokenizer tokenizer = new Tokenizer();

        tokenizer.tokenize("int x = 100;");

        tokenizer.tokens.forEach(token -> System.out.println(token.token() + " === " +  token.type()));
    }
}
