package org.vivi.framework.excel.configure.core.beans;

public class ExportFieldBean {

    /**
     * 表格列名描述
     *
     * @return
     */
    private String title;

    /**
     * 表格列名索引位置,从0开始
     * @return
     */
    private int    index;

    /**
     * 表格字段值来源
     * @return
     */
    private String sourceKey;

    /**
     * 日期类型转换
     * @return
     */
    private String formate;

    /**
     * 保留源字段和替换字段,比如想保留id,同时在id后面加入name，
     * 则id.index=1,id.referIndex=2
     * name.index=2,name.referIndex=-1
     * @return
     */
    private int referIndex;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getFormate() {
        return formate;
    }

    public void setFormate(String formate) {
        this.formate = formate;
    }

    public int getReferIndex() {
        return referIndex;
    }

    public void setReferIndex(int referIndex) {
        this.referIndex = referIndex;
    }

    @Override
    public String toString() {
        return "ExportFieldBean{" +
                "title='" + title + '\'' +
                ", index=" + index +
                ", sourceKey='" + sourceKey + '\'' +
                ", formate='" + formate + '\'' +
                ", referIndex=" + referIndex +
                '}';
    }
}
