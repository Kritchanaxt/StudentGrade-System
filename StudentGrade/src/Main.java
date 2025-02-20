import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main {
    private static Student[] studentList = getStudentList();
    private static int studentCount = 0; // เพิ่มตัวแปรนับจำนวนนักเรียนใน Array
    private static final int MAX_STUDENTS_ARRAY = 100; // กำหนดขนาด Array สูงสุด (ตัวอย่าง: 100)

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Student Grade System");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1920, 1080);
            mainFrame.setLayout(new GridBagLayout());
            mainFrame.getContentPane().setBackground(new Color(255, 204, 225));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            JButton btnGradeCalc = new RoundedButton("Grade Calculate");
            JButton btnStudentManage = new RoundedButton("Student Management");
            JButton btnStudentView = new RoundedButton("Student Information Table");


            btnGradeCalc.addActionListener(e -> {
                mainFrame.dispose();
                new GradeCalculatorView();
            });

            btnStudentManage.addActionListener(e -> {
                JPanel panel = new JPanel();
                JTextField numStudentsField = new JTextField(10);
                panel.add(new JLabel("Number of Students:"));
                panel.add(numStudentsField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Set Number of Students",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int numStudents = Integer.parseInt(numStudentsField.getText());
                        if (numStudents > 0 && numStudents <= MAX_STUDENTS_ARRAY) { // ตรวจสอบขนาด Array สูงสุด
                            mainFrame.dispose();
                            StudentManagementView studentManagementView = new StudentManagementView(studentList, studentCount); // ส่ง Array และ studentCount
                            studentManagementView.setupFormPanel(numStudents);
                            studentManagementView.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Please enter a number of students between 1 and " + MAX_STUDENTS_ARRAY, "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter the number of students as a number", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnStudentView.addActionListener(e -> {
                System.out.println("Student List ก่อนเปิด StudentTableView: " + java.util.Arrays.toString(studentList)); // แสดง Array ใน Console
                mainFrame.dispose();
                // **ส่ง Array และ studentCount ให้ StudentTableView**
                new StudentTableView(studentList, studentCount);
            });

            gbc.gridx = 0;
            gbc.gridy = 0;
            mainFrame.add(btnGradeCalc, gbc);

            gbc.gridy = 1;
            mainFrame.add(btnStudentManage, gbc);

            gbc.gridy = 2;
            mainFrame.add(btnStudentView, gbc);

            mainFrame.setMinimumSize(new Dimension(600, 400));
            mainFrame.setVisible(true);
        });
    }

    // ฟังก์ชันสำหรับดึงข้อมูลนักเรียน (getStudentList) - **แก้ไขให้คืนค่า Student[] Array**
    private static Student[] getStudentList() {
        if (studentList == null) {
            // **สร้าง Array Student[] ขนาด MAX_STUDENTS_ARRAY**
            studentList = new Student[MAX_STUDENTS_ARRAY];
        }
        return studentList;
    }

    // ฟังก์ชัน getStudentCount สำหรับเข้าถึงตัวแปร studentCount จากภายนอก
    public static int getStudentCount() {
        return studentCount;
    }

    // ฟังก์ชัน setStudentCount สำหรับกำหนดค่า studentCount จากภายนอก
    public static void setStudentCount(int count) {
        studentCount = count;
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
            g2.setColor(new Color(255, 20, 147));
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