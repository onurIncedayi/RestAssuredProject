package Campus.RES_5.Model;

import java.util.Arrays;

public class Position {

    private String name;
    private String shortName;
    private String id;
    private String[] translateName;
    private String tenantId;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getShortName() {return shortName;}

    public void setShortName(String shortName) {this.shortName = shortName;}

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String[] getTranslateName() {return translateName;}

    public void setTranslateName(String[] translateName) {this.translateName = translateName;}

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "Position{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", id='" + id + '\'' +
                ", translateName=" + Arrays.toString(translateName) +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
