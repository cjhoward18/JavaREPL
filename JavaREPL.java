package cs345.repl;
import java.io.*;

public class JavaREPL {

    public static void main(String[] args) throws Exception {
        exec(new InputStreamReader(System.in));
    }

    public static void exec(Reader r) throws Exception {
        BufferedReader stdin = new BufferedReader(r);
        NestedReader reader = new NestedReader(stdin);
        Compiler comp = new Compiler();

        while (true) {
            System.out.print("> ");
            String java = reader.getNestedString();
            try {
                stdin.ready();
            } catch (IOException e) {
                return;
            }
            if (java.equals("poii")) {
                stdin.close();
                break;
            }            comp.setCode(java);
            comp.declarationBuilder();
        }
    }
}
