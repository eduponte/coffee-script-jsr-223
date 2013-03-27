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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoffeeScriptEngineTest
{
    private ScriptEngine se;

    @Before
    public void setup() {
        ScriptEngineManager sem = new ScriptEngineManager();
        se = sem.getEngineByName("CoffeeScript");
        assertNotNull("created engine", se);
    }

    @Test
    public void testEval() throws Exception {
        Object o = se.eval("1+1");
        assertEquals("trivial eval", 2.0, o);
    }

    public static final String FUNS = "subr = (x,y) -> x+y\n" +
                                      "@fun = (x,y) -> subr(x,y)\n";

    @Test
    public void testInvoke() throws Exception {
        ((Compilable)se).compile(FUNS).eval();
        Object o = ((Invocable)se).invokeFunction("fun", 1, 2);
        assertEquals("trivial function", 3.0, o);
    }

}
