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

    // **[ส่วนที่แก้ไข] ประกาศ btnAddStudent เป็น Instance Variable**
    private JButton btnAddStudent; // ประกาศตัวแปร btnAddStudent

    // **[ส่วนที่เพิ่มใหม่] ตัวแปรเคาน์เตอร์และ Label**
    private int currentStudentCounter = 0; // ตัวแปรนับจำนวนนักเรียนที่กรอกไปแล้ว
    private JLabel studentCounterLabel; // Label แสดงลำดับนักเรียน

    private static final Color PINK = new Color(255, 20, 147);
    private static final Color BACKGROUND_COLOR = new Color(255, 204, 225);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 42);
    private static final Font TEXT_FIELD_FONT = new Font("Arial", Font.PLAIN, 40);

    public StudentManagementView(ArrayList<Student> students) {
        this.students = students;

        setTitle("Student Management System"); // Title เป็นภาษาอังกฤษ
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

        String[] columns = {"Student ID", "Student Name", "Total Score", "Grade"}; // Column Header เป็นภาษาอังกฤษ
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

        // **[ส่วนที่แก้ไข] Initial ค่า Instance Variable btnAddStudent**
        btnAddStudent = new RoundedButton("Add Student"); // Initial ค่า btnAddStudent ที่เป็น Instance Variable
        JButton btnBack = new RoundedButton("Back"); // Button Text เป็นภาษาอังกฤษ

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

    // **[ส่วนที่แก้ไข] ฟังก์ชัน setupFormPanel ปรับปรุงเป็นฟอร์มกล่องเดียว + แสดงลำดับนักเรียน**
    public void setupFormPanel(int numStudents) {
        this.maxStudents = numStudents;
        idInputPanel.removeAll();
        idInputPanel.setLayout(new BoxLayout(idInputPanel, BoxLayout.Y_AXIS));
        studentIDFields.clear();

        JPanel singleStudentPanel = new JPanel();
        singleStudentPanel.setLayout(new GridLayout(5, 2, 10, 7));

        // **[ส่วนที่เพิ่มใหม่] รีเซ็ตตัวนับเมื่อเริ่ม setupFormPanel ใหม่**
        currentStudentCounter = 0;

        // **[ส่วนที่เพิ่มใหม่] สร้าง Label แสดงลำดับนักเรียน**
        studentCounterLabel = createLabel("Student " + (currentStudentCounter + 1)); // เริ่มต้นที่ Student 1
        singleStudentPanel.add(studentCounterLabel); // เพิ่ม Label ลงใน Panel (ช่องแรก)
        singleStudentPanel.add(new JLabel("")); // ช่องว่าง (เพื่อให้เยื้อง Label)

        JTextField txtStudentID_dynamic = createTextField();
        JTextField txtStudentName_dynamic = createTextField();
        JTextField txtHomeworkScore_dynamic = createTextField();
        JTextField txtTestScore_dynamic = createTextField();

        singleStudentPanel.add(createLabel("Student ID:")); // Label เป็นภาษาอังกฤษ
        singleStudentPanel.add(txtStudentID_dynamic);

        singleStudentPanel.add(createLabel("Student Name:")); // Label เป็นภาษาอังกฤษ
        singleStudentPanel.add(txtStudentName_dynamic);

        singleStudentPanel.add(createLabel("Homework Score (Max 30%):")); // Label เป็นภาษาอังกฤษ
        singleStudentPanel.add(txtHomeworkScore_dynamic);

        singleStudentPanel.add(createLabel("Test Score (Max 70%):")); // Label เป็นภาษาอังกฤษ
        singleStudentPanel.add(txtTestScore_dynamic);

        idInputPanel.add(singleStudentPanel);
        studentIDFields.add(txtStudentID_dynamic);

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



    // **[ส่วนที่แก้ไข] ฟังก์ชัน submitStudentData ปรับปรุงการวนลูป + จำกัดจำนวนกรอก + แจ้งเตือน + แก้ Index**
    public void submitStudentData() {
        ArrayList<Student> newStudents = new ArrayList<>();
        boolean allFieldsFilled = true;

        // **[ส่วนที่เพิ่มใหม่] ตรวจสอบ: ถ้านับจำนวนนักเรียนที่กรอกแล้ว ยังไม่ถึง maxStudents**
        if (currentStudentCounter < maxStudents) {
            JPanel singleStudentPanel = (JPanel) idInputPanel.getComponent(0);
            // **[ส่วนที่แก้ไข] ปรับ Index ให้ถูกต้องตามโครงสร้างใหม่ของ singleStudentPanel**
            JTextField txtStudentID_dynamic = (JTextField) singleStudentPanel.getComponent(3); // Index แก้เป็น 3 (JTextField Student ID)
            JTextField txtStudentName_dynamic = (JTextField) singleStudentPanel.getComponent(5); // Index แก้เป็น 5 (JTextField Student Name)
            JTextField txtHomeworkScore_dynamic = (JTextField) singleStudentPanel.getComponent(7); // Index แก้เป็น 7 (JTextField Homework Score)
            JTextField txtTestScore_dynamic = (JTextField) singleStudentPanel.getComponent(9); // Index แก้เป็น 9 (JTextField Test Score)

            String studentID = txtStudentID_dynamic.getText().trim();
            String studentName = txtStudentName_dynamic.getText().trim();
            double homeworkScore = 0;
            double testScore = 0;

            try {
                homeworkScore = Double.parseDouble(txtHomeworkScore_dynamic.getText().trim());
                testScore = Double.parseDouble(txtTestScore_dynamic.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please fill in all information!", "Error", JOptionPane.ERROR_MESSAGE); // Message Dialog เป็นภาษาอังกฤษ
                return;
            }

            if (homeworkScore > 30 || testScore > 70) {
                JOptionPane.showMessageDialog(this, "Scores exceed the maximum limit!", "Error", JOptionPane.ERROR_MESSAGE); // Message Dialog เป็นภาษาอังกฤษ
                return;
            }


            if (validateStudentID(studentID)) {
                newStudents.add(new Student(studentID, studentName, homeworkScore, testScore));
            } else {
                return;
            }

            if (!allFieldsFilled) {
                JOptionPane.showMessageDialog(this, "Please fill in all student information!", "Error", JOptionPane.ERROR_MESSAGE); // Message Dialog เป็นภาษาอังกฤษ
                return;
            } else {
                // **[ส่วนที่เพิ่มใหม่] เพิ่มข้อมูลนักเรียนสำเร็จ + อัปเดตตัวนับ + อัปเดต Label + เคลียร์ฟิลด์ + แจ้งเตือน**
                students.addAll(newStudents);
                populateTable();
                JOptionPane.showMessageDialog(this, "Data entry for Student " + (currentStudentCounter + 1) + " successful!", "Success", JOptionPane.INFORMATION_MESSAGE); // Message Dialog เป็นภาษาอังกฤษ แสดงเลขนักเรียนที่เพิ่มสำเร็จ

                // **[ส่วนที่เพิ่มใหม่] เพิ่มตัวนับ**
                currentStudentCounter++;

                // **[ส่วนที่เพิ่มใหม่] อัปเดต Label แสดงลำดับนักเรียน (ถ้ายังไม่ครบตามจำนวน)**
                if (currentStudentCounter < maxStudents) {
                    studentCounterLabel.setText("Student " + (currentStudentCounter + 1)); // อัปเดต Label เป็นเลขนักเรียนคนต่อไป
                    clearIDInputFields(); // เคลียร์ฟิลด์เพื่อกรอกคนต่อไป
                } else {
                    // **[ส่วนที่เพิ่มใหม่] ถ้ากรอกครบตามจำนวนแล้ว**
                    studentCounterLabel.setText("Completed!"); // เปลี่ยน Label เป็น "Completed!" หรือข้อความอื่น ๆ
                    clearIDInputFields(); //เคลียร์ข้อมูลล่าสุด
                    JOptionPane.showMessageDialog(this, "Data entry completed for all " + maxStudents + " students.", "Information", JOptionPane.INFORMATION_MESSAGE); // Message Dialog เป็นภาษาอังกฤษ แจ้งว่ากรอกครบแล้ว
                    btnAddStudent.setEnabled(false); // ปิดปุ่ม "Add Student" เมื่อกรอกครบแล้ว (optional)
                }
            }
        } else {
            // **[ส่วนที่เพิ่มใหม่] ถ้ากรอกข้อมูลเกินจำนวนที่กำหนด**
            JOptionPane.showMessageDialog(this, "You have already entered data for " + maxStudents + " students.", "Information", JOptionPane.INFORMATION_MESSAGE); // Message Dialog เป็นภาษาอังกฤษ แจ้งว่ากรอกครบแล้ว
        }
    }


    private boolean validateStudentID(String studentID) {
        for (Student existingStudent : students) {
            if (existingStudent.getStudentID().equals(studentID)) {
                JOptionPane.showMessageDialog(this, "Duplicate Student ID: " + studentID, "Error", JOptionPane.ERROR_MESSAGE); // Message Dialog เป็นภาษาอังกฤษ
                return false;
            }
        }
        return true;
    }

    // **[ส่วนที่แก้ไข] ฟังก์ชัน clearIDInputFields ปรับ Index ให้ถูกต้อง**
    private void clearIDInputFields() {
        JPanel singleStudentPanel = (JPanel) idInputPanel.getComponent(0);
        JTextField txtStudentID_dynamic = (JTextField) singleStudentPanel.getComponent(3); // Index แก้เป็น 3 (JTextField Student ID)
        JTextField txtStudentName_dynamic = (JTextField) singleStudentPanel.getComponent(5); // Index แก้เป็น 5 (JTextField Student Name)
        JTextField txtHomeworkScore_dynamic = (JTextField) singleStudentPanel.getComponent(7); // Index แก้เป็น 7 (JTextField Homework Score)
        JTextField txtTestScore_dynamic = (JTextField) singleStudentPanel.getComponent(9); // Index แก้เป็น 9 (JTextField Test Score)

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