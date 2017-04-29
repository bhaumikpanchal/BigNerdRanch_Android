package com.geoquiz.bpanchal.geoquiz;

public class Question
{
    private int mTextResId;
    private boolean mAnswerTrue;

    public Question(int textRestId, boolean answerTrue) {
        this.mTextResId = textRestId;
        this.mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
