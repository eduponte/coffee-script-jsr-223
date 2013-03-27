
package com.akiban.script;

import javax.script.*;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

public class CoffeeScriptCompiler {
    private static final String COFFEE_SCRIPT = "coffee-script.js";
    private static final String PACKAGE = "CoffeeScript";
    private static final String COMPILE = "compile";
    private static final String OPTIONS = "{bare: true}"; // Always return value.

    private final Invocable invocable;
    private final Object coffeeScript, options;

    public CoffeeScriptCompiler(ScriptEngine javaScript) {
        Reader reader;
        try {
            reader = new InputStreamReader(CoffeeScriptCompiler.class.getResourceAsStream(COFFEE_SCRIPT), "UTF-8");
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            options = javaScript.eval(String.format("var o = %s; o", OPTIONS));
            CompiledScript compiled = ((Compilable)javaScript).compile(reader);
            compiled.eval();
        }
        catch (ScriptException ex) {
            throw new RuntimeException(ex);
        }
        invocable = (Invocable)javaScript;
        coffeeScript = javaScript.get(PACKAGE);
    }

    public synchronized String compile(String script) throws ScriptException {
        try {
            return (String)invocable.invokeMethod(coffeeScript, COMPILE, script, options);
        }
        catch (NoSuchMethodException ex) {
            throw new ScriptException(ex);
        }
    }

    public synchronized String compile(Reader script) throws ScriptException {
        StringBuilder contents = new StringBuilder();
        try {
            script.read(CharBuffer.wrap(contents));
        }
        catch (IOException ex) {
            throw new ScriptException(ex);
        }
        return compile(contents.toString());
    }
}
