package com.wk.config;

import com.wk.ImportSelect.MyImportSelector;
import com.wk.beans.Color;
import com.wk.beans.Red;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {Color.class, Red.class, MyImportSelector.class})
public class ImportSelectConfig {
}
