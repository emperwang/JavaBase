package com.java.modules.sink.streamimg.conf;

import com.java.modules.sink.streamimg.sinker.SinkerConfig;

public class FileSinkerConfig extends SinkerConfig {
    private String prefix;
    private String label;
    private String suffix;
    private String outputPath;
    private String hdfsOutputPath;
    private String appendLineEnding;
    private String lineEnding;
    private long rollingPeriod = 60L;
    private long lineThreshold;
    private long sizeThreshold;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getHdfsOutputPath() {
        return hdfsOutputPath;
    }

    public void setHdfsOutputPath(String hdfsOutputPath) {
        this.hdfsOutputPath = hdfsOutputPath;
    }

    public String getAppendLineEnding() {
        return appendLineEnding;
    }

    public void setAppendLineEnding(String appendLineEnding) {
        this.appendLineEnding = appendLineEnding;
    }

    public String getLineEnding() {
        return lineEnding;
    }

    public void setLineEnding(String lineEnding) {
        this.lineEnding = lineEnding;
    }

    public long getRollingPeriod() {
        return rollingPeriod;
    }

    public void setRollingPeriod(long rollingPeriod) {
        this.rollingPeriod = rollingPeriod;
    }

    public long getLineThreshold() {
        return lineThreshold;
    }

    public void setLineThreshold(long lineThreshold) {
        this.lineThreshold = lineThreshold;
    }

    public long getSizeThreshold() {
        return sizeThreshold;
    }

    public void setSizeThreshold(long sizeThreshold) {
        this.sizeThreshold = sizeThreshold;
    }
}

