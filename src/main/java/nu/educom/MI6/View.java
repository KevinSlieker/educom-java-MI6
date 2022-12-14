package nu.educom.MI6;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JScrollPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDateTime;

import static java.lang.String.valueOf;

public class View {
    private IPresenter presenter;
    private LoginHandler loginHandler;
    private BackHandler backHandler;
    private JFrame frame;
    private JLabel agentNumberLabel;
    private JLabel sentenceLabel;
    private JLabel errorLabel;
    private JLabel clockLabel;
    private JPanel errorPanel;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JFormattedTextField agentNumberTextField;
    private JFormattedTextField sentenceTextField;
    private JButton loginButton;
    private JButton backButton;
    private JLabel loginLabel;
    private JPanel loginPanel;
    private JLabel licenseLabel;
    private JPanel attemptsPanel;
    private JTable table;

    private DateTimeFormatter dtf   = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    public View() {
        // make frame with the settings
        frame = new JFrame("Login");
    }

    public void setPresenterInterface(IPresenter presenter) {
        this.presenter = presenter;
    }

    public void setLoginHandler() {
        loginHandler = new LoginHandler(presenter);
    }

    public void setBackHandler() {
        backHandler = new BackHandler(presenter);
    }

    public void displayLogin(){
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.revalidate();

        // making labels and panels
        agentNumberLabel = new JLabel("Agent number:", JLabel.CENTER);
        sentenceLabel = new JLabel("Secret sentence:", JLabel.CENTER);
        panel1 = new JPanel();
        panel2 = new JPanel();
//        panel3 = new JPanel();

        // setting up panel1
        BoxLayout boxLayout1 = new BoxLayout(panel1, BoxLayout.Y_AXIS);
        panel1.setLayout(boxLayout1);
        panel1.setBorder(new EmptyBorder(new Insets(45, 75, 45, 75)));

        // setting up panel2
        BoxLayout boxLayout2 = new BoxLayout(panel2, BoxLayout.Y_AXIS);
        panel2.setLayout(boxLayout2);
        panel2.setBorder(new EmptyBorder(new Insets(45, 75, 45, 75)));

        // setting up panel3
//        BoxLayout boxLayout3 = new BoxLayout(panel3, BoxLayout.Y_AXIS);
//        panel3.setLayout(boxLayout3);
//        panel3.setBorder(new EmptyBorder(new Insets(45, 75, 45, 75)));

        // add button and textfield
        agentNumberTextField = new JFormattedTextField();
        sentenceTextField = new JFormattedTextField();
        loginButton = new JButton("Login");
        loginButton.addActionListener(loginHandler);

        clockLabel = new JLabel("");

        // add to panel
        panel1.add(agentNumberLabel);
        panel1.add(agentNumberTextField);
        panel1.add(sentenceLabel);
        panel1.add(sentenceTextField);
        panel2.add(loginButton);

        //add to frame
        frame.setLayout(new GridLayout(2, 1));
        frame.add(panel1);
        frame.add(panel2);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void displayMsg(String errorMsg){
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.revalidate();

        errorLabel = new JLabel(errorMsg, JLabel.CENTER);
        errorPanel = new JPanel();
        panel2 = new JPanel();

        backButton = new JButton("Back");
        backButton.addActionListener(backHandler);

        // configure panel2
        BoxLayout boxlayout2 = new BoxLayout(panel2, BoxLayout.Y_AXIS);
        panel2.setLayout(boxlayout2);
        panel2.setBorder(new EmptyBorder(new Insets(45, 70, 45, 70)));


        // configure errPanel
        BoxLayout boxlayout = new BoxLayout(errorPanel, BoxLayout.Y_AXIS);
        errorPanel.setLayout(boxlayout);
        errorPanel.setBorder(new EmptyBorder(new Insets(45, 75, 45, 75)));

        errorPanel.add(errorLabel);
        panel2.add(backButton);

        frame.setLayout(new GridLayout(2, 1));
        frame.add(errorPanel);
        frame.add(panel2);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public void displayLoginMsg(String Msg, String license_to_kill, List<LoginAttempt> loginAttempts){
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.revalidate();

        loginLabel = new JLabel(Msg, JLabel.CENTER);
        licenseLabel = new JLabel(license_to_kill, JLabel.CENTER);
        loginPanel = new JPanel();
        panel2 = new JPanel();

        backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(backHandler);

        // configure panel2
        BoxLayout boxlayout2 = new BoxLayout(panel2, BoxLayout.Y_AXIS);
        panel2.setLayout(boxlayout2);
        panel2.setBorder(new EmptyBorder(new Insets(5, 70, 5, 70)));


        // configure loginPanel
        BoxLayout boxlayout = new BoxLayout(loginPanel, BoxLayout.Y_AXIS);
        loginPanel.setLayout(boxlayout);
        loginPanel.setBorder(new EmptyBorder(new Insets(5, 70, 5, 70)));


        // set up attempts table
        List<String> columns = new ArrayList<String>();
        List<String[]> values = new ArrayList<String[]>();

        columns.add("Login Attempt Date");
        columns.add("Login Success");

        // format date 23/11/2022
        if (loginAttempts!=null) {
            for (LoginAttempt s : loginAttempts) {
                values.add(new String[]{dtf.format(s.getDate()).toString(), valueOf(s.isLogin_success())});
            }
        }

        values.add(new String[] {dtf.format(LocalDateTime.now()).toString(), "true"});

        TableModel model = new DefaultTableModel(values.toArray(new Object[][]{}), columns.toArray());

        table = new JTable(model);


        loginPanel.add(loginLabel);
        panel2.add(backButton);

        frame.setLayout(new GridLayout(5, 1));
        frame.add(loginPanel);
        frame.add(licenseLabel);
        frame.add(table.getTableHeader());
        frame.add(table);
        frame.add(panel2);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public void displayLoginError(String errorMsg) {
        displayMsg(errorMsg);
    }

    public void displayLoginSuccess(String msg, String license_to_kill, List<LoginAttempt> loginAttempts) {
        displayLoginMsg(msg, license_to_kill, loginAttempts);
    }

    public String getAgentNumber(){
        return agentNumberTextField.getText();
    }

    public String getSentence(){
        return sentenceTextField.getText();
    }

}
