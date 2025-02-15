package org.vivi.framework.generator.ddl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.vivi.framework.generator.ddl.utils.AnalysisUtil;

import java.io.File;

@SpringBootTest
class GeneratorDdlApplicationTests {

    @Test
    void testGenerator() {
        String separator = File.separator;
        AnalysisUtil.printDDL(System.getProperty("user.dir") +separator+ "ddl_template.xlsx");
    }

}
