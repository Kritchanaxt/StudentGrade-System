import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Student Grading System");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1920, 1080);
            mainFrame.setLayout(new GridBagLayout());
            mainFrame.getContentPane().setBackground(new Color(255, 204, 225)); // สีพื้นหลังชมพูอ่อน (Misty Rose)

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            // สร้างปุ่มที่มีสไตล์
            JButton btnGradeCalc = new RoundedButton("Grade Calculator");
            JButton btnStudentSystem = new RoundedButton("Student Grade System");
            JButton btnStudentTable = new RoundedButton("View Student Table");

            // Action Listener สำหรับแต่ละปุ่ม
            btnGradeCalc.addActionListener(e -> {
                mainFrame.dispose(); // ปิดหน้าหลัก
                new GradeCalculatorView(); // เปิดหน้าคำนวณเกรด
            });

            btnStudentSystem.addActionListener(e -> {
                mainFrame.dispose(); // ปิดหน้าหลัก
                new StudentGradeView(); // เปิดระบบจัดการนักเรียน
            });

            btnStudentTable.addActionListener(e -> {
                mainFrame.dispose(); // ปิดหน้าหลัก
                ArrayList<Student> students = getStudentList();
                new StudentTableView(students); // เปิดหน้าตารางนักเรียน
            });

            // เพิ่มปุ่มลงใน Layout
            gbc.gridx = 0;
            gbc.gridy = 0;
            mainFrame.add(btnGradeCalc, gbc);

            gbc.gridy = 1;
            mainFrame.add(btnStudentSystem, gbc);

            gbc.gridy = 2;
            mainFrame.add(btnStudentTable, gbc);

            mainFrame.setMinimumSize(new Dimension(600, 400));
            mainFrame.setVisible(true);
        });
    }

    // สร้างคลาสปุ่มแบบโค้งมน
    static class RoundedButton extends JButton {
        private static final int RADIUS = 40; // ปรับความโค้งของขอบ

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false); // ปิดการวาดพื้นหลังปุ่มเริ่มต้น
            setFocusPainted(false); // เอาเส้นโฟกัสออก
            setForeground(Color.WHITE); // สีตัวอักษรขาว
            setFont(new Font("Arial", Font.BOLD, 48)); // ฟอนต์ตัวหนา
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding (บน, ซ้าย, ล่าง, ขวา)
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

    // ฟังก์ชันสำหรับดึงข้อมูลนักเรียน
    private static ArrayList<Student> getStudentList() {
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("S001", "John Doe", 85, 90));
        students.add(new Student("S002", "Jane Smith", 88, 92));
        students.add(new Student("S003", "Alice Brown", 75, 80));
        students.add(new Student("S004", "Bob Johnson", 95, 98));
        students.add(new Student("S005", "Charlie White", 70, 72));
        return students;
    }
}