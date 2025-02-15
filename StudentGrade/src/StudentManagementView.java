import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;


public class StudentManagementView extends JFrame {
    private final ArrayList<Student> students;
    private int maxStudents;
    private final JTable table;
    private final DefaultTableModel tableModel;

    private final JPanel idInputPanel = new JPanel();
    private final ArrayList<JTextField> studentIDFields = new ArrayList<>();

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

        JPanel formPanel = new JPanel();
        formPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // **ตรวจสอบ: ครอบ idInputPanel ด้วย JScrollPane**
        JScrollPane idScrollPane = new JScrollPane(idInputPanel);
        idScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); // ให้ ScrollBar แสดงเมื่อจำเป็น
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

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton btnAddStudent = new RoundedButton("Add Student");
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
        studentIDFields.clear();

        for (int i = 0; i < numStudents; i++) {
            JPanel studentFormPanel = new JPanel(new GridLayout(5, 2, 7, 7));
            studentFormPanel.setBorder(BorderFactory.createTitledBorder("นักเรียน " + (i + 1)));

            JTextField txtStudentID_dynamic = createTextField();
            JTextField txtStudentName_dynamic = createTextField();
            JTextField txtHomeworkScore_dynamic = createTextField();
            JTextField txtTestScore_dynamic = createTextField();

            studentFormPanel.add(createLabel("Student ID:"));
            studentFormPanel.add(txtStudentID_dynamic);

            studentFormPanel.add(createLabel("Student Name:"));
            studentFormPanel.add(txtStudentName_dynamic);

            studentFormPanel.add(createLabel("Homework Score (Max 30%):"));
            studentFormPanel.add(txtHomeworkScore_dynamic);

            studentFormPanel.add(createLabel("Test Score (Max 70%):"));
            studentFormPanel.add(txtTestScore_dynamic);

            idInputPanel.add(studentFormPanel);
            studentIDFields.add(txtStudentID_dynamic);
        }

        idInputPanel.revalidate();
        idInputPanel.repaint();
    }


    public boolean allFieldsFilled() {
        if (studentIDFields.isEmpty()) return false;

        for (JTextField field : studentIDFields) {
            if (field.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void updateSubmitButton() {
    }


    private void submitStudentIDs() {
    }

    public void submitStudentData() {
        ArrayList<Student> newStudents = new ArrayList<>();

        for (int i = 0; i < maxStudents; i++) {
            JPanel studentFormPanel = (JPanel) idInputPanel.getComponent(i);

            JTextField txtStudentID_dynamic = (JTextField) studentFormPanel.getComponent(1);
            JTextField txtStudentName_dynamic = (JTextField) studentFormPanel.getComponent(3);
            JTextField txtHomeworkScore_dynamic = (JTextField) studentFormPanel.getComponent(5);
            JTextField txtTestScore_dynamic = (JTextField) studentFormPanel.getComponent(7);

            String studentID = txtStudentID_dynamic.getText().trim();
            String studentName = txtStudentName_dynamic.getText().trim();
            double homeworkScore = 0;
            double testScore = 0;

            try {
                homeworkScore = Double.parseDouble(txtHomeworkScore_dynamic.getText().trim());
                testScore = Double.parseDouble(txtTestScore_dynamic.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกข้อมูลให้ครบ!", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (homeworkScore > 30 || testScore > 70) {
                JOptionPane.showMessageDialog(this, "คะแนนเกินค่าสูงสุดที่กำหนด!", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (validateStudentID(studentID)) {
                newStudents.add(new Student(studentID, studentName, homeworkScore, testScore));
            } else {
                return;
            }
        }


        students.addAll(newStudents);
        populateTable();
        JOptionPane.showMessageDialog(this, "กรอกข้อมูลสำเร็จ", "สำเร็จ", JOptionPane.INFORMATION_MESSAGE);

        clearIDInputFields();

    }


    private boolean validateStudentID(String studentID) {
        for (Student existingStudent : students) {
            if (existingStudent.getStudentID().equals(studentID)) {
                JOptionPane.showMessageDialog(this, "ไอดีนักเรียนซ้ำกัน: " + studentID, "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }


    private void clearIDInputFields() {
        for (int i = 0; i < maxStudents; i++) {
            JPanel studentFormPanel = (JPanel) idInputPanel.getComponent(i);

            JTextField txtStudentID_dynamic = (JTextField) studentFormPanel.getComponent(1);
            JTextField txtStudentName_dynamic = (JTextField) studentFormPanel.getComponent(3);
            JTextField txtHomeworkScore_dynamic = (JTextField) studentFormPanel.getComponent(5);
            JTextField txtTestScore_dynamic = (JTextField) studentFormPanel.getComponent(7);

            txtStudentID_dynamic.setText("");
            txtStudentName_dynamic.setText("");
            txtHomeworkScore_dynamic.setText("");
            txtTestScore_dynamic.setText("");
        }
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


    private void updateStudent() {
        JOptionPane.showMessageDialog(this, "ฟังก์ชัน Update นักเรียน (ถูกเอาออกแล้ว)", "แจ้งเตือน", JOptionPane.INFORMATION_MESSAGE);
    }


    private void deleteStudent() {
        JOptionPane.showMessageDialog(this, "ฟังก์ชัน Delete นักเรียน (ถูกเอาออกแล้ว)", "แจ้งเตือน", JOptionPane.INFORMATION_MESSAGE);
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