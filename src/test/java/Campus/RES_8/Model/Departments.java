package Campus.RES_8.Model;

import java.util.Arrays;

public class Departments {
    private String id;
    private String name;
    private String code;
    private boolean active = true;
    private String schoolId = "6343bf893ed01f0dc03a509a";
    private String[] sections;
    private String[] constants;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String[] getSections() {
        return sections;
    }

    public void setSections(String[] sections) {
        this.sections = sections;
    }

    public String[] getConstants() {
        return constants;
    }

    public void setConstants(String[] constants) {
        this.constants = constants;
    }

    @Override
    public String toString() {
        return "Departments{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", active=" + active +
                ", schoolId='" + schoolId + '\'' +
                ", sections=" + Arrays.toString(sections) +
                ", constants=" + Arrays.toString(constants) +
                '}';
    }
}
