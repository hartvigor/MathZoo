package no.hartvigor.s306386mappe1;

import java.io.Serializable;

public class GameItem implements Serializable {
    private String question;
    private String answer;
    private int Id;

    private boolean answered;
    private boolean correct;
    private long inputAnswer;

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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public long getInputAnswer() {
        return inputAnswer;
    }

    public void setInputAnswer(long inputAnswer) {
        this.inputAnswer = inputAnswer;
    }
}
