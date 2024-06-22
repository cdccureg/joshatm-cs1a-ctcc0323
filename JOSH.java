import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JOSH extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel welcomeLabel, dateTimeLabel, selectServiceLabel, loginBackground;
    private JButton withdrawButton, depositButton, transferButton, balanceButton, logoutButton, changePinButton;
    private JPanel loginPanel, mainPanel;
    private String currentUser;
    private Map<String, Double> userBalances = new HashMap<>();
    private Map<String, String> userPins = new HashMap<>();

    public JOSH() {
        // Initialize the frame
        setTitle("ATM Application");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Initialize default balances and PINs
        userBalances.put("John", 1000.0);
        userBalances.put("Josh", 2000.0);
        userBalances.put("Curtis", 3000.0);
        userPins.put("John", "123456");
        userPins.put("Josh", "123456");
        userPins.put("Curtis", "123456");

        // Initialize login panel
        loginPanel = new JPanel(null);
        loginPanel.setBounds(0, 0, 700, 500);
        loginBackground = new JLabel();
        loginBackground.setBounds(0,0,700,500);

        // Login components
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(400, 100, 100, 30);
        usernameField = new JTextField();
        usernameField.setBounds(500, 100, 150, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(400, 150, 100, 30);
        passwordField = new JPasswordField();
        passwordField.setBounds(500, 150, 150, 30);

        loginButton = new JButton("Login");
        loginButton.setBounds(400, 200, 100, 30);
        loginButton.addActionListener(this);

        // Add login components to login panel
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        // Initialize main panel
        mainPanel = new JPanel(null);
        mainPanel.setBounds(0, 0, 700, 500);
        mainPanel.setVisible(false);

        // Main frame components
        welcomeLabel = new JLabel();
        welcomeLabel.setBounds(10, 10, 200, 30);

        dateTimeLabel = new JLabel("10:05 AM, 9 AUG");
        dateTimeLabel.setBounds(580, 10, 150, 30);

        selectServiceLabel = new JLabel("PLEASE SELECT A SERVICE");
        selectServiceLabel.setBounds(300, 50, 200, 30);

        withdrawButton = new JButton("WITHDRAW");
        withdrawButton.setBounds(200, 100, 150, 40);
        withdrawButton.addActionListener(this);

        depositButton = new JButton("DEPOSIT");
        depositButton.setBounds(400, 100, 150, 40);
        depositButton.addActionListener(this);

        transferButton = new JButton("TRANSFER");
        transferButton.setBounds(200, 150, 150, 40);
        transferButton.addActionListener(this);

        balanceButton = new JButton("BALANCE ENQUIRY");
        balanceButton.setBounds(400, 150, 150, 40);
        balanceButton.addActionListener(this);

        logoutButton = new JButton("LOGOUT");
        logoutButton.setBounds(10, 400, 150, 40);
        logoutButton.addActionListener(this);

        changePinButton = new JButton("CHANGE PIN");
        changePinButton.setBounds(300, 200, 150, 40);
        changePinButton.addActionListener(this);

        // Add main frame components to main panel
        mainPanel.add(welcomeLabel);
        mainPanel.add(dateTimeLabel);
        mainPanel.add(selectServiceLabel);
        mainPanel.add(withdrawButton);
        mainPanel.add(depositButton);
        mainPanel.add(transferButton);
        mainPanel.add(balanceButton);
        mainPanel.add(logoutButton);
        mainPanel.add(changePinButton);

        // Add panels to the frame
        add(loginPanel);
        add(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (userPins.containsKey(username) && userPins.get(username).equals(password)) {
                currentUser = username;
                welcomeLabel.setText("HI, " + username.toUpperCase());
                loginPanel.setVisible(false);
                mainPanel.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        } else if (e.getSource() == withdrawButton) {
            String amountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
            if (amountStr != null && !amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                int confirm = JOptionPane.showConfirmDialog(this, "Confirm withdrawal of ₱" + amount + "?", "Confirm Withdrawal", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    double balance = userBalances.get(currentUser);
                    if (amount <= balance) {
                        userBalances.put(currentUser, balance - amount);
                        JOptionPane.showMessageDialog(this, "Withdrawal successful! New balance: ₱" + (balance - amount));
                        handleReceipt("Withdrawal of ₱" + amount, currentUser, null, null);
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient balance!");
                    }
                }
            }
        } else if (e.getSource() == depositButton) {
            String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
            if (amountStr != null && !amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                int confirm = JOptionPane.showConfirmDialog(this, "Confirm deposit of ₱" + amount + "?", "Confirm Deposit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    double balance = userBalances.get(currentUser);
                    userBalances.put(currentUser, balance + amount);
                    JOptionPane.showMessageDialog(this, "Deposit successful! New balance: ₱" + (balance + amount));
                    handleReceipt("Deposit of ₱" + amount, currentUser, null, null);
                }
            }
        } else if (e.getSource() == transferButton) {
            String recipient = JOptionPane.showInputDialog(this, "Enter recipient username:");
            if (recipient != null && userBalances.containsKey(recipient)) {
                String amountStr = JOptionPane.showInputDialog(this, "Enter amount to transfer:");
                if (amountStr != null && !amountStr.isEmpty()) {
                    double amount = Double.parseDouble(amountStr);
                    int confirm = JOptionPane.showConfirmDialog(this, "Confirm transfer of ₱" + amount + " to " + recipient + "?", "Confirm Transfer", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        double balance = userBalances.get(currentUser);
                        if (amount <= balance) {
                            userBalances.put(currentUser, balance - amount);
                            userBalances.put(recipient, userBalances.get(recipient) + amount);
                            JOptionPane.showMessageDialog(this, "Transfer successful! New balance: ₱" + (balance - amount));
                            handleReceipt("Transfer of ₱" + amount, currentUser, recipient, null);
                        } else {
                            JOptionPane.showMessageDialog(this, "Insufficient balance!");
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Recipient not found!");
            }
        } else if (e.getSource() == balanceButton) {
            double balance = userBalances.get(currentUser);
            JOptionPane.showMessageDialog(this, "Current balance: ₱" + balance);
            handleReceipt("Balance enquiry: ₱" + balance, currentUser, null, null);
        } else if (e.getSource() == logoutButton) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mainPanel.setVisible(false);
                loginPanel.setVisible(true);
                usernameField.setText("");
                passwordField.setText("");
                currentUser = null;
            }
        } else if (e.getSource() == changePinButton) {
            String newPin = JOptionPane.showInputDialog(this, "Enter new PIN:");
            if (newPin != null && !newPin.isEmpty()) {
                userPins.put(currentUser, newPin);
                JOptionPane.showMessageDialog(this, "PIN changed successfully!");
            }
        }
    }

    private void handleReceipt(String transactionDetails, String fromUser, String toUser, String balance) {
        ReceiptDialog receiptDialog = new ReceiptDialog(this, transactionDetails, fromUser, toUser, balance);
        receiptDialog.setVisible(true);
        mainPanel.setVisible(true);
    }

    public static void main(String[] args) {
        new JOSH();
    }
}

class ReceiptDialog extends JDialog implements ActionListener {
    private String transactionDetails;
    private String fromUser;
    private String toUser;
    private String balance;

    public ReceiptDialog(JFrame parent, String transactionDetails, String fromUser, String toUser, String balance) {
        super(parent, "Receipt", true);
        this.transactionDetails = transactionDetails;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.balance = balance;
        setLayout(new BorderLayout());
        setSize(300, 350);
        setLocationRelativeTo(parent);

        // Create receipt panel with border and background
        JPanel receiptPanel = new JPanel();
        receiptPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setLayout(new BoxLayout(receiptPanel, BoxLayout.Y_AXIS));

        // Create header label
        JLabel headerLabel = new JLabel("Transaction Receipt", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(Color.BLUE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create date and time label
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        JLabel dateTimeLabel = new JLabel("Date & Time: " + dateTime, SwingConstants.CENTER);
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateTimeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create transaction details label
        StringBuilder details = new StringBuilder("<html>" + transactionDetails.replaceAll("\n", "<br>"));
        if (fromUser != null) {
            details.append("<br>From: ").append(fromUser);
        }
        if (toUser != null) {
            details.append("<br>To: ").append(toUser);
        }
        if (balance != null) {
            details.append("<br>Balance: ").append(balance);
        }
        details.append("</html>");
        JLabel detailsLabel = new JLabel(details.toString(), SwingConstants.CENTER);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add labels to the receipt panel
        receiptPanel.add(headerLabel);
        receiptPanel.add(dateTimeLabel);
        receiptPanel.add(detailsLabel);

        // Add receipt panel to the dialog
        add(receiptPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // No action needed for this dialog
    }
}