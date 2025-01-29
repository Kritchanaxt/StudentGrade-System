import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentTableView extends JFrame {
    private final JTable table;
    private final ArrayList<Student> students;
    private final DefaultTableModel tableModel;

    public StudentTableView(ArrayList<Student> students) {
        this.students = students;

        setTitle("Student Grades");
        setSize(1920, 1080);  // ปรับขนาดหน้าต่างให้พอดีกับตารางใหญ่
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Student ID", "Student Name", "Total Score", "Calculated Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        populateTable();


        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ป้องกันไม่ให้แก้ไขข้อมูลในเซลล์
            }
        };

        // ปรับขนาดฟอนต์ในหัวตาราง (JTableHeader)
        Font headerFont = new Font("Arial", Font.BOLD, 32);  
        table.getTableHeader().setFont(headerFont);

        // ปรับขนาดของคอลัมน์ให้เหมาะสมกับข้อมูลในตาราง
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(150); // ปรับขนาดคอลัมน์ Student ID
        table.getColumnModel().getColumn(1).setPreferredWidth(300); // ปรับขนาดคอลัมน์ Student Name
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // ปรับขนาดคอลัมน์ Total Score
        table.getColumnModel().getColumn(3).setPreferredWidth(200); // ปรับขนาดคอลัมน์ Calculated Grade

        // ปรับขนาดฟอนต์ให้ใหญ่ขึ้นเพื่อความสะดวกในการอ่าน
        table.setFont(new Font("Arial", Font.PLAIN, 24));  // ปรับขนาดฟอนต์ให้ใหญ่ขึ้น

        // ปรับความสูงของแถวเพื่อให้มองเห็นได้ชัดเจน
        table.setRowHeight(40);  // กำหนดความสูงแถวให้เป็น 40 พิกเซล

        // ตั้งค่าสีพื้นหลังของตารางให้เป็นสีชมพูอ่อน
        table.setBackground(new Color(255, 228, 225)); // สีชมพูอ่อน
        table.setGridColor(new Color(255, 105, 180)); // เส้นกริดสีชมพูเข้ม

        // ปรับหัวตารางให้มีพื้นหลังสีชมพูและตัวอักษรสีขาว
        table.getTableHeader().setBackground(new Color(255, 105, 180)); // สีพื้นหลังหัวตารางเป็นชมพูเข้ม
        table.getTableHeader().setForeground(Color.WHITE); // ตัวอักษรในหัวตารางเป็นสีขาว

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // สร้างปุ่มสำหรับ Update, Delete และ Back
        JButton btnUpdate = new RoundedButton("Update");
        JButton btnDelete = new RoundedButton("Delete");
        JButton btnBack = new RoundedButton("Back");

        // เพิ่ม ActionListener สำหรับ Update และ Delete
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());

        btnBack.addActionListener(e -> {
            dispose(); // ปิดหน้าต่างปัจจุบัน
            Main.main(null); // กลับไปที่หน้าหลัก
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 204, 225));

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ฟังก์ชันสำหรับกรอกข้อมูลลงในตาราง
    private void populateTable() {
        tableModel.setRowCount(0); // ลบแถวเดิมออกก่อน
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getStudentID(),
                    student.getStudentName(),
                    student.calculateTotalScore(),
                    student.calculateGrade()
            });
        }
    }

    // ฟังก์ชันสำหรับอัพเดทข้อมูลของนักเรียน
    private void updateStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studentID = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = students.stream()
                .filter(s -> s.getStudentID().equals(studentID))
                .findFirst()
                .orElse(null);

        if (student != null) {
            JTextField txtStudentName = new JTextField(student.getStudentName());
            JTextField txtHomeworkScore = new JTextField(String.valueOf(student.getHomeworkScore()));
            JTextField txtTestScore = new JTextField(String.valueOf(student.getTestScore()));

            Object[] message = {
                    "Student Name:", txtStudentName,
                    "Homework Score:", txtHomeworkScore,
                    "Test Score:", txtTestScore
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Update Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    student.setStudentName(txtStudentName.getText().trim());
                    student.setHomeworkScore(Double.parseDouble(txtHomeworkScore.getText().trim()));
                    student.setTestScore(Double.parseDouble(txtTestScore.getText().trim()));
                    populateTable(); // รีเฟรชตาราง
                    JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // ฟังก์ชันสำหรับลบข้อมูลของนักเรียน
    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Delete Student", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            String studentID = (String) tableModel.getValueAt(selectedRow, 0);
            students.removeIf(student -> student.getStudentID().equals(studentID));
            populateTable(); // รีเฟรชตาราง
            JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // ตัวอย่างข้อมูลสำหรับการทดสอบ
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("S001", "John Doe", 25.0, 65.0));
        students.add(new Student("S002", "Jane Smith", 20.0, 70.0));
        new StudentTableView(students);
    }

    // ปุ่มที่มีขอบโค้งสำหรับใช้สไตล์ที่สอดคล้องกัน
    static class RoundedButton extends JButton {
        private static final int RADIUS = 40; // ปรับขนาดความโค้งของมุม

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false); // ปิดการเติมพื้นหลังปุ่ม
            setFocusPainted(false); // เอาขอบโฟกัสออก
            setForeground(new Color(255, 255, 255)); // สีตัวอักษรขาว
            setFont(new Font("Arial", Font.BOLD, 48)); // ฟอนต์ตัวหนา
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // กำหนด Padding
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // กำหนดสีพื้นหลังของปุ่ม
            g2.setColor(new Color(255, 20, 147)); // สี Deep Pink
            g2.fillRoundRect(2, 2, getWidth() - 8 , getHeight() - 8, RADIUS, RADIUS);

            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // สีขอบของปุ่ม
            g2.setColor(Color.WHITE); // สีขอบขาว
            g2.setStroke(new BasicStroke(8)); // ความหนาของขอบ
            g2.drawRoundRect(4, 4, getWidth() - 8, getHeight() - 8, RADIUS, RADIUS);

            g2.dispose();
        }
    }
}