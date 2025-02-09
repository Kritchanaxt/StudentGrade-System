import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentManagementView extends JFrame {
    private final ArrayList<Student> students;
    private final JTable table;
    private final DefaultTableModel tableModel;

    // ฟอร์มสำหรับเพิ่ม/แก้ไขนักเรียน
    private final JTextField txtStudentID = createTextField();
    private final JTextField txtStudentName = createTextField();
    private final JTextField txtHomeworkScore = createTextField();
    private final JTextField txtTestScore = createTextField();

    // กำหนดค่าพื้นฐานสำหรับสไตล์
    private static final Color PINK = new Color(255, 20, 147);
    private static final Color BACKGROUND_COLOR = new Color(255, 204, 225);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font TEXT_FIELD_FONT = new Font("Arial", Font.PLAIN, 24);

    public StudentManagementView(ArrayList<Student> students) {
        this.students = students;

        setTitle("Student Management System");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // ส่วนฟอร์ม (Form Panel)
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 7, 7));
        formPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        formPanel.setBackground(BACKGROUND_COLOR);

        formPanel.add(createLabel("Student ID:"));
        formPanel.add(txtStudentID);

        formPanel.add(createLabel("Student Name:"));
        formPanel.add(txtStudentName);

        formPanel.add(createLabel("Homework Score (Max 30%):"));
        formPanel.add(txtHomeworkScore);

        formPanel.add(createLabel("Test Score (Max 70%):"));
        formPanel.add(txtTestScore);

        // ส่วนตาราง (Table Panel)
        String[] columns = {"Student ID", "Student Name", "Total Score", "Grade"}; // **เอา "Calculated Grade" ออกแล้ว**
        tableModel = new DefaultTableModel(columns, 0);
        populateTable();

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ป้องกันไม่ให้แก้ไขข้อมูลในเซลล์
            }
        };
        configureTableStyle(table);

        JScrollPane scrollPane = new JScrollPane(table); // ใส่ตารางใน ScrollPane

        // ส่วนปุ่ม (Button Panel)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton btnAddStudent = new RoundedButton("Add Student");
        JButton btnUpdate = new RoundedButton("Update");
        JButton btnDelete = new RoundedButton("Delete");
        JButton btnBack = new RoundedButton("Back");

        // ActionListener สำหรับปุ่ม Add Student
        btnAddStudent.addActionListener(e -> handleAddStudent());

        // ActionListener สำหรับปุ่ม Update
        btnUpdate.addActionListener(e -> updateStudent());

        // ActionListener สำหรับปุ่ม Delete
        btnDelete.addActionListener(e -> deleteStudent());

        // ActionListener สำหรับปุ่ม Back
        btnBack.addActionListener(e -> {
            dispose();
            Main.main(null);
        });

        buttonPanel.add(btnAddStudent);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);

        // เพิ่ม Components ลงใน JFrame - จัด Layout ใหม่ให้มีทั้ง Form และ Table
        JPanel centerPanel = new JPanel(new BorderLayout()); // Panel หลักตรงกลาง
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(formPanel, BorderLayout.NORTH); // ฟอร์มอยู่ด้านบน
        centerPanel.add(scrollPane, BorderLayout.CENTER); // ตารางอยู่ตรงกลาง

        add(centerPanel, BorderLayout.CENTER); // เพิ่ม CenterPanel ลง JFrame
        add(buttonPanel, BorderLayout.SOUTH); // ปุ่มอยู่ด้านล่าง

        setVisible(true);
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

    private void configureTableStyle(JTable table) {
        Font headerFont = new Font("Arial", Font.BOLD, 32);
        table.getTableHeader().setFont(headerFont);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // ปรับขนาดคอลัมน์ Grade (เดิมคอลัมน์ที่ 5)
        table.setFont(new Font("Arial", Font.PLAIN, 24));
        table.setRowHeight(40);
        table.setBackground(new Color(255, 228, 225));
        table.setGridColor(new Color(255, 105, 180));
        table.getTableHeader().setBackground(new Color(255, 105, 180));
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private void populateTable() {
        tableModel.setRowCount(0); // ลบแถวเดิมออกก่อน
        for (Student student : students) {
            // double calculatedGrade = student.calculateGrade(); // ไม่ใช้ calculatedGrade แล้ว
            double total = student.calculateTotalScore();
            String grade = calculateLetterGrade(total); // เรียกฟังก์ชันคำนวณเกรด
            tableModel.addRow(new Object[]{
                    student.getStudentID(),
                    student.getStudentName(),
                    student.calculateTotalScore(), // ใช้ TotalScore แทน
                    grade // เพิ่มเกรดลงในแถวข้อมูล
            });
        }
    }

    private String calculateLetterGrade(double calculatedGrade) { // ฟังก์ชันนี้ไม่ได้ใช้แล้ว แต่ยังคงเก็บไว้เผื่ออนาคต
        if (calculatedGrade >= 80) {
            return "A";
        } else if (calculatedGrade >= 70) {
            return "B";
        } else if (calculatedGrade >= 60) {
            return "C";
        } else if (calculatedGrade >= 50) {
            return "D";
        } else {
            return "F";
        }
    }

    private void handleAddStudent() {
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
            populateTable(); // รีเฟรชตาราง
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields(); // เคลียร์ฟิลด์หลังจากเพิ่มข้อมูล
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
            txtStudentName.setText(student.getStudentName());
            txtHomeworkScore.setText(String.valueOf(student.getHomeworkScore()));
            txtTestScore.setText(String.valueOf(student.getTestScore()));
            txtStudentID.setText(student.getStudentID());
            txtStudentID.setEditable(false); // make student ID uneditable during update

            Object[] message = {
                    "Student ID:", txtStudentID,
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
                    clearFields(); // เคลียร์ฟิลด์หลังอัปเดต
                    txtStudentID.setEditable(true); // re-enable editing student ID for next add/update
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                clearFields(); // เคลียร์ฟิลด์หากยกเลิกการอัปเดต
                txtStudentID.setEditable(true); // re-enable editing student ID if update cancelled
            }
        }
    }


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

    private void clearFields() {
        txtStudentID.setText("");
        txtStudentName.setText("");
        txtHomeworkScore.setText("");
        txtTestScore.setText("");
        txtStudentID.setEditable(true); // ensure Student ID is editable when form is cleared
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