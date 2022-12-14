import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

class Login extends JFrame{
    JLabel lType, l1,l2,l3;
    JTextField t1;
    JPasswordField t2;
    JButton b1;
    String dbURL = "jdbc:oracle:thin:@DESKTOP-7JS83K2:1522:xe";
    String username = "system";
    String password = "admin";
    JComboBox comboLoginType;
    String[] loginType = {"Administrator", "Lekarz", "Pacjent"};
    String userType = "ADMIN";
    Login(){
        Font f=new Font("Arial",Font.BOLD,24);
        l1=new JLabel("Logowanie");
        l1.setFont(f);
        lType=new JLabel("Wybierz rodzaj użytkownika");
        comboLoginType = new JComboBox(loginType);
        l2=new JLabel("Nazwa użytkownika");
        t1=new JTextField();
        l3=new JLabel("Hasło");
        t2=new JPasswordField();
        b1=new JButton("Zaloguj");
        l1.setBounds(180,40,200,40);
        lType.setBounds(150,110,200,20);
        comboLoginType.setBounds(150,130,200,35);
        l2.setBounds(150,220,200,20);//UserName Label
        t1.setBounds(150,240,200,30);//TextField
        l3.setBounds(150,290,200,20);
        t2.setBounds(150,310,200,30);
        b1.setBounds(250,360,100,30);
        add(l1);
        add(lType);
        add(comboLoginType);
        add(l2);
        add(t1);
        add(l3);
        add(t2);
        add(b1);
        setLayout(null);
        setVisible(true);
        setSize(500,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        comboLoginType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==comboLoginType) {
                    if (comboLoginType.getSelectedItem() == "Administrator") {
                        userType = "ADMIN";
                    }
                    else if (comboLoginType.getSelectedItem() == "Lekarz") {
                        userType = "DOCTORS";
                    }
                    else if (comboLoginType.getSelectedItem() == "Pacjent") {
                        userType = "PATIENTS";
                    }
                }
            }});

        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                try {
                    Connection con = DriverManager.getConnection(dbURL, username, password);
                    String sql = "select * from "+userType+" where user_name='"+t1.getText()+"' and password='"+t2.getText()+"'";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        System.out.println("Zalogowano!");
                        setVisible(false);
                        if("ADMIN".equals(userType)){
                            new Admin();
                        }
                        else if ("DOCTORS".equals(userType)) {
                            new Doctor(rs.getInt("DOCTOR_ID"));
                        }
                        else if ("PATIENTS".equals(userType)) {
                            new Patient(rs.getInt("PATIENT_ID"));
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Niepoprawne dane!!");
                    }
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Błąd podczas logowania:");
                    throw new RuntimeException(e);
                }
            }});
    }
}