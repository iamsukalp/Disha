package com.appsters.disha.Models;

public class FAQModal {
    String Title;
    String Question;
    String Answers;
    private boolean expanded;

    public FAQModal(){}

    public FAQModal(String title, String question, String answers) {
        Title = title;
        Question = question;
        Answers = answers;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswers() {
        return Answers;
    }

    public void setAnswers(String answers) {
        Answers = answers;
    }
}
