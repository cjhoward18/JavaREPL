/**
 * Created by Cole Howard on 2/1/16.
 */

package cs345.repl;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;

public class NestedReader {

    StringBuilder buf;
    BufferedReader input;
    int c;

    public NestedReader(BufferedReader stdin) {

        this.input = stdin;
        this.buf = new StringBuilder();
    }

    public String getNestedString() throws IOException {

        Stack<Integer> symbols = new Stack<Integer>();
        c = input.read(); // initial char
        buf = new StringBuilder(); // resetting buffer

        while (c != 10 || !symbols.empty()) { // while the next char is not
            // newline
            c = consume();
	    if (c == -1) {
            return "poii";
	}

            if (c == 40 || c == 91 || c == 123) { // if opening symbol

                switch (c) { // pushing its closing counter-part
                    case 40:
                        symbols.push(41);
                        break;
                    case 91:
                        symbols.push(93);
                        break;
                    case 123:
                        symbols.push(125);
                        break;
                }
            }

            if (c == 41 || c == 93 || c == 125) { // if closing symbol
                int match;

                if (symbols.empty()) {
                    System.out.println("ERROR: unexpected symbol");
                    while (c != 10) {
                        c = input.read();
                    }
                    break;
                } else if ((match = symbols.pop()) != c) {
                    System.out.println("ERROR: incorrect symbol to match ");
                    while (c != 10) {
                        c = input.read();
                    }
                    break;
                }
            }
        }
        String output = buf.toString().trim();

        if (output.startsWith("print (")) {
            output = output.replaceFirst("print", "System.out.println");
        } else if (output.startsWith("print")) {
            output = output.replaceFirst("print", "System.out.println(");
            output = output.replaceFirst(";", ");");
        }

        return output;
    }

    public int consume() throws IOException {

        buf.append((char) c);

        if (c == 39) { // handles quotes
            c = input.read();
            while (c != 39) {
                buf.append((char) c);
                c = input.read();
            }
            buf.append((char) c);
        }
        if (c == 34) { // handles quotes
            c = input.read();
            while (c != 34) {
                buf.append((char) c);
                c = input.read();
            }
            buf.append((char) c);
        }
        if (c == 47) { // if one '/'
            c = input.read();
            if (c == 47) { // rest is a comment delete last '/' and ignore rest
                // of line
                buf.deleteCharAt(buf.length() - 1);
                while (c != 10) {
                    c = input.read();
                }
            }
        } else {
            c = input.read();
        }

        return c;
    }
}
