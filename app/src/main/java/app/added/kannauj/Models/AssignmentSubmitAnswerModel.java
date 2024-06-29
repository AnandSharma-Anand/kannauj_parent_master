package app.added.kannauj.Models;

public class AssignmentSubmitAnswerModel {
    String AssignmentAnswersImagePath;
    String AnswerImageName;

    public AssignmentSubmitAnswerModel(String assignmentAnswersImagePath, String answerImageName) {
        AssignmentAnswersImagePath = assignmentAnswersImagePath;
        AnswerImageName = answerImageName;
    }

    public String getAssignmentAnswersImagePath() {
        return AssignmentAnswersImagePath;
    }

    public void setAssignmentAnswersImagePath(String assignmentAnswersImagePath) {
        AssignmentAnswersImagePath = assignmentAnswersImagePath;
    }

    public String getAnswerImageName() {
        return AnswerImageName;
    }

    public void setAnswerImageName(String answerImageName) {
        AnswerImageName = answerImageName;
    }
}
