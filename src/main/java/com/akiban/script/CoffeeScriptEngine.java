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
import java.io.Reader;

public class CoffeeScriptEngine implements ScriptEngine, Compilable, Invocable
{
    private final CoffeeScriptEngineFactory factory;
    private final CoffeeScriptCompiler compiler;
    private final ScriptEngine javaScript;

    public CoffeeScriptEngine(CoffeeScriptEngineFactory factory, CoffeeScriptCompiler compiler, ScriptEngine javaScript) {
        this.factory = factory;
        this.compiler = compiler;
        this.javaScript = javaScript;
    }

    /* ScriptEngine */

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return javaScript.eval(compiler.compile(script), context);
    }

    @Override
    public Object eval(Reader reader , ScriptContext context) throws ScriptException {
        return javaScript.eval(compiler.compile(reader), context);
    }

    @Override
    public Object eval(String script) throws ScriptException {
        return javaScript.eval(compiler.compile(script));
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        return javaScript.eval(compiler.compile(reader));
    }

    @Override
    public Object eval(String script, Bindings n) throws ScriptException {
        return javaScript.eval(compiler.compile(script), n);
    }

    @Override
    public Object eval(Reader reader, Bindings n) throws ScriptException {
        return javaScript.eval(compiler.compile(reader), n);
    }

    @Override
    public void put(String key, Object value) {
        javaScript.put(key, value);
    }

    @Override
    public Object get(String key) {
        return javaScript.get(key);
    }

    @Override
    public Bindings getBindings(int scope) {
        return javaScript.getBindings(scope);
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        javaScript.setBindings(bindings, scope);
    }

    @Override
    public Bindings createBindings() {
        return javaScript.createBindings();
    }

    @Override
    public ScriptContext getContext() {
        return javaScript.getContext();
    }

    @Override
    public void setContext(ScriptContext context) {
        javaScript.setContext(context);
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return factory;
    }

    /* Compilable */

    @Override
    public CompiledScript compile(String script) throws ScriptException {
        return ((Compilable)javaScript).compile(compiler.compile(script));
    }

    @Override
    public CompiledScript compile(Reader script) throws ScriptException {
        return ((Compilable)javaScript).compile(compiler.compile(script));
    }

    /* Invocable */

    @Override
    public Object invokeMethod(Object thiz, String name, Object... args)
            throws ScriptException, NoSuchMethodException {
        return ((Invocable)javaScript).invokeMethod(thiz, name, args);
    }
    
    @Override
    public Object invokeFunction(String name, Object... args)
            throws ScriptException, NoSuchMethodException {
        return ((Invocable)javaScript).invokeFunction(name, args);
    }

    @Override
    public <T> T getInterface(Class<T> clazz) {
        return ((Invocable)javaScript).getInterface(clazz);
    }

    @Override
    public <T> T getInterface(Object thiz, Class<T> clazz) {
        return ((Invocable)javaScript).getInterface(thiz, clazz);
    }

}
