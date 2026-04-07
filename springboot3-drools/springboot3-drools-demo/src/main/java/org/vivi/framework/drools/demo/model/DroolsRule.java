package org.vivi.framework.drools.demo.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;


@Data
@ToString
public class DroolsRule {

    /**
     * 规则id
     */
    private Long ruleId;
    /**
     * kbase的名字
     */
    private String kieBaseName;
    /**
     * 设置该kbase需要从那个目录下加载文件，这个是一个虚拟的目录，相对于 `src/main/resources`
     * 比如：kiePackageName=rules/rule01 那么当前规则文件写入路径为： kieFileSystem.write("src/main/resources/rules/rule01/1.drl")
     */
    private String kiePackageName;
    /**
     * 规则内容
     */
    private String ruleContent;
    /**
     * 规则创建时间
     */
    private Date createdTime;
    /**
     * 规则更新时间
     */
    private Date updateTime;

    public void validate() {
        if (this.ruleId == null || isBlank(kieBaseName) || isBlank(kiePackageName) || isBlank(ruleContent)) {
            throw new RuntimeException("参数有问题");
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }
}
