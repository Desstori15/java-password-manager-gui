import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class Main extends JFrame {
    private static String masterPassword; // Переменная для хранения мастер-пароля
    private static String selectedFilePath; // Имя файла для хранения паролей

    private JTextField nameField, categoryField, loginField, websiteField;
    private JPasswordField passwordField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblLastDecryptionTime; // Метка для отображения времени последней расшифровки

    // Основной метод запуска программы
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Main frame = new Main();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Конструктор класса PasswordManagerGUI
    public Main() {
        // Настройка главного окна
        setTitle("Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 650);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        Font font = new Font("Arial", Font.PLAIN, 14);
        Color textColor = Color.CYAN;
        Color inputTextColor = Color.PINK;

        // Запрос мастер-пароля
        JPanel masterPasswordPanel = new JPanel();
        masterPasswordPanel.setBackground(Color.BLACK);
        masterPasswordPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel lblMasterPassword = new JLabel("Enter master password: ");
        lblMasterPassword.setForeground(textColor);
        lblMasterPassword.setFont(font);
        masterPasswordPanel.add(lblMasterPassword);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setForeground(inputTextColor);
        passwordField.setBackground(Color.DARK_GRAY);
        passwordField.setFont(font);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        masterPasswordPanel.add(passwordField);

        JButton btnEnter = new JButton("Enter");
        btnEnter.setForeground(textColor);
        btnEnter.setBackground(Color.DARK_GRAY);
        btnEnter.setFont(font);
        masterPasswordPanel.add(btnEnter);

        lblLastDecryptionTime = new JLabel();
        lblLastDecryptionTime.setForeground(textColor);
        lblLastDecryptionTime.setFont(font);
        lblLastDecryptionTime.setHorizontalAlignment(SwingConstants.LEFT);
        masterPasswordPanel.add(lblLastDecryptionTime);

        getContentPane().add(masterPasswordPanel, BorderLayout.NORTH);

        JPasswordField finalPasswordField = passwordField;
        btnEnter.addActionListener(e -> {
            masterPassword = new String(finalPasswordField.getPassword());

            // Диалоговое окно для выбора файла или создания нового
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select or Create Password File");
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFilePath = fileChooser.getSelectedFile().getPath();
                if (Files.exists(Paths.get(selectedFilePath))) {
                    PasswordManager.readFromFile(selectedFilePath, masterPassword);
                } else {
                    PasswordManager.createNewFile(selectedFilePath);
                    JOptionPane.showMessageDialog(this, "New file created: " + selectedFilePath);
                }
                updateLastDecryptionTimeLabel(); // Обновление метки времени последней расшифровки
                updateTable();
            } else {
                JOptionPane.showMessageDialog(this, "No file selected. Exiting...");
                System.exit(0);
            }
        });

        // Создание таблицы для отображения паролей
        tableModel = new DefaultTableModel(new Object[]{"Name", "Password", "Category", "Login", "Website"}, 0);
        table = new JTable(tableModel);
        table.setForeground(textColor);
        table.setBackground(Color.DARK_GRAY);
        table.setFont(font);
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Создание панели для ввода данных пароля и кнопок
        JPanel inputAndButtonPanel = new JPanel();
        inputAndButtonPanel.setBackground(Color.BLACK);
        inputAndButtonPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnAdd = new JButton("Add Password");
        btnAdd.setForeground(textColor);
        btnAdd.setBackground(Color.DARK_GRAY);
        btnAdd.setFont(font);
        btnAdd.addActionListener(e -> addPassword());
        buttonPanel.add(btnAdd);

        JButton btnDelete = new JButton("Delete Password");
        btnDelete.setForeground(textColor);
        btnDelete.setBackground(Color.DARK_GRAY);
        btnDelete.setFont(font);
        btnDelete.addActionListener(e -> deletePassword());
        buttonPanel.add(btnDelete);

        JButton btnSave = new JButton("Save to File");
        btnSave.setForeground(textColor);
        btnSave.setBackground(Color.DARK_GRAY);
        btnSave.setFont(font);
        btnSave.addActionListener(e -> PasswordManager.saveToFile(selectedFilePath, masterPassword));
        buttonPanel.add(btnSave);

        JButton btnGenerate = new JButton("Generate Password");
        btnGenerate.setForeground(textColor);
        btnGenerate.setBackground(Color.DARK_GRAY);
        btnGenerate.setFont(font);
        btnGenerate.addActionListener(e -> generatePassword());
        buttonPanel.add(btnGenerate);

        JButton btnSearch = new JButton("Search by Category");
        btnSearch.setForeground(textColor);
        btnSearch.setBackground(Color.DARK_GRAY);
        btnSearch.setFont(font);
        btnSearch.addActionListener(e -> searchByCategory());
        buttonPanel.add(btnSearch);

        JButton btnSort = new JButton("Sort Passwords");
        btnSort.setForeground(textColor);
        btnSort.setBackground(Color.DARK_GRAY);
        btnSort.setFont(font);
        btnSort.addActionListener(e -> sortPasswords());
        buttonPanel.add(btnSort);

        inputAndButtonPanel.add(buttonPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.BLACK);
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(textColor);
        lblName.setFont(font);
        inputPanel.add(lblName);
        nameField = new JTextField();
        nameField.setForeground(inputTextColor);
        nameField.setBackground(Color.DARK_GRAY);
        nameField.setFont(font);
        nameField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        inputPanel.add(nameField);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(textColor);
        lblPassword.setFont(font);
        inputPanel.add(lblPassword);
        this.passwordField = new JPasswordField();
        this.passwordField.setForeground(inputTextColor);
        this.passwordField.setBackground(Color.DARK_GRAY);
        this.passwordField.setFont(font);
        this.passwordField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        inputPanel.add(this.passwordField);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setForeground(textColor);
        lblCategory.setFont(font);
        inputPanel.add(lblCategory);
        categoryField = new JTextField();
        categoryField.setForeground(inputTextColor);
        categoryField.setBackground(Color.DARK_GRAY);
        categoryField.setFont(font);
        categoryField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        inputPanel.add(categoryField);

        JLabel lblLogin = new JLabel("Login  (optional):");
        lblLogin.setForeground(textColor);
        lblLogin.setFont(font);
        inputPanel.add(lblLogin);
        loginField = new JTextField();
        loginField.setForeground(inputTextColor);
        loginField.setBackground(Color.DARK_GRAY);
        loginField.setFont(font);
        loginField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        inputPanel.add(loginField);

        JLabel lblWebsite = new JLabel("Website (optional):");
        lblWebsite.setForeground(textColor);
        lblWebsite.setFont(font);
        inputPanel.add(lblWebsite);
        websiteField = new JTextField();
        websiteField.setForeground(inputTextColor);
        websiteField.setBackground(Color.DARK_GRAY);
        websiteField.setFont(font);
        websiteField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        inputPanel.add(websiteField);

        inputAndButtonPanel.add(inputPanel, BorderLayout.CENTER);

        getContentPane().add(inputAndButtonPanel, BorderLayout.SOUTH);

        updateLastDecryptionTimeLabel(); // Обновление метки времени последней расшифровки при инициализации
    }

    // Обновление метки времени последней расшифровки
    private void updateLastDecryptionTimeLabel() {
        LocalDateTime lastDecryptionTime = PasswordManager.getLastDecryptionTime();
        if (lastDecryptionTime != null) {
            lblLastDecryptionTime.setText("Last decryption time: " + lastDecryptionTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            lblLastDecryptionTime.setText("No decryption time available.");
        }
    }

    // Метод для добавления нового пароля
    private void addPassword() {
        String name = nameField.getText();
        String password = new String(passwordField.getPassword());
        String category = categoryField.getText();
        String login = loginField.getText();
        String website = websiteField.getText();

        PasswordEntry entry = new PasswordEntry(name, password, category, login, website);
        PasswordManager.addEntry(entry);
        tableModel.addRow(new Object[]{name, password, category, login, website});
        clearFields();
        JOptionPane.showMessageDialog(this, "Password added successfully.");
    }

    // Метод для удаления выбранного пароля
    private void deletePassword() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
            PasswordManager.deleteEntry(selectedRow);
            JOptionPane.showMessageDialog(this, "Password deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a password to delete.");
        }
    }

    // Метод для генерации случайного пароля
    private void generatePassword() {
        int length = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter password length:"));
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        JOptionPane.showMessageDialog(this, "Generated password: " + sb.toString());
    }

    // Метод для поиска паролей по категории
    private void searchByCategory() {
        String category = JOptionPane.showInputDialog(this, "Enter category to search:");
        StringBuilder result = new StringBuilder();

        for (PasswordEntry entry : PasswordManager.getEntries()) {
            if (entry.getCategory().equals(category)) {
                result.append(entry).append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, result.toString());
    }

    // Метод для сортировки паролей по имени или категории
    private void sortPasswords() {
        String[] options = {"Name", "Category"};
        String choice = (String) JOptionPane.showInputDialog(this, "Sort by:", "Sort Passwords", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        Comparator<PasswordEntry> comparator;
        if (choice.equals("Name")) {
            comparator = Comparator.comparing(PasswordEntry::getName);
        } else {
            comparator = Comparator.comparing(PasswordEntry::getCategory);
        }

        List<PasswordEntry> entries = PasswordManager.getEntries();
        entries.sort(comparator);
        tableModel.setRowCount(0);
        for (PasswordEntry entry : entries) {
            tableModel.addRow(new Object[]{entry.getName(), entry.getPassword(), entry.getCategory(), entry.getLogin(), entry.getWebsite()});
        }
    }

    // Метод для очистки полей ввода
    private void clearFields() {
        nameField.setText("");
        passwordField.setText("");
        categoryField.setText("");
        loginField.setText("");
        websiteField.setText("");
    }

    // Метод для обновления таблицы паролей
    private void updateTable() {
        List<PasswordEntry> entries = PasswordManager.getEntries();
        tableModel.setRowCount(0);
        for (PasswordEntry entry : entries) {
            tableModel.addRow(new Object[]{entry.getName(), entry.getPassword(), entry.getCategory(), entry.getLogin(), entry.getWebsite()});
        }
    }
}
