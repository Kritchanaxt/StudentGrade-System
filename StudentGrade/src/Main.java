import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class Main {
    // **ArrayList สำหรับเก็บข้อมูลนักเรียนแบบ static (เหมือนเดิม)**
    private static ArrayList<Student> studentList = getStudentList();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Student Grade System"); // Title เป็นภาษาอังกฤษ
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1920, 1080);
            mainFrame.setLayout(new GridBagLayout());
            mainFrame.getContentPane().setBackground(new Color(255, 204, 225)); // สีพื้นหลังชมพูอ่อน (Misty Rose)

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            // สร้างปุ่มที่มีสไตล์
            JButton btnGradeCalc = new RoundedButton("Grade Calculate"); // Text ปุ่มเป็นภาษาอังกฤษ
            JButton btnStudentManage = new RoundedButton("Student Management"); // Text ปุ่มเป็นภาษาอังกฤษ
            JButton btnStudentView = new RoundedButton("Student Information Table"); // Text ปุ่มเป็นภาษาอังกฤษ



            // Action Listener สำหรับแต่ละปุ่ม
            btnGradeCalc.addActionListener(e -> {
                mainFrame.dispose();
                new GradeCalculatorView();
            });

            btnStudentManage.addActionListener(e -> {
                // สร้าง Dialog ขึ้นมาให้ผู้ใช้กรอกจำนวนนักเรียน
                JPanel panel = new JPanel();
                JTextField numStudentsField = new JTextField(5);
                panel.add(new JLabel("Number of Students:")); // Label ใน Dialog เป็นภาษาอังกฤษ
                panel.add(numStudentsField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Set Number of Students", // Title ใน Dialog เป็นภาษาอังกฤษ
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int numStudents = Integer.parseInt(numStudentsField.getText());
                        if (numStudents > 0) {
                            mainFrame.dispose();
                            StudentManagementView studentManagementView = new StudentManagementView(studentList);
                            studentManagementView.setupFormPanel(numStudents);
//                            studentManagementView.getSubmitButton().addActionListener(submitEvent -> {
//                                studentManagementView.submitStudentData();
//                            });
                            studentManagementView.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Please enter a number of students greater than 0", "Error", // Message ใน Dialog เป็นภาษาอังกฤษ
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter the number of students as a number", "Error", // Message ใน Dialog เป็นภาษาอังกฤษ
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnStudentView.addActionListener(e -> {
                System.out.println("Student List ก่อนเปิด StudentTableView: " + studentList);
                mainFrame.dispose();
                // **เปิด StudentTableView (หน้าจอ Table) สำหรับ View Student**
                new StudentTableView(studentList);
            });

            // เพิ่มปุ่มลงใน Layout
            gbc.gridx = 0;
            gbc.gridy = 0;
            mainFrame.add(btnGradeCalc, gbc);

            gbc.gridy = 1;
            mainFrame.add(btnStudentManage, gbc); // เปลี่ยนปุ่ม Student System เป็น Student Management

            gbc.gridy = 2;
            mainFrame.add(btnStudentView, gbc); // เพิ่มปุ่ม Student View

            mainFrame.setMinimumSize(new Dimension(600, 400));
            mainFrame.setVisible(true);
        });
    }

    // ฟังก์ชันสำหรับดึงข้อมูลนักเรียน (getStudentList) - **แก้ไขให้คืนค่า studentList ที่เป็น static** (เหมือนเดิม)
    private static ArrayList<Student> getStudentList() {
        if (studentList == null) {
            studentList = new ArrayList<>();

        }
        return studentList;
    }

    // คลาส RoundedButton (ปุ่มที่มีมุมโค้ง) (เหมือนเดิม)
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

            // วาดพื้นหลังปุ่มเป็นสีชมพูเข้ม
            g2.setColor(new Color(255, 20, 147)); // สีชมพูเข้ม (Deep Pink)
            g2.fillRoundRect(2, 2, getWidth() - 8 , getHeight() - 8, RADIUS, RADIUS);

            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // วาดขอบสีขาว
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(8)); // ปรับความหนาของขอบให้พอดี
            g2.drawRoundRect(4, 4, getWidth() - 8, getHeight() - 8, RADIUS, RADIUS); // ปรับตำแหน่งและขนาดขอบ

            g2.dispose();
        }
    }
}