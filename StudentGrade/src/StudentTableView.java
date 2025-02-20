import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.util.ArrayList;

public class StudentTableView extends JFrame {
    private final JTable table;

    private Student[] students;
    private final DefaultTableModel tableModel;
    private static final Color PINK = new Color(255, 20, 147);
    private int studentCount; // เพิ่มตัวแปร studentCount


    private JLabel averageLabel;

    // **แก้ไข Constructor รับ Student[] array และ studentCount**
    public StudentTableView(Student[] students, int studentCount) {
        this.students = students;
        this.studentCount = studentCount;

        setTitle("Student Grades");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        averageLabel = new JLabel();

        String[] columns = {"Student ID", "Student Name", "Total Score", "Calculated Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        populateTable();

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        configureTableStyle(table);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnUpdate = new RoundedButton("Update");
        JButton btnDelete = new RoundedButton("Delete");
        JButton btnBack = new RoundedButton("Back");

        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnBack.addActionListener(e -> {
            dispose();
            Main.main(null);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 204, 225));

        updateAverageLabel();

        buttonPanel.add(averageLabel);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void populateTable() {
        tableModel.setRowCount(0);

        for (int i = 0; i < studentCount; i++) { // วนลูปเท่ากับ studentCount
            Student student = students[i];
            if (student != null) {
                double total = student.calculateTotalScore();
                String grade = calculateLetterGrade(total);
                tableModel.addRow(new Object[]{
                        student.getStudentID(),
                        student.getStudentName(),
                        student.calculateTotalScore(),
                        grade
                });
            }
        }
        updateAverageLabel();
    }


    private void updateAverageLabel() {
        double overallAverage = calculateOverallAverage();
        String formattedAverage = String.format("%.2f", overallAverage);

        averageLabel.setText("Total All Average: " + formattedAverage);
        averageLabel.setForeground(PINK);
        averageLabel.setHorizontalAlignment(SwingConstants.LEFT);

        Font currentFont = averageLabel.getFont();
        Font largerFont = currentFont.deriveFont(Font.BOLD, 36);
        averageLabel.setFont(largerFont);

        setVisible(true);
    }


    private String calculateLetterGrade(double calculatedGrade) {
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

    private void updateStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studentID = (String) tableModel.getValueAt(selectedRow, 0);
        Student studentToUpdate = null;

        // **แก้ไข: วนลูปหา Student ใน Array students**
        for (int i = 0; i < studentCount; i++) { // วนลูปเท่ากับ studentCount
            if (students[i] != null && students[i].getStudentID().equals(studentID)) { // ตรวจสอบ null ก่อนเทียบ Student ID
                studentToUpdate = students[i];
                break; // เจอแล้วก็ออกจาก Loop
            }
        }


        if (studentToUpdate != null) {
            JTextField txtStudentName = new JTextField(studentToUpdate.getStudentName());
            JTextField txtHomeworkScore = new JTextField(String.valueOf(studentToUpdate.getHomeworkScore()));
            JTextField txtTestScore = new JTextField(String.valueOf(studentToUpdate.getTestScore()));

            Object[] message = {
                    "Student Name:", txtStudentName,
                    "Homework Score:", txtHomeworkScore,
                    "Test Score:", txtTestScore
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Update Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    studentToUpdate.setStudentName(txtStudentName.getText().trim());
                    studentToUpdate.setHomeworkScore(Double.parseDouble(txtHomeworkScore.getText().trim()));
                    studentToUpdate.setTestScore(Double.parseDouble(txtTestScore.getText().trim()));
                    populateTable();
                    JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
            String studentIDToDelete = (String) tableModel.getValueAt(selectedRow, 0);

            // **แก้ไข: ลบ Student ใน Array โดย Shift ข้อมูล**
            int indexToDelete = -1;
            for (int i = 0; i < studentCount; i++) { // วนลูปเท่ากับ studentCount
                if (students[i] != null && students[i].getStudentID().equals(studentIDToDelete)) { // ตรวจสอบ null ก่อนเทียบ Student ID
                    indexToDelete = i;
                    break;
                }
            }

            if (indexToDelete != -1) {
                // Shift ข้อมูลเพื่อลบช่องว่าง
                for (int i = indexToDelete; i < studentCount - 1; i++) {
                    students[i] = students[i + 1];
                }
                students[studentCount - 1] = null; // Set ตัวสุดท้ายเป็น null (optional, for clarity)
                studentCount--; // ลด studentCount ลง
                Main.setStudentCount(studentCount); // อัปเดต studentCount ใน Main

                populateTable();
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    private double calculateOverallAverage() {
        if (studentCount == 0) { // ตรวจสอบ studentCount แทน students.isEmpty()
            return 0.0;
        }
        double totalScoreSum = 0;

        for (int i = 0; i < studentCount; i++) { // วนลูปเท่ากับ studentCount
            Student student = students[i];
            if (student != null) { // ตรวจสอบ null ก่อนเรียก Method
                totalScoreSum += student.calculateTotalScore();
            }
        }
        return totalScoreSum / studentCount; // หารด้วย studentCount แทน students.size()
    }


    private void configureTableStyle(JTable table) {
        Font headerFont = new Font("Arial", Font.BOLD, 32);
        table.getTableHeader().setFont(headerFont);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.setFont(new Font("Arial", Font.PLAIN, 24));
        table.setRowHeight(40);
        table.setBackground(new Color(255, 228, 225));
        table.setGridColor(new Color(255, 105, 180));
        table.getTableHeader().setBackground(new Color(255, 105, 180));
        table.getTableHeader().setForeground(Color.WHITE);
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