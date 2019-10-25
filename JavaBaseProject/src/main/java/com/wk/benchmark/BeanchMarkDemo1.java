package com.wk.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(value = {Mode.Throughput,Mode.AverageTime})
@OutputTimeUnit(value = TimeUnit.SECONDS)
@State(Scope.Thread) // 每个测试线程一个实例
public class BeanchMarkDemo1 {

    @Benchmark
    public void bench(){
        add(1,1);
    }

    private static int add(int a,int b){
        return a+b;
    }

    @Benchmark
    public String stringConcat(){
        String a = "a";
        String b = "b";
        String c = "c";
        return a+b+c;
    }

    public static void testBench() throws RunnerException {
        Options options = new OptionsBuilder()
                .include(BeanchMarkDemo1.class.getSimpleName()) // 表示测试哪个文件
                .exclude("stringConcat")        // 移除哪个类
                .forks(2)                   // 做2轮测试
                .warmupIterations(5)        // 预热次数
                .measurementIterations(5)   // 测试次数
                .build();
        new Runner(options).run();
    }

    public static void testStringConcat() throws RunnerException {
        Options options = new OptionsBuilder()
                .include(BeanchMarkDemo1.class.getSimpleName())
                .exclude("bench")
                .forks(2)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();
        new Runner(options).run();
    }

    public static void main(String[] args) throws RunnerException {
        testBench();
    }

}
