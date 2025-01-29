import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GradeCalculatorView extends JFrame {
    public GradeCalculatorView() {
        setTitle("Grade Average Calculator");  // กำหนดชื่อหน้าต่าง
        setSize(1920, 1080);  // ขนาดหน้าต่าง
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // เมื่อปิดหน้าต่างจะปิดแอปพลิเคชัน
        setLayout(new BorderLayout(10, 10));  // กำหนด Layout ของหน้าต่าง
        getContentPane().setBackground(new Color(255, 204, 225));  // กำหนดสีพื้นหลัง

        // การตั้งค่า Panel กลาง (ใช้ GridLayout)
        JPanel centerPanel = new JPanel(new GridLayout(7, 2, 7, 7));
        centerPanel.setBorder(new EmptyBorder(50, 50, 50, 50));  // ขอบของ Panel
        centerPanel.setBackground(new Color(255, 204, 225));  // สีพื้นหลังของ Panel กลาง

        // สร้าง Label และ TextField สำหรับกรอกข้อมูล (Prelim, Midterm, Final, Average, Status)
        JLabel lblPrelim = createLabel("Prelim:");
        JTextField txtPrelim = createTextField();

        JLabel lblMidterm = createLabel("Midterm:");
        JTextField txtMidterm = createTextField();

        JLabel lblFinal = createLabel("Final:");
        JTextField txtFinal = createTextField();

        JLabel lblAverage = createLabel("Average:");
        JTextField txtAverage = createTextField();
        txtAverage.setEditable(false);  // ปิดการแก้ไขค่าใน TextField สำหรับ Average

        JLabel lblStatus = createLabel("Status:");
        JTextField txtStatus = createTextField();
        txtStatus.setEditable(false);  // ปิดการแก้ไขค่าใน TextField สำหรับ Status

        // เพิ่ม Label และ TextField ลงใน centerPanel
        centerPanel.add(lblPrelim);
        centerPanel.add(txtPrelim);
        centerPanel.add(lblMidterm);
        centerPanel.add(txtMidterm);
        centerPanel.add(lblFinal);
        centerPanel.add(txtFinal);
        centerPanel.add(lblAverage);
        centerPanel.add(txtAverage);
        centerPanel.add(lblStatus);
        centerPanel.add(txtStatus);

        // สร้างปุ่ม Calculate (ใช้ RoundedButton)
        RoundedButton btnCalculate = new RoundedButton("Calculate");
        btnCalculate.addActionListener(e -> {
            try {
                // รับค่าจาก TextField
                double prelim = Double.parseDouble(txtPrelim.getText());
                double midterm = Double.parseDouble(txtMidterm.getText());
                double finalExam = Double.parseDouble(txtFinal.getText());

                // ตรวจสอบค่าที่กรอกว่าอยู่ระหว่าง 0 ถึง 100 หรือไม่
                if (prelim < 0 || prelim > 100 || midterm < 0 || midterm > 100 || finalExam < 0 || finalExam > 100) {
                    JOptionPane.showMessageDialog(this, "Grades must be between 0 and 100!", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // คำนวณค่าเฉลี่ย
                double average = (prelim + midterm + finalExam) / 3;
                txtAverage.setText(String.format("%.2f", average));

                // แสดงผลสถานะ (ผ่าน หรือ ไม่ผ่าน)
                String status = average >= 60 ? "Passed" : "Failed";
                txtStatus.setText(status);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // สร้างปุ่ม Back (ใช้ RoundedButton)
        RoundedButton btnBack = new RoundedButton("Back");
        btnBack.setFont(new Font("Arial", Font.BOLD, 48));
        btnBack.setForeground(new Color(255, 255, 255));
        btnBack.setBackground(new Color(255, 20, 147));
        btnBack.addActionListener(e -> {
            dispose();  // ปิดหน้าต่างปัจจุบัน
            Main.main(null);  // กลับไปยังหน้าหลัก
        });

        // Bottom Panel สำหรับปุ่ม
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(255, 204, 225));  // สีพื้นหลังของ Bottom Panel
        bottomPanel.add(btnCalculate);
        bottomPanel.add(btnBack);

        // เพิ่ม Components ลงใน JFrame
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);  // แสดงหน้าต่าง
    }

    // ฟังก์ชันสร้าง Label
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(new Color(255, 20, 147));  // กำหนดสีของข้อความ
        return label;
    }

    // ฟังก์ชันสร้าง TextField
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 24));
        textField.setForeground(new Color(255, 20, 147));  // กำหนดสีของข้อความ
        textField.setBorder(BorderFactory.createLineBorder(new Color(255, 20, 147), 2));  // ขอบสีชมพู
        return textField;
    }

    // คลาส RoundedButton (ปุ่มที่มีมุมโค้ง)
    static class RoundedButton extends JButton {
        private static final int RADIUS = 40;  // กำหนดความโค้งของปุ่ม

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);  // ปิดการเติมพื้นหลังของปุ่ม
            setFocusPainted(false);  // ปิดการแสดงกรอบเมื่อเลือก
            setForeground(Color.WHITE);  // กำหนดสีข้อความเป็นขาว
            setFont(new Font("Arial", Font.BOLD, 48));  // กำหนดฟอนต์และขนาด
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));  // กำหนดระยะห่างภายในปุ่ม
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(255, 20, 147));  // สีชมพูเข้ม
            g2.fillRoundRect(2, 2, getWidth() - 8, getHeight() - 8, RADIUS, RADIUS);  // วาดปุ่มที่มีมุมโค้ง

            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);  // สีของกรอบเป็นขาว
            g2.setStroke(new BasicStroke(8));  // กำหนดความหนาของกรอบ
            g2.drawRoundRect(4, 4, getWidth() - 8, getHeight() - 8, RADIUS, RADIUS);  // วาดกรอบที่มีมุมโค้ง

            g2.dispose();
        }
    }
}