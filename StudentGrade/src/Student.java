public class Student extends AbstractStudent implements GradeCalculatorInterface {
    private double homeworkScore;
    private double testScore;

    public Student(String studentID, String studentName, double homeworkScore, double testScore) {
        super(studentID, studentName); // เรียกใช้งาน Constructor ของ AbstractStudent
        this.homeworkScore = homeworkScore;
        this.testScore = testScore;
    }

    @Override
    public double calculateGrade() {

        return calculateTotalScore();
    }

    @Override
    public double calculateTotalScore() {
        return homeworkScore + testScore;
    }

    // Getter methods (เหมือนเดิม)
    public String getStudentID() {
        return super.getStudentID(); // เรียก Getter จาก AbstractStudent
    }
    public String getStudentName() {
        return super.getStudentName(); // เรียก Getter จาก AbstractStudent
    }

    public double getHomeworkScore() {
        return homeworkScore;
    }

    public double getTestScore() {
        return testScore;
    }

    // Setter methods for updating values (เหมือนเดิม)
    public void setHomeworkScore(double homeworkScore) {
        this.homeworkScore = homeworkScore;
    }

    public void setTestScore(double testScore) {
        this.testScore = testScore;
    }

    public void setStudentName(String studentName) {
        super.setStudentName(studentName); // เรียก Setter จาก AbstractStudent
    }
    public void setStudentID(String studentID) {
        super.setStudentID(studentID); // เรียก Setter จาก AbstractStudent
    }


}