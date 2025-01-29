import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class StudentGradeView extends JFrame {
    private final ArrayList<Student> students;

    // กำหนดค่าพื้นฐานสำหรับสไตล์
    private static final Color PINK = new Color(255, 20, 147);
    private static final Color BACKGROUND_COLOR = new Color(255, 204, 225);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font TEXT_FIELD_FONT = new Font("Arial", Font.PLAIN, 24);

    public StudentGradeView() {
        students = new ArrayList<>();

        setTitle("Student Grading System");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // ส่วนฟอร์ม (Form Panel)
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 7, 7));
        formPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        formPanel.setBackground(BACKGROUND_COLOR);

        // เพิ่มฟอร์มทั้งหมดโดยใช้ helper method
        formPanel.add(createLabel("Student ID:"));
        JTextField txtStudentID = createTextField();
        formPanel.add(txtStudentID);

        formPanel.add(createLabel("Student Name:"));
        JTextField txtStudentName = createTextField();
        formPanel.add(txtStudentName);

        formPanel.add(createLabel("Homework Score (Max 30%):"));
        JTextField txtHomeworkScore = createTextField();
        formPanel.add(txtHomeworkScore);

        formPanel.add(createLabel("Test Score (Max 70%):"));
        JTextField txtTestScore = createTextField();
        formPanel.add(txtTestScore);

        // เพิ่มส่วนต่าง ๆ ลงใน JFrame
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);

        // ส่วนปุ่ม (Button Panel)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton btnAddStudent = new RoundedButton("Add Student");
        JButton btnUpdateStudent = new RoundedButton("Update Student");
        JButton btnDeleteStudent = new RoundedButton("Delete Student");
        JButton btnViewGrades = new RoundedButton("View Grades");
        JButton btnBack = new RoundedButton("Back");

        // ActionListener สำหรับปุ่ม Add Student
        btnAddStudent.addActionListener(e -> handleAddStudent(txtStudentID, txtStudentName, txtHomeworkScore, txtTestScore));

        // ActionListener สำหรับปุ่ม Update Student
        btnUpdateStudent.addActionListener(e -> handleUpdateStudent(txtStudentID, txtStudentName, txtHomeworkScore, txtTestScore));

        // ActionListener สำหรับปุ่ม Delete Student
        btnDeleteStudent.addActionListener(e -> handleDeleteStudent(txtStudentID));

        // ActionListener สำหรับปุ่ม View Grades
        btnViewGrades.addActionListener(e -> new StudentTableView(students));

        // ActionListener สำหรับปุ่ม Back
        btnBack.addActionListener(e -> {
            dispose(); // ปิดหน้าต่างปัจจุบัน
            Main.main(null); // กลับไปหน้าหลัก
        });

        buttonPanel.add(btnAddStudent);
        buttonPanel.add(btnUpdateStudent);
        buttonPanel.add(btnDeleteStudent);
        buttonPanel.add(btnViewGrades);
        buttonPanel.add(btnBack);

        // เพิ่มส่วนต่าง ๆ ลงใน JFrame
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(PINK);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(TEXT_FIELD_FONT);
        textField.setForeground(PINK);
        textField.setBorder(BorderFactory.createLineBorder(PINK, 2));
        return textField;
    }

    private void handleAddStudent(JTextField txtStudentID, JTextField txtStudentName, JTextField txtHomeworkScore, JTextField txtTestScore) {
        try {
            String studentID = txtStudentID.getText().trim();
            String studentName = txtStudentName.getText().trim();
            double homeworkScore = Double.parseDouble(txtHomeworkScore.getText().trim());
            double testScore = Double.parseDouble(txtTestScore.getText().trim());

            if (homeworkScore > 30 || testScore > 70) {
                JOptionPane.showMessageDialog(this, "Scores exceed maximum values!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            students.add(new Student(studentID, studentName, homeworkScore, testScore));
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields(txtStudentID, txtStudentName, txtHomeworkScore, txtTestScore);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateStudent(JTextField txtStudentID, JTextField txtStudentName, JTextField txtHomeworkScore, JTextField txtTestScore) {
        String studentID = txtStudentID.getText().trim();
        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Student ID to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Student student : students) {
            if (student.getStudentID().equals(studentID)) {
                try {
                    String studentName = txtStudentName.getText().trim();
                    double homeworkScore = Double.parseDouble(txtHomeworkScore.getText().trim());
                    double testScore = Double.parseDouble(txtTestScore.getText().trim());

                    if (homeworkScore > 30 || testScore > 70) {
                        JOptionPane.showMessageDialog(this, "Scores exceed maximum values!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    student.setStudentName(studentName);
                    student.setHomeworkScore(homeworkScore);
                    student.setTestScore(testScore);

                    JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Student ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void handleDeleteStudent(JTextField txtStudentID) {
        String studentID = txtStudentID.getText().trim();

        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Student ID to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentID().equals(studentID)) {
                students.remove(i);
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Student ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void clearFields(JTextField txtStudentID, JTextField txtStudentName, JTextField txtHomeworkScore, JTextField txtTestScore) {
        txtStudentID.setText("");
        txtStudentName.setText("");
        txtHomeworkScore.setText("");
        txtTestScore.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGradeView::new);
    }

    static class RoundedButton extends JButton {
        private static final int RADIUS = 40;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 48));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(PINK);
            g2.fillRoundRect(2, 2, getWidth() - 8 , getHeight() - 8, RADIUS, RADIUS);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(8));
            g2.drawRoundRect(4, 4, getWidth() - 8, getHeight() - 8, RADIUS, RADIUS);
            g2.dispose();
        }
    }
}