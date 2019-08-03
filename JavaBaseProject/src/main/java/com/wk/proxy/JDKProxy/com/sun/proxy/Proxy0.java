package com.wk.proxy.JDKProxy.com.sun.proxy;

import com.wk.proxy.API.MathCalc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

public class Proxy0 extends Proxy implements MathCalc{

    private static Method m1;
    private static Method m5;
    private static Method m4;
    private static Method m2;
    private static Method m6;
    private static Method m3;
    private static Method m0;

    public Proxy0(InvocationHandler var1)  {
        super(var1);
    }

    public final boolean equals(Object var1)  {
        try {
            return (Boolean) super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final int sub(Integer var1, Integer var2)  {
        try {
            return (Integer) super.h.invoke(this, m5, new Object[]{var1, var2});
        } catch (RuntimeException | Error var4) {
            throw var4;
        } catch (Throwable var5) {
            throw new UndeclaredThrowableException(var5);
        }
    }

    public final int div(Integer var1, Integer var2)  {
        try {
            return (Integer) super.h.invoke(this, m4, new Object[]{var1, var2});
        } catch (RuntimeException | Error var4) {
            throw var4;
        } catch (Throwable var5) {
            throw new UndeclaredThrowableException(var5);
        }
    }

    public final String toString()  {
        try {
            return (String) super.h.invoke(this, m2, (Object[]) null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final int mut(Integer var1, Integer var2)  {
        try {
            return (Integer) super.h.invoke(this, m6, new Object[]{var1, var2});
        } catch (RuntimeException | Error var4) {
            throw var4;
        } catch (Throwable var5) {
            throw new UndeclaredThrowableException(var5);
        }
    }

    public final int add(Integer var1, Integer var2)  {
        try {
            return (Integer) super.h.invoke(this, m3, new Object[]{var1, var2});
        } catch (RuntimeException | Error var4) {
            throw var4;
        } catch (Throwable var5) {
            throw new UndeclaredThrowableException(var5);
        }
    }

    public final int hashCode()  {
        try {
            return (Integer) super.h.invoke(this, m0, (Object[]) null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m5 = Class.forName("com.wk.proxy.API.MathCalc").getMethod("sub", Class.forName("java.lang.Integer"), Class.forName("java.lang.Integer"));
            m4 = Class.forName("com.wk.proxy.API.MathCalc").getMethod("div", Class.forName("java.lang.Integer"), Class.forName("java.lang.Integer"));
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m6 = Class.forName("com.wk.proxy.API.MathCalc").getMethod("mut", Class.forName("java.lang.Integer"), Class.forName("java.lang.Integer"));
            m3 = Class.forName("com.wk.proxy.API.MathCalc").getMethod("add", Class.forName("java.lang.Integer"), Class.forName("java.lang.Integer"));
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
