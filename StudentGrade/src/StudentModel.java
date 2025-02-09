public class StudentModel {
    private String studentID;
    private String studentName;
    private double homeworkScore;
    private double testScore;

    public StudentModel(String studentID, String studentName, double homeworkScore, double testScore) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.homeworkScore = homeworkScore;
        this.testScore = testScore;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public double getHomeworkScore() {
        return homeworkScore;
    }

    public double getTestScore() {
        return testScore;
    }

    public double getTotalScore() {
        return homeworkScore + testScore;
    }
}