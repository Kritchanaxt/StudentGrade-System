import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentTableView extends JFrame {
    private final JTable table;
    private final ArrayList<Student> students;
    private final DefaultTableModel tableModel;
    private static final Color PINK = new Color(255, 20, 147);


    // **[ประกาศเป็น Instance Variable]**
    private JLabel averageLabel; // ประกาศ averageLabel เป็น Instance Variable ที่นี่

    public StudentTableView(ArrayList<Student> students) {
        this.students = students;

        setTitle("Student Grades");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initial ค่า averageLabel ที่นี่ (สร้าง JLabel Object)
        averageLabel = new JLabel();

        String[] columns = {"Student ID", "Student Name", "Total Score", "Calculated Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        populateTable(); // เรียก populateTable() ครั้งแรกเพื่อแสดงข้อมูลเริ่มต้น (เรียกหลัง Initial averageLabel)

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

        updateAverageLabel(); // เรียก updateAverageLabel() เริ่มต้นเพื่อแสดงค่าเฉลี่ยครั้งแรก (เรียกหลัง Initial averageLabel)

        buttonPanel.add(averageLabel);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        for (Student student : students) {
            double total = student.calculateTotalScore();
            String grade = calculateLetterGrade(total);
            tableModel.addRow(new Object[]{
                    student.getStudentID(),
                    student.getStudentName(),
                    student.calculateTotalScore(),
                    grade
            });
        }
        updateAverageLabel(); // **[เรียก updateAverageLabel() ทุกครั้งหลัง populateTable()]**
    }


    private void updateAverageLabel() {
        double overallAverage = calculateOverallAverage();
        String formattedAverage = String.format("%.2f", overallAverage);

        // ปรับข้อความ Label เป็น "Total All Average" และจัดรูปแบบ
        averageLabel.setText("Total All Average: " + formattedAverage);
        averageLabel.setForeground(PINK);
        averageLabel.setHorizontalAlignment(SwingConstants.LEFT); // จัดข้อความชิดซ้าย


        // ปรับขนาด Font ให้ใหญ่ขึ้น
        Font currentFont = averageLabel.getFont(); // ดึง Font ปัจจุบัน
        Font largerFont = currentFont.deriveFont(Font.BOLD, 36); // สร้าง Font ใหม่ให้ใหญ่ขึ้น (ขนาด 36, ตัวหนา)
        averageLabel.setFont(largerFont); // ตั้ง Font ใหม่ให้กับ averageLabel


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
                    populateTable(); // **[เรียก populateTable() หลัง Update]**
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
            String studentID = (String) tableModel.getValueAt(selectedRow, 0);
            students.removeIf(student -> student.getStudentID().equals(studentID));
            populateTable(); // **[เรียก populateTable() หลัง Delete]**
            JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private double calculateOverallAverage() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double totalScoreSum = 0;
        for (Student student : students) {
            totalScoreSum += student.calculateTotalScore();
        }
        return totalScoreSum / students.size();
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