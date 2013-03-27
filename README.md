CoffeeScript for JSR 223
=====================

Wrap CoffeeScript compiler around existing JavaScript engine for use with <tt>javax.script</tt>. JavaScript (Rhino in JDK 6-7) runs the compiled scripts and the compiler itself (in different engine instances).