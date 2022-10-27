package main.dto;

public class TeamDto {

        private String teamName;
        private String img;

        public TeamDto() {
        }

        public TeamDto(String teamName, String img) {
            this.teamName = teamName;
            this.img = img;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
}
