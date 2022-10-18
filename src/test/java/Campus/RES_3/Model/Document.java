package Campus.RES_3.Model;

import java.util.Arrays;


public class Document {


        private String name;
        private String id;

        public String[] attachmentStages;

        private String description;
        private String[] translateName;
        private String schoolId;
        private Boolean active;
        private Boolean required;
        private Boolean useCamera;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getAttachmentStages() {
        return attachmentStages;
    }

    public void setAttachmentStages(String[] attachmentStages) {
        this.attachmentStages = attachmentStages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTranslateName() {
        return translateName;
    }

    public void setTranslateName(String[] translateName) {
        this.translateName = translateName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getUseCamera() {
        return useCamera;
    }

    public void setUseCamera(Boolean useCamera) {
        this.useCamera = useCamera;
    }

    @Override
    public String toString() {
        return "Document{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", attachmentStages=" + Arrays.toString(attachmentStages) +
                ", description='" + description + '\'' +
                ", translateName=" + Arrays.toString(translateName) +
                ", schoolId='" + schoolId + '\'' +
                ", active=" + active +
                ", required=" + required +
                ", useCamera=" + useCamera +
                '}';
    }
}



