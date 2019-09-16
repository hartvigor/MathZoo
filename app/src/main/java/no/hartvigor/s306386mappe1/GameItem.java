package no.hartvigor.s306386mappe1;

public class GameItem {
    private String question;
    private String answer;

    public GameItem(String question, String answer){
        this.question = question;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }
}
