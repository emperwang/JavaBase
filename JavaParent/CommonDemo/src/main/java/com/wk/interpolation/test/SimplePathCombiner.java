package com.wk.interpolation.test;

import com.wk.interpolation.InterpolationEngine;
import com.wk.interpolation.StringCombiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class SimplePathCombiner<T> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Object context;
    private final String template;
    private final StringCombiner combiner;

    public SimplePathCombiner(Object context, String template) {
        this.context = context;
        this.template = template;
        this.combiner = createCombiner();
        logger.info(combiner.toString());
    }

    private StringCombiner createCombiner() {
        InterpolationEngine engine = InterpolationEngine.create();
        return engine.compile(template, context);
    }

    public String combine() {
        combiner.bind("dt", new Date());
        return combiner.combine();
    }

    public void bind(String name, T value) {
    }

    public StringCombiner getCombiner() {
        return combiner;
    }

    public void bindVar(String name, Object value) {
        combiner.bind(name, value);
    }
}