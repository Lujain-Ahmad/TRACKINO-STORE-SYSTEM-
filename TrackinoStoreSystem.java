package trackinostoresystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

class Product {
    private final String id;
    private final String name;
    private int stock;
    private final double price;

    public Product(String id, String name, int stock, double price) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public double getPrice() { return price; }
    
    public void decreaseStock() { if (stock > 0) stock--; }
    public void increaseStock() { stock++; }
}

public class TrackinoStoreSystem extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private CardLayout storeCardLayout;
    private JPanel storeContentPanel;
    
    
    private ArrayList<Product> inventory = new ArrayList<>();
    private ArrayList<Product> cart = new ArrayList<>();
    private JLabel cartLabel;
    private DefaultTableModel tableModel;
    private JLabel totalSalesLabel;

    public TrackinoStoreSystem() {
        setTitle("TRACKINO STORE SYSTEM - FULL PROTOTYPE");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

  
        inventory.add(new Product("#1001", "Arduino Uno R3", 15, 12.00));
        inventory.add(new Product("#1002", "Soil Moisture Sensor", 45, 3.50));
        inventory.add(new Product("#1003", "Water Pump 5V", 3, 5.00));
        inventory.add(new Product("#1004", "Relay Module", 22, 2.00));
        inventory.add(new Product("#1005", "LCD Display 16x2", 10, 4.50));
        inventory.add(new Product("#1006", "Jumper Wires Pack", 50, 1.50));
        inventory.add(new Product("#1007", "Breadboard", 30, 2.50));
        inventory.add(new Product("#1008", "Power Adapter 12V", 8, 6.00));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPage(), "Login");
        mainPanel.add(createStoreMainPage(), "StoreMain");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(18, 30, 49));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("TRACKINO STORE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(userLabel, gbc);

        JTextField userField = new JTextField("admin", 15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField("1234", 15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(passField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 150, 136));
        loginButton.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if(username.equals("admin") && password.equals("1234")) {
                JOptionPane.showMessageDialog(panel, "Welcome to Trackino Store!");
                cardLayout.show(mainPanel, "StoreMain");
            } else {
                JOptionPane.showMessageDialog(panel, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    private JPanel createStoreMainPage() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(18, 30, 49));
        sidebar.setPreferredSize(new Dimension(220, 800));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel sideTitle = new JLabel("Trackino Menu");
        sideTitle.setFont(new Font("Arial", Font.BOLD, 20));
        sideTitle.setForeground(Color.WHITE);
        sideTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        sidebar.add(sideTitle);

        storeCardLayout = new CardLayout();
        storeContentPanel = new JPanel(storeCardLayout);

        storeContentPanel.add(createStorePage(), "View_Store");
        storeContentPanel.add(createProductsPage(), "View_Products");
        storeContentPanel.add(createDashboardPage(), "View_Dashboard");

        String[] menuItems = {"🛒 Store", "📦 Products", "📊 Dashboard", "🚪 Logout"};
        for (String item : menuItems) {
            JButton menuButton = new JButton(item);
            menuButton.setMaximumSize(new Dimension(200, 40));
            menuButton.setBackground(new Color(28, 43, 65));
            menuButton.setForeground(Color.WHITE);
            menuButton.setFocusPainted(false);
            menuButton.setBorderPainted(false);

            menuButton.addActionListener(e -> {
                if (item.contains("Store")) storeCardLayout.show(storeContentPanel, "View_Store");
                else if (item.contains("Products")) {
                    refreshProductsTable();
                    storeCardLayout.show(storeContentPanel, "View_Products");
                }
                else if (item.contains("Dashboard")) storeCardLayout.show(storeContentPanel, "View_Dashboard");
                else if (item.contains("Logout")) {
                    cart.clear();
                    cartLabel.setText("🛒 Cart: 0 items");
                    cardLayout.show(this.mainPanel, "Login");
                }
            });
            sidebar.add(menuButton);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(980, 60));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel pageTitle = new JLabel("Trackino Management System");
        pageTitle.setFont(new Font("Arial", Font.BOLD, 18));
        topBar.add(pageTitle, BorderLayout.WEST);

        cartLabel = new JLabel("🛒 Cart: 0 items");
        cartLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cartLabel.setForeground(new Color(0, 150, 136));
        topBar.add(cartLabel, BorderLayout.EAST);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(topBar, BorderLayout.NORTH);
        centerContainer.add(storeContentPanel, BorderLayout.CENTER);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(centerContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createStorePage() {
        JPanel productsGrid = new JPanel(new GridLayout(2, 4, 20, 20));
        productsGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        productsGrid.setBackground(new Color(245, 247, 250));


        for (Product prod : inventory) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1));

            JLabel nameLabel = new JLabel(prod.getName() + " ($" + String.format("%.2f", prod.getPrice()) + ")", SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
            card.add(nameLabel, BorderLayout.CENTER);

            JButton buyButton = new JButton("Add to Cart");
            buyButton.setBackground(new Color(0, 150, 136));
            buyButton.setForeground(Color.WHITE);
            
        
            buyButton.addActionListener(e -> {
                if (prod.getStock() > 0) {
                    prod.decreaseStock();
                    cart.add(prod);
                    cartLabel.setText("🛒 Cart: " + cart.size() + " items");
                    JOptionPane.showMessageDialog(this, prod.getName() + " added to cart!");
                } else {
                    JOptionPane.showMessageDialog(this, "Operation Failed: " + prod.getName() + " is completely Out of Stock!", "Inventory Warning", JOptionPane.WARNING_MESSAGE);
                }
            });
            
            card.add(buyButton, BorderLayout.SOUTH);
            productsGrid.add(card);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(new JLabel("   🛒 Store / Browse Items", SwingConstants.LEFT), BorderLayout.NORTH);
        wrapper.add(new JScrollPane(productsGrid), BorderLayout.CENTER);
        
       
        JButton checkoutBtn = new JButton("Complete Purchase & Generate Receipt");
        checkoutBtn.setBackground(new Color(220, 53, 69));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 15));
        

        checkoutBtn.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your shopping cart is completely empty!");
                return;
            }
            double total = cart.stream().mapToDouble(Product::getPrice).sum();
            StringBuilder receipt = new StringBuilder("--- TRACKINO STORE RECEIPT ---\n\n");
            cart.forEach(p -> receipt.append(p.getName()).append(" - $").append(String.format("%.2f", p.getPrice())).append("\n"));
            receipt.append("\n-----------------------------\nTotal Bill Amount: $").append(String.format("%.2f", total));
            
            JOptionPane.showMessageDialog(this, receipt.toString(), "Transaction Invoice", JOptionPane.INFORMATION_MESSAGE);
            cart.clear();
            cartLabel.setText("🛒 Cart: 0 items");
        });
        
        wrapper.add(checkoutBtn, BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel createProductsPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📦 Products Live Stock Control Board");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        String[] columnNames = {"Product ID", "Product Name", "Current Live Stock", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        refreshProductsTable();

        JTable table = new JTable(tableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void refreshProductsTable() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        for (Product p : inventory) {
            String stockStr = p.getStock() == 0 ? "OUT OF STOCK" : "In Stock (" + p.getStock() + ")";
            tableModel.addRow(new Object[]{p.getId(), p.getName(), stockStr, "$" + String.format("%.2f", p.getPrice())});
        }
    }

    private JPanel createDashboardPage() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 20));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        JPanel card1 = new JPanel(new GridBagLayout());
        card1.setBackground(Color.WHITE);
        card1.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 136), 2));
        totalSalesLabel = new JLabel("💰 Store Status: Active Logs");
        totalSalesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        card1.add(totalSalesLabel);

        JPanel card2 = new JPanel(new GridBagLayout());
        card2.setBackground(Color.WHITE);
        card2.setBorder(BorderFactory.createLineBorder(new Color(18, 30, 49), 2));
        JLabel lbl2 = new JLabel("📦 Items: 8 Active Hardware Parts");
        lbl2.setFont(new Font("Arial", Font.BOLD, 14));
        card2.add(lbl2);

        JPanel card3 = new JPanel(new GridBagLayout());
        card3.setBackground(Color.WHITE);
        card3.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        JLabel lbl3 = new JLabel("⚙️ System Status: Fully Functional");
        lbl3.setFont(new Font("Arial", Font.BOLD, 14));
        card3.add(lbl3);

        panel.add(card1);
        panel.add(card2);
        panel.add(card3);

        JPanel mainDash = new JPanel(new BorderLayout());
        mainDash.add(new JLabel("   📊 Trackino Analytics Dashboard", SwingConstants.LEFT), BorderLayout.NORTH);
        mainDash.add(panel, BorderLayout.CENTER);

        return mainDash;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TrackinoStoreSystem().setVisible(true);
        });
    }
}