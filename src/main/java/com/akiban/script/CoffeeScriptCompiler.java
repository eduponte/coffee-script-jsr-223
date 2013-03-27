
package com.akiban.script;

import javax.script.*;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

public class CoffeeScriptCompiler {
    private final Invocable invocable;
    private final Object coffeeScript;

    public CoffeeScriptCompiler(ScriptEngine javaScript) {
        Reader reader;
        try {
            reader = new InputStreamReader(CoffeeScriptCompiler.class.getResourceAsStream("coffee-script.js"), "UTF-8");
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            CompiledScript compiled = ((Compilable)javaScript).compile(reader);
            compiled.eval();
        }
        catch (ScriptException ex) {
            throw new RuntimeException(ex);
        }
        invocable = (Invocable)javaScript;
        coffeeScript = javaScript.get("CoffeeScript");
    }

    public synchronized String compile(String script) throws ScriptException {
        try {
            return (String)invocable.invokeMethod(coffeeScript, "compile", script);
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
