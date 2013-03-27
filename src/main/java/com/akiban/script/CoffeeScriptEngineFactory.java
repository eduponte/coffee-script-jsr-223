
package com.akiban.script;

import javax.script.*;
import java.util.Arrays;
import java.util.List;

public class CoffeeScriptEngineFactory implements ScriptEngineFactory
{
    public static final String COFFEESCRIPT_VERSION = "1.6.2";
    public static final List<String> EXTENSIONS = Arrays.asList("coffee");
    public static final List<String> MIME_TYPES = Arrays.asList("text/coffeescript");
    public static final List<String> NAMES = Arrays.asList("CoffeeScript", "coffeescript");

    private static final String JAVASCRIPT_FACTORY = "com.sun.script.javascript.RhinoScriptEngineFactory";
    private final ScriptEngineFactory javaScript;
    private CoffeeScriptCompiler compiler = null;

    public CoffeeScriptEngineFactory() {
        try {
            // Don't have ScriptEngineManager for getEngineByName("JavaScript");
            javaScript = (ScriptEngineFactory)Class.forName(JAVASCRIPT_FACTORY).newInstance();
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public String getEngineName() {
        return "CoffeeScript around " + javaScript.getEngineName();
    }

    @Override
    public String getEngineVersion() {
        return "1.0";
    }

    @Override
    public List<String> getExtensions() {
        return EXTENSIONS;
    }

    @Override
    public List<String> getMimeTypes() {
        return MIME_TYPES;
    }

    @Override
    public List<String> getNames() {
        return NAMES;
    }

    @Override
    public String getLanguageName() {
        return "CoffeeScript";
    }

    @Override
    public String getLanguageVersion() {
        return COFFEESCRIPT_VERSION;
    }

    @Override
    public Object getParameter(String key) {
        if (ScriptEngine.ENGINE.equals(key))
            return getEngineName();
        else if (ScriptEngine.ENGINE_VERSION.equals(key))
            return getEngineVersion();
        else if (ScriptEngine.NAME.equals(key))
            return getNames().get(0);
        else if (ScriptEngine.LANGUAGE.equals(key))
            return getLanguageName();
        else if (ScriptEngine.LANGUAGE_VERSION.equals(key))
            return getLanguageVersion();
        else
            return javaScript.getParameter(key);
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        StringBuilder str = new StringBuilder(obj);
        str.append(".").append(m).append("(");
        for (int i = 0; i < args.length; i++) {
            if (i > 0)
                str.append(", ");
            str.append(args[i]);
        }
        str.append(")");
        return str.toString();
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return "print " + toDisplay;
    }

    @Override
    public String getProgram(String... statements) {
        StringBuilder str = new StringBuilder();
        for (String stmt : statements) {
            str.append(stmt).append("\n");
        }
        return str.toString();
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new CoffeeScriptEngine(this, getCompiler(), javaScript.getScriptEngine());
    }

    public synchronized CoffeeScriptCompiler getCompiler() {
        if (compiler == null)
            compiler = new CoffeeScriptCompiler(javaScript.getScriptEngine());
        return compiler;
    }

}
