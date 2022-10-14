package Campus.RES_2.Model;

    public class Attestations {
        private String nameInput;
        private String id;

        public String getNameInput() {
            return nameInput;
        }

        public void setNameInput(String nameInput) {
            this.nameInput = nameInput;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Attestations{" +
                    "nameInput='" + nameInput + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }


