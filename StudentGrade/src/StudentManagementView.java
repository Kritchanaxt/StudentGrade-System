import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.util.ArrayList;

public class StudentManagementView extends JFrame {

    private Student[] students;
    private int maxStudents;
    private final JTable table;
    private final DefaultTableModel tableModel;

    private final JPanel idInputPanel = new JPanel();


    private JButton btnAddStudent;

    private int currentStudentCounter = 0;
    private JLabel studentCounterLabel;

    private static final Color PINK = new Color(255, 20, 147);
    private static final Color BACKGROUND_COLOR = new Color(255, 204, 225);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 42);
    private static final Font TEXT_FIELD_FONT = new Font("Arial", Font.PLAIN, 40);

    // **แก้ไข Constructor รับ Student[] array และ studentCount**
    public StudentManagementView(Student[] students, int studentCount) {
        this.students = students;
        this.currentStudentCounter = studentCount; // กำหนดค่า currentStudentCounter เริ่มต้น

        setTitle("Student Management System");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel formPanel = new JPanel();
        formPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JScrollPane idScrollPane = new JScrollPane(idInputPanel);
        idScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        idScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        formPanel.add(idScrollPane);

        add(formPanel, BorderLayout.NORTH);

        String[] columns = {"Student ID", "Student Name", "Total Score", "Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        populateTable();

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        configureTableStyle(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        btnAddStudent = new RoundedButton("Add Student");
        JButton btnBack = new RoundedButton("Back");

        btnAddStudent.addActionListener(e -> updateStudentTable());

        btnBack.addActionListener(e -> {
            dispose();
            Main.main(null);
        });

        buttonPanel.add(btnAddStudent);
        buttonPanel.add(btnBack);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(formPanel, BorderLayout.NORTH);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }


    public void setupFormPanel(int numStudents) {
        this.maxStudents = numStudents;
        idInputPanel.removeAll();
        idInputPanel.setLayout(new BoxLayout(idInputPanel, BoxLayout.Y_AXIS));
        //studentIDFields.clear(); // Removed studentIDFields.clear()

        JPanel singleStudentPanel = new JPanel();
        singleStudentPanel.setLayout(new GridLayout(5, 2, 10, 7));

        currentStudentCounter = Main.getStudentCount(); // ดึงค่า currentStudentCounter จาก Main (ค่าต่อเนื่อง)

        studentCounterLabel = createLabel("Student " + (currentStudentCounter + 1));
        singleStudentPanel.add(studentCounterLabel);
        singleStudentPanel.add(new JLabel(""));

        JTextField txtStudentID_dynamic = createTextField();
        JTextField txtStudentName_dynamic = createTextField();
        JTextField txtHomeworkScore_dynamic = createTextField();
        JTextField txtTestScore_dynamic = createTextField();

        singleStudentPanel.add(createLabel("Student ID:"));
        singleStudentPanel.add(txtStudentID_dynamic);

        singleStudentPanel.add(createLabel("Student Name:"));
        singleStudentPanel.add(txtStudentName_dynamic);

        singleStudentPanel.add(createLabel("Homework Score (Max 30%):"));
        singleStudentPanel.add(txtHomeworkScore_dynamic);

        singleStudentPanel.add(createLabel("Test Score (Max 70%):"));
        singleStudentPanel.add(txtTestScore_dynamic);

        idInputPanel.add(singleStudentPanel);
        //studentIDFields.add(txtStudentID_dynamic);

        idInputPanel.revalidate();
        idInputPanel.repaint();
    }


    public void submitStudentData() {
        // **แก้ไข: ตรวจสอบ currentStudentCounter < maxStudents และ < students.length**
        if (currentStudentCounter < maxStudents && currentStudentCounter < students.length) {
            JPanel singleStudentPanel = (JPanel) idInputPanel.getComponent(0);
            JTextField txtStudentID_dynamic = (JTextField) singleStudentPanel.getComponent(3);
            JTextField txtStudentName_dynamic = (JTextField) singleStudentPanel.getComponent(5);
            JTextField txtHomeworkScore_dynamic = (JTextField) singleStudentPanel.getComponent(7);
            JTextField txtTestScore_dynamic = (JTextField) singleStudentPanel.getComponent(9);

            String studentID = txtStudentID_dynamic.getText().trim();
            String studentName = txtStudentName_dynamic.getText().trim();
            double homeworkScore = 0;
            double testScore = 0;

            try {
                homeworkScore = Double.parseDouble(txtHomeworkScore_dynamic.getText().trim());
                testScore = Double.parseDouble(txtTestScore_dynamic.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please fill in all information!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (homeworkScore > 30 || testScore > 70) {
                JOptionPane.showMessageDialog(this, "Scores exceed the maximum limit!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (validateStudentID(studentID)) {
                // **แก้ไข: เพิ่ม Student ใหม่ลงใน Array students โดยใช้ Index currentStudentCounter**
                students[currentStudentCounter] = new Student(studentID, studentName, homeworkScore, testScore);
            } else {
                return;
            }


            // **แก้ไข: อัปเดต studentCount ใน Main Class**
            Main.setStudentCount(currentStudentCounter + 1); // อัปเดต studentCount ก่อนเพิ่มค่า
            currentStudentCounter = Main.getStudentCount(); // ดึงค่า studentCount ที่อัปเดตแล้ว

            populateTable();
            JOptionPane.showMessageDialog(this, "Data entry for Student " + (currentStudentCounter) + " successful!", "Success", JOptionPane.INFORMATION_MESSAGE);


            if (currentStudentCounter < maxStudents) {
                studentCounterLabel.setText("Student " + (currentStudentCounter + 1));
                clearIDInputFields();
            } else {
                studentCounterLabel.setText("Completed!");
                clearIDInputFields();
                JOptionPane.showMessageDialog(this, "Data entry completed for all " + maxStudents + " students.", "Information", JOptionPane.INFORMATION_MESSAGE);
                btnAddStudent.setEnabled(false);
            }

        } else {
            JOptionPane.showMessageDialog(this, "You have already entered data for " + maxStudents + " students.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private boolean validateStudentID(String studentID) {
        // **แก้ไข: วนลูปตรวจสอบ Student ID ใน Array students**
        for (int i = 0; i < currentStudentCounter; i++) { // วนลูปเท่ากับ currentStudentCounter
            if (students[i] != null && students[i].getStudentID().equals(studentID)) { // ตรวจสอบ null ก่อนเรียก Method
                JOptionPane.showMessageDialog(this, "Duplicate Student ID: " + studentID, "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }


    private void clearIDInputFields() {
        JPanel singleStudentPanel = (JPanel) idInputPanel.getComponent(0);
        JTextField txtStudentID_dynamic = (JTextField) singleStudentPanel.getComponent(3);
        JTextField txtStudentName_dynamic = (JTextField) singleStudentPanel.getComponent(5);
        JTextField txtHomeworkScore_dynamic = (JTextField) singleStudentPanel.getComponent(7);
        JTextField txtTestScore_dynamic = (JTextField) singleStudentPanel.getComponent(9);

        txtStudentID_dynamic.setText("");
        txtStudentName_dynamic.setText("");
        txtHomeworkScore_dynamic.setText("");
        txtTestScore_dynamic.setText("");
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
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.setFont(new Font("Arial", Font.PLAIN, 24));
        table.setRowHeight(40);
        table.setBackground(new Color(255, 228, 225));
        table.setGridColor(new Color(255, 105, 180));
        table.getTableHeader().setBackground(new Color(255, 105, 180));
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        // **แก้ไข: วนลูป populateTable() โดยใช้ Array students และ studentCount**
        for (int i = 0; i < Main.getStudentCount(); i++) { // วนลูปเท่ากับ studentCount ที่ได้จาก Main
            Student student = students[i];
            if (student != null) { // ตรวจสอบ null ก่อนเรียก Method
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

    private void updateStudentTable() {
        submitStudentData();
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