package no.hartvigor.s306386mappe1;

import java.io.Serializable;

public class GameItem implements Serializable {
    private String question;
    private String answer;
    private int Id;

    private boolean answered;
    private boolean correct;

    public GameItem(int Id, String question, String answer){
        this.Id = Id;
        this.question = question;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public int getId() {
        return Id;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }


    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

}
