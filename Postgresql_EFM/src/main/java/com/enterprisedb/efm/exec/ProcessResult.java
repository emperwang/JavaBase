package com.enterprisedb.efm.exec;

public class ProcessResult {
    private volatile int exitValue;

    private volatile String errorOut;

    private volatile String stdOut;

    private volatile Throwable throwable;

    public int getExitValue() {
        return this.exitValue;
    }

    public void setExitValue(int exitValue) {
        this.exitValue = exitValue;
    }

    public String getErrorOut() {
        return this.errorOut;
    }

    public void setErrorOut(String errorOut) {
        this.errorOut = errorOut;
    }

    public String getStdOut() {
        return this.stdOut;
    }

    public void setStdOut(String stdOut) {
        this.stdOut = stdOut;
    }

    public Throwable getThrown() {
        return this.throwable;
    }

    public void setThrown(Throwable error) {
        this.throwable = error;
    }

    public String toString() {
        return "ProcessResult{exitValue=" + this.exitValue + ", errorOut='" + ((this.errorOut == null) ? "" : this.errorOut.trim()) + '\'' + ", stdOut='" + ((this.stdOut == null) ? "" : this.stdOut.trim()) + '\'' + '}';
    }

    public void addNiceOutput(StringBuilder builder) {
        builder.append("\nexit status: ");
        builder.append(getExitValue());
        if (this.stdOut != null && !this.stdOut.isEmpty()) {
            builder.append("\noutput:\n");
            builder.append(this.stdOut.trim());
        }
        if (this.errorOut != null && !this.errorOut.isEmpty()) {
            builder.append("\nerror:\n");
            builder.append(this.errorOut.trim());
        }
        if (this.throwable != null) {
            builder.append("\nthrown:\n");
            builder.append(this.throwable.toString());
        }
    }
}
