package Campus.RES_4.Model;

import java.util.Arrays;

public class Field {

    private String id;
    private String name;
    private String[] translateName;
    private String code;
    private String type;
    private String[] children;
    private String[] listElements;
    private boolean systemField = false;
    private String systemFieldName;
    private String schoolId = "6343bf893ed01f0dc03a509a";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getListElements() {
        return listElements;
    }

    public void setListElements(String[] listElements) {
        this.listElements = listElements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getTranslateName() {
        return translateName;
    }

    public void setTranslateName(String[] translateName) {
        this.translateName = translateName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getChildren() {
        return children;
    }

    public void setChildren(String[] children) {
        this.children = children;
    }

    public boolean isSystemField() {
        return systemField;
    }

    public void setSystemField(boolean systemField) {
        this.systemField = systemField;
    }

    public String getSystemFieldName() {
        return systemFieldName;
    }

    public void setSystemFieldName(String systemFieldName) {
        this.systemFieldName = systemFieldName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public String toString() {
        return "Field{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", translateName=" + Arrays.toString(translateName) +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", children=" + Arrays.toString(children) +
                ", systemField=" + systemField +
                ", systemFieldName='" + systemFieldName + '\'' +
                ", schoolId='" + schoolId + '\'' +
                '}';
    }
}
