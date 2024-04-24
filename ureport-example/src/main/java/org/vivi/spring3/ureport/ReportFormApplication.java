package org.vivi.spring3.ureport;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * http://localhost:8080/ureport/designer
 */
@SpringBootApplication
//@ComponentScan("org.spring.ureport")
public class ReportFormApplication {

    /**
     *
     * 主要功能: 报表服务主程序
     * @param args  命令行参数。
     */
    public static void main(String[] args) {
        SpringApplication.run(ReportFormApplication.class, args);

    }

}
