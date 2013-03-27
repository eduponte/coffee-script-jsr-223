/**
 * Copyright (c) 2013 Akiban Technologies, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
