package main.response;

public class GetUserResponse{
    private String nickname;
    private int score;
    private int rank;

    public GetUserResponse() {
    }

    public GetUserResponse(String nickname, int score, int rank) {
        this.nickname = nickname;
        this.score = score;
        this.rank = rank;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
