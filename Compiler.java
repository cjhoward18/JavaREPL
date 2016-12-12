
package cs345.repl;
/**
 * Created by Cole Howard on 2/3/16.
 */

import com.sun.source.util.JavacTask;

import javax.tools.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.*;

public class Compiler {

    final String tmpdir = System.getProperty("java.io.tmpdir");
    final String CLASSPATH = System.getenv("CLASSPATH");
    final String pathSep = File.pathSeparator;
    int classcount;
    String code;
    final URL it = new File(tmpdir).toURI().toURL();
    final URLClassLoader loader = new URLClassLoader(new URL[]{it});

    public Compiler() throws IOException {
        this.classcount = 0;
        this.code = "";
    }

    public void declarationBuilder() throws Exception {

        FileWriter tmp = new FileWriter(tmpdir + "Interp" + Integer.toString(this.classcount) + ".java");
        BufferedWriter w = new BufferedWriter(tmp);
        String dec_template;

        if (this.classcount == 0) {
            dec_template = "import java.io.*; import java.util.*; public class Interp" + Integer.toString(this.classcount) + " { public static zxcv  public static void exec() { } }";
        } else {
            dec_template = "import java.io.*; import java.util.*; public class Interp" + Integer.toString(this.classcount) + " extends Interp" + Integer.toString(this.classcount - 1) + " { public static zxcv  public static void exec() { } }";
        }

        dec_template = dec_template.replaceFirst("zxcv", this.code);
        w.write(dec_template);
        w.close();

        File f = new File(tmpdir, "Interp" + Integer.toString(this.classcount) + ".java");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(f));

        Iterable<String> compileOptions =
                Arrays.asList("-d", tmpdir, "-cp", tmpdir + pathSep + CLASSPATH);
        JavacTask task = (JavacTask)
                compiler.getTask(null, fileManager, diagnostics, compileOptions, null, compilationUnits);
        boolean parse = task.call();
        fileManager.close();

        if (diagnostics.getDiagnostics().size() == 0) { // if compiled
            String Class = "Interp" + Integer.toString((this.classcount));
            Class<?> c2 = loader.loadClass(Class);
            Method m2 = c2.getMethod("exec");
            m2.invoke(null, null);
            this.classcount++;
        } else {
            statmentBuilder();
        }
    }

    public void statmentBuilder() throws Exception {

        FileWriter tmp = new FileWriter(tmpdir + "Interp" + Integer.toString(this.classcount) + ".java");
        BufferedWriter w = new BufferedWriter(tmp);
        String state_template;

        if (this.classcount != 0) {
            state_template = "import java.io.*; import java.util.*; public class Interp" + Integer.toString(this.classcount) + " extends Interp" + Integer.toString(this.classcount - 1) + " {  public static void exec() {  zxcv }  }";
        } else {
            state_template = "import java.io.*;  import java.util.*;  public class Interp" + Integer.toString(this.classcount) + " {  public static void exec() { zxcv } }";
        }
        state_template = state_template.replaceFirst("zxcv", this.code);
        w.write(state_template);
        w.close();

        File f = new File(tmpdir, "Interp" + Integer.toString(this.classcount) + ".java");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(diagnostics, null, null);

        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(f));

        Iterable<String> compileOptions =
                Arrays.asList("-d", tmpdir, "-cp", tmpdir + pathSep + CLASSPATH);

        JavacTask task = (JavacTask)
                compiler.getTask(null, fileManager, diagnostics, compileOptions, null, compilationUnits);
        boolean t = task.call();

        fileManager.close();

        if (diagnostics.getDiagnostics().size() != 0) { // if compiled
            List<Diagnostic<?extends JavaFileObject>> error = diagnostics.getDiagnostics();
            for (Diagnostic d : error ) {
                int star = ((int)d.getLineNumber()) + 6 ;
                System.err.println( "line " + star + ": " + d.getMessage(new Locale("English")));

            }

        } else {
            String Class = "Interp" + Integer.toString(this.classcount);
            Class<?> c2 = loader.loadClass(Class);
            Method m2 = c2.getMethod("exec");
            m2.invoke(null, null);
            this.classcount++;
        }
    }

    public void setCode(String code) {
        this.code = code;
    }

}
