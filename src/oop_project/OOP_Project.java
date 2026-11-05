package oop_project;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class OOP_Project extends JFrame {
    static final String PROOF_DIR = "payment_proofs";
    static Store store = initializeStore();

    Userrr currentUser;
    CardLayout cards;
    JPanel root;

    final Color COLOR_BG_DARK = new Color(15, 23, 42);
    final Color COLOR_BG_LIGHT = new Color(241, 245, 249);
    final Color COLOR_CARD = Color.WHITE;
    final Color COLOR_PRIMARY = new Color(37, 99, 235);
    final Color COLOR_TEXT_MAIN = new Color(15, 23, 42);
    final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);
    final Color COLOR_BORDER = new Color(226, 232, 240);
    final Color COLOR_SUCCESS = new Color(16, 185, 129);
    final Color COLOR_DANGER = new Color(239, 68, 68);

    public OOP_Project() {
        setTitle("Hoop Shot - Basketball Court Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 750));
        setLocationRelativeTo(null);
        showSplashAndLogin();
    }

    static Store initializeStore() {
        Store freshStore = new Store();
        freshStore.getUsers().add(new Adminn("System Administrator", "admin", "-", "admin123"));
        freshStore.getUsers().add(new Customerrr("Renz Nabarte", "renz", "09243920185", "123"));
        return freshStore;
    }

    void showSplashAndLogin() {
        cards = new CardLayout();
        root = new JPanel(cards);
        root.add(buildSplashScreenPanel(), "splash");
        root.add(buildLoginPanel(), "login");
        root.add(buildSignupPanel(), "signup");
        setContentPane(root);
        cards.show(root, "splash");
        revalidate();
        repaint();

        javax.swing.Timer timer = new javax.swing.Timer(2500, e -> cards.show(root, "login"));
        timer.setRepeats(false);
        timer.start();
    }

    JPanel buildSplashScreenPanel() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(COLOR_BG_DARK);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(COLOR_BG_DARK);
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoLabel = new JLabel("HOOP SHOT", SwingConstants.CENTER);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("Court Reservation Management System", SwingConstants.CENTER);
        subLabel.setForeground(new Color(148, 163, 184));
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(320, 6));
        progressBar.setMaximumSize(new Dimension(320, 6));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(logoLabel);
        box.add(Box.createVerticalStrut(10));
        box.add(subLabel);
        box.add(Box.createVerticalStrut(40));
        box.add(progressBar);

        page.add(box);
        return page;
    }

    JPanel buildLoginPanel() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(COLOR_BG_DARK);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(COLOR_CARD);
        box.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(45, 50, 45, 50)
        ));
        box.setPreferredSize(new Dimension(500, 500));

        JLabel title = heading("WELCOME BACK", 26);
        JLabel subtitle = new JLabel("Sign in to manage your court reservations");
        subtitle.setForeground(COLOR_TEXT_MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        styleField(username, "Username");
        styleField(password, "Password");

        JButton login = primaryButton("LOG IN");

        JButton signup = new JButton("Don't have an account? Sign up");
        signup.setAlignmentX(Component.CENTER_ALIGNMENT);
        signup.setBorderPainted(false);
        signup.setContentAreaFilled(false);
        signup.setForeground(COLOR_PRIMARY);
        signup.setFont(new Font("SansSerif", Font.BOLD, 14));
        signup.setCursor(new Cursor(Cursor.HAND_CURSOR));

        box.add(title);
        box.add(Box.createVerticalStrut(6));
        box.add(subtitle);
        box.add(Box.createVerticalStrut(30));
        
        addAlignedField(box, "Username", username);
        box.add(Box.createVerticalStrut(15));
        addAlignedField(box, "Password", password);
        box.add(Box.createVerticalStrut(30));
        
        JPanel loginBtnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        loginBtnRow.setBackground(COLOR_CARD);
        loginBtnRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtnRow.add(login);
        box.add(loginBtnRow);
        
        box.add(Box.createVerticalStrut(15));
        box.add(signup);

        login.addActionListener(e -> {
            String u = username.getText().trim();
            String p = new String(password.getPassword());
            
            Userrr targetUser = null;
            for (Userrr userObj : store.getUsers()) {
                if (userObj != null && userObj.getUsername() != null && userObj.getUsername().equalsIgnoreCase(u)) {
                    targetUser = userObj;
                    break;
                }
            }
            if (targetUser != null && targetUser.isBlocked()) {
                JOptionPane.showMessageDialog(this, "Your account is blocked due to 3 failed login attempts. Please contact the administrator.", "Account Blocked", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Userrr found = AuthenticationManagerr.login(store, u, p);
            if (found == null) {
                if (targetUser != null && targetUser.isBlocked()) {
                    JOptionPane.showMessageDialog(this, "Your account has now been blocked due to 3 failed login attempts.", "Account Blocked", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            currentUser = found;
            if (found.isAdmin()) showAdminDashboard();
            else showUserDashboard();
        });

        signup.addActionListener(e -> cards.show(root, "signup"));

        page.add(box);
        return page;
    }
    
    JPanel buildSignupPanel() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(COLOR_BG_DARK);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(COLOR_CARD);
        box.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(35, 50, 35, 50)
        ));
        box.setPreferredSize(new Dimension(500, 620));

        JLabel title = heading("CREATE ACCOUNT", 26);
        JLabel subtitle = new JLabel("Fill in your details to register");
        subtitle.setForeground(COLOR_TEXT_MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField name = new JTextField();
        JTextField username = new JTextField();
        JTextField contact = new JTextField();
        JPasswordField password = new JPasswordField();
        
        styleField(name, "Full name");
        styleField(username, "Username");
        styleField(contact, "Contact number");
        styleField(password, "Password");

        JButton create = primaryButton("SIGN UP NOW");

        JButton back = new JButton("Already have an account? Log in");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setForeground(COLOR_PRIMARY);
        back.setFont(new Font("SansSerif", Font.BOLD, 14));
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));

        box.add(title);
        box.add(Box.createVerticalStrut(6));
        box.add(subtitle);
        box.add(Box.createVerticalStrut(20));
        
        addAlignedField(box, "Full Name", name);
        box.add(Box.createVerticalStrut(10));
        addAlignedField(box, "Username", username);
        box.add(Box.createVerticalStrut(10));
        addAlignedField(box, "Contact No.", contact);
        box.add(Box.createVerticalStrut(10));
        addAlignedField(box, "Password", password);
        
        box.add(Box.createVerticalStrut(20));
        
        JPanel createBtnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        createBtnRow.setBackground(COLOR_CARD);
        createBtnRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        createBtnRow.add(create);
        box.add(createBtnRow);
        
        box.add(Box.createVerticalStrut(12));
        box.add(back);

        create.addActionListener(e -> {
            String n = name.getText().trim();
            String u = username.getText().trim();
            String c = contact.getText().trim();
            String p = new String(password.getPassword());
            if (n.isEmpty() || u.isEmpty() || c.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please complete all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean taken = false;
            for (Userrr userObj : store.getUsers()) {
                if (userObj != null && userObj.getUsername() != null && userObj.getUsername().equalsIgnoreCase(u)) {
                    taken = true;
                    break;
                }
            }

            if (u.equalsIgnoreCase("admin") || taken) {
                JOptionPane.showMessageDialog(this, "That username is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            store.getUsers().add(new Customerrr(n, u, c, p));
            JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.");
            cards.show(root, "login");
        });

        back.addActionListener(e -> cards.show(root, "login"));

        page.add(box);
        return page;
    }

    void showUserDashboard() {
        JPanel page = new JPanel(new BorderLayout(20, 20));
        page.setBorder(new EmptyBorder(25, 30, 25, 30));
        page.setBackground(COLOR_BG_LIGHT);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(COLOR_PRIMARY);
        top.setOpaque(true);
        top.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("HOOP SHOT");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel welcome = new JLabel("Welcome, " + currentUser.getFullName() + "    ");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 14));
        welcome.setForeground(Color.WHITE);

        JButton logout = styledActionButton("Log out", COLOR_DANGER);
        logout.setPreferredSize(new Dimension(100, 34));
        logout.setMaximumSize(new Dimension(100, 34));
        logout.addActionListener(e -> { currentUser = null; showSplashAndLogin(); });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(welcome);
        right.add(logout);
        
        top.add(title, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        UIManager.put("TabbedPane.selected", COLOR_PRIMARY);
        UIManager.put("TabbedPane.background", new Color(226, 232, 240));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 15));
        tabs.addTab("Make a Reservation", buildBookingPanel());
        tabs.addTab("My Reservations", buildMyReservationsPanel());

        page.add(top, BorderLayout.NORTH);
        page.add(tabs, BorderLayout.CENTER);
        setContentPane(page);
        revalidate();
        repaint();
    }

    JPanel buildBookingPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(COLOR_BG_LIGHT);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(COLOR_CARD);
        form.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));
        form.setPreferredSize(new Dimension(620, 640));

        JLabel title = heading("Book a Court Session", 22);
        JLabel note = new JLabel("Scan GCash QR Code to pay, then upload your payment screenshot.");
        note.setForeground(COLOR_TEXT_MUTED);
        note.setFont(new Font("SansSerif", Font.PLAIN, 13));
        note.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        styleField(dateField, "Date (yyyy-MM-dd)");

        JComboBox<String> timeBox = new JComboBox<>(getTimeSlots());
        styleField(timeBox, "Time Slot");

        timeBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String slot = (String) value;
                String date = dateField.getText().trim();
                if (slot != null && !isSlotAvailable(date, slot)) {
                    setText(slot + " (Unavailable)");
                    setForeground(Color.GRAY);
                } else {
                    setForeground(COLOR_TEXT_MAIN);
                }
                return c;
            }
        });

        JTextField hours = new JTextField("1");
        styleField(hours, "Duration in hours");

        JLabel rate = new JLabel("Rate: ₱500.00 per hour  |  GCash Payment");
        rate.setFont(new Font("SansSerif", Font.BOLD, 14));
        rate.setForeground(COLOR_TEXT_MAIN);
        rate.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField proofPath = new JTextField();
        proofPath.setEditable(false);
        styleField(proofPath, "No proof selected");

        JButton viewQR = styledActionButton("PAY VIA QR ", new Color(13, 148, 136));
        JButton chooseProof = styledActionButton("UPLOAD PROOF OF PAYMENT", COLOR_PRIMARY);
        JButton reserve = primaryButton("SUBMIT RESERVATION");

        viewQR.addActionListener(e -> {
            JPanel qrPanel = new JPanel();
            qrPanel.setLayout(new BoxLayout(qrPanel, BoxLayout.Y_AXIS));
            qrPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
            qrPanel.setBackground(COLOR_CARD);
            
            JLabel qrLabel = new JLabel("GCASH PAYMENT QR CODE");
            qrLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            qrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel instruction = new JLabel("Scan via GCash App and save screenshot of receipt.");
            instruction.setFont(new Font("SansSerif", Font.PLAIN, 12));
            instruction.setForeground(COLOR_TEXT_MUTED);
            instruction.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel imageLabel = new JLabel();
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            try {
                String imgPath = "C:\\Users\\rennz\\Downloads\\qrcode.jpg";
                ImageIcon originalIcon = new ImageIcon(imgPath);
                Image img = originalIcon.getImage();
                Image scaledImg = img.getScaledInstance(240, 360, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
            } catch (Exception ex) {
                imageLabel.setText("[QR Code Image not found in path]");
            }

            qrPanel.add(qrLabel);
            qrPanel.add(Box.createVerticalStrut(5));
            qrPanel.add(instruction);
            qrPanel.add(Box.createVerticalStrut(10));
            qrPanel.add(imageLabel);
            
            JOptionPane.showMessageDialog(this, qrPanel, "GCash Payment", JOptionPane.PLAIN_MESSAGE);
        });

        final File[] selectedProof = {null};
        chooseProof.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select GCash Payment Proof");
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedProof[0] = chooser.getSelectedFile();
                proofPath.setText(selectedProof[0].getAbsolutePath());
            }
        });

        reserve.addActionListener(e -> {
            String date = dateField.getText().trim();
            String time = (String) timeBox.getSelectedItem();
            int duration;
            try {
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) throw new Exception();
                duration = Integer.parseInt(hours.getText().trim());
                if (duration < 1 || duration > 4) throw new Exception();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid date (yyyy-MM-dd) and duration (1 to 4 hours).", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (time == null || !isSlotAvailable(date, time)) {
                JOptionPane.showMessageDialog(this, "The selected slot is unavailable or already booked.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (selectedProof[0] == null || !selectedProof[0].isFile()) {
                JOptionPane.showMessageDialog(this, "Please upload your GCash payment proof screenshot.", "Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isRangeAvailable(date, time, duration)) {
                JOptionPane.showMessageDialog(this, "Selected time duration overlaps with an existing reservation.", "Conflict", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                File dir = new File(PROOF_DIR);
                if (!dir.exists()) dir.mkdirs();
                
                String ext = getExtension(selectedProof[0].getName());
                String proofName = "proof_" + System.currentTimeMillis() + "." + ext;
                File destination = new File(dir, proofName);
                
                try (InputStream in = new FileInputStream(selectedProof[0]);
                     OutputStream out = new FileOutputStream(destination)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }

                String formattedId = String.format("RSV%03d", store.getNextReservationId());

                Reservation r = new Reservation(
                    formattedId,
                    currentUser.getUsername(),
                    currentUser.getFullName(),
                    currentUser.getContact(),
                    date,
                    time,
                    duration,
                    duration * 500,
                    destination.getAbsolutePath(),
                    "Pending approval"
                );
                
                store.setNextReservationId(store.getNextReservationId() + 1);
                store.getReservations().add(r);

                JOptionPane.showMessageDialog(this, "Reservation submitted successfully!\nWaiting for admin approval.");
                showUserDashboard();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving payment proof: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        form.add(title);
        form.add(Box.createVerticalStrut(4));
        form.add(note);
        form.add(Box.createVerticalStrut(14));
        
        addAlignedField(form, "Reservation Date (yyyy-MM-dd)", dateField);
        form.add(Box.createVerticalStrut(10));
        addAlignedField(form, "Time Slot", timeBox);
        form.add(Box.createVerticalStrut(8));
        addAlignedField(form, "Duration (Hours)", hours);
        form.add(Box.createVerticalStrut(10));
        form.add(rate);
        form.add(Box.createVerticalStrut(10));
        
        JPanel qrRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        qrRow.setBackground(COLOR_CARD);
        qrRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        qrRow.add(viewQR);
        form.add(qrRow);
        
        form.add(Box.createVerticalStrut(8));
        addAlignedField(form, "Payment Proof", proofPath);
        form.add(Box.createVerticalStrut(8));
        
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnRow.setBackground(COLOR_CARD);
        btnRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRow.add(chooseProof);
        form.add(btnRow);
        
        form.add(Box.createVerticalStrut(12));
        JPanel reserveBtnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        reserveBtnRow.setBackground(COLOR_CARD);
        reserveBtnRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        reserveBtnRow.add(reserve);
        form.add(reserveBtnRow);

        outer.add(form);
        return outer;
    }

    JPanel buildMyReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(COLOR_BG_LIGHT);

        String[] columns = {"ID", "Date", "Time", "Hours", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (Reservation r : store.getReservations()) {
            if (r != null && r.getUsername() != null && currentUser != null && r.getUsername().equals(currentUser.getUsername())) {
                model.addRow(new Object[]{r.getId(), r.getDate(), r.getTime(), r.getDuration(), peso(r.getAmount()), r.getStatus()});
            }
        }
        JTable table = new JTable(model);
        styleTable(table);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    void showAdminDashboard() {
        JPanel page = new JPanel(new BorderLayout(15, 15));
        page.setBackground(COLOR_BG_LIGHT);
        page.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(COLOR_PRIMARY);
        top.setOpaque(true);
        top.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel adminTitle = new JLabel("HOOP SHOT - ADMIN");
        adminTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        adminTitle.setForeground(Color.WHITE);

        JButton logout = styledActionButton("Log out", COLOR_DANGER);
        logout.setPreferredSize(new Dimension(100, 34));
        logout.setMaximumSize(new Dimension(100, 34));
        logout.addActionListener(e -> { currentUser = null; showSplashAndLogin(); });
        
        top.add(adminTitle, BorderLayout.WEST);
        top.add(logout, BorderLayout.EAST);

        String[] columns = {"ID", "Customer Name", "Contact No.", "Date", "Time Slot", "Hours", "Amount", "Payment Proof Path", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (Reservation r : store.getReservations()) {
            if (r != null) {
                model.addRow(new Object[]{r.getId(), r.getFullName(), r.getContact(), r.getDate(), r.getTime(), r.getDuration(), peso(r.getAmount()), r.getProofPath(), r.getStatus()});
            }
        }
        JTable table = new JTable(model);
        styleTable(table);

        int[] widths = {80, 180, 120, 100, 140, 60, 90, 280, 110};
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actions.setBackground(COLOR_CARD);
        
        JButton viewProof = tableActionButton("View Proof", new Color(13, 148, 136));
        JButton approve = tableActionButton("Approve", COLOR_SUCCESS);
        JButton reject = tableActionButton("Reject", COLOR_DANGER);
        JButton deleteRes = tableActionButton("Delete", new Color(220, 38, 38));
        JButton refresh = tableActionButton("Refresh", COLOR_PRIMARY);

        actions.add(viewProof);
        actions.add(approve);
        actions.add(reject);
        actions.add(deleteRes);
        actions.add(refresh);

        viewProof.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a reservation row first.", "Notice", JOptionPane.INFORMATION_MESSAGE); return; }
            Reservation r = findReservation((String) model.getValueAt(row, 0));
            if (r == null) return;
            File file = new File(r.getProofPath());
            if (!file.exists()) { JOptionPane.showMessageDialog(this, "Payment proof file not found:\n" + r.getProofPath(), "Error", JOptionPane.ERROR_MESSAGE); return; }
            showImagePreview(file);
        });

        approve.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a reservation row first.", "Notice", JOptionPane.INFORMATION_MESSAGE); return; }
            Reservation r = findReservation((String) model.getValueAt(row, 0));
            if (r == null) return;
            r.setStatus("Approved");
            showAdminDashboard();
        });

        reject.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a reservation row first.", "Notice", JOptionPane.INFORMATION_MESSAGE); return; }
            Reservation r = findReservation((String) model.getValueAt(row, 0));
            if (r == null) return;
            r.setStatus("Rejected");
            showAdminDashboard();
        });

        deleteRes.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a reservation row first.", "Notice", JOptionPane.INFORMATION_MESSAGE); return; }
            Reservation r = findReservation((String) model.getValueAt(row, 0));
            if (r == null) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete reservation " + r.getId() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                store.getReservations().remove(r);
                showAdminDashboard();
            }
        });

        refresh.addActionListener(e -> showAdminDashboard());

        JPanel leftContainer = new JPanel(new BorderLayout(10, 10));
        leftContainer.setBackground(COLOR_CARD);
        leftContainer.setBorder(new EmptyBorder(12, 12, 12, 12));
        leftContainer.add(new JScrollPane(table), BorderLayout.CENTER);
        leftContainer.add(actions, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(COLOR_CARD);
        rightPanel.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        rightPanel.setPreferredSize(new Dimension(340, 0));

        JLabel blockedTitle = heading("Blocked Accounts", 18);
        JLabel blockedSub = new JLabel("Manage accounts locked due to failed logins");
        blockedSub.setForeground(COLOR_TEXT_MUTED);
        blockedSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        blockedSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] blockedCols = {"Username", "Full Name"};
        DefaultTableModel blockedModel = new DefaultTableModel(blockedCols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (Userrr u : store.getUsers()) {
            if (u != null && !u.isAdmin() && u.isBlocked()) {
                blockedModel.addRow(new Object[]{u.getUsername(), u.getFullName()});
            }
        }
        JTable blockedTable = new JTable(blockedModel);
        styleTable(blockedTable);

        JButton unblockBtn = styledActionButton("Unblock Account", COLOR_SUCCESS);
        unblockBtn.addActionListener(e -> {
            int row = blockedTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a blocked account from the list.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String targetUsername = (String) blockedModel.getValueAt(row, 0);
            for (Userrr u : store.getUsers()) {
                if (u != null && u.getUsername() != null && u.getUsername().equalsIgnoreCase(targetUsername)) {
                    u.setBlocked(false);
                    u.setLoginAttempts(0);
                    break;
                }
            }
            JOptionPane.showMessageDialog(this, "Account '" + targetUsername + "' has been successfully unblocked.");
            showAdminDashboard();
        });

        rightPanel.add(blockedTitle);
        rightPanel.add(Box.createVerticalStrut(4));
        rightPanel.add(blockedSub);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(new JScrollPane(blockedTable));
        rightPanel.add(Box.createVerticalStrut(15));
        
        JPanel unblockRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        unblockRow.setBackground(COLOR_CARD);
        unblockRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        unblockRow.add(unblockBtn);
        rightPanel.add(unblockRow);

        JPanel mainContent = new JPanel(new BorderLayout(15, 0));
        mainContent.setOpaque(false);
        mainContent.add(leftContainer, BorderLayout.CENTER);
        mainContent.add(rightPanel, BorderLayout.EAST);

        page.add(top, BorderLayout.NORTH);
        page.add(mainContent, BorderLayout.CENTER);
        setContentPane(page);
        revalidate();
        repaint();
    }

    void styleTable(JTable table) {
        table.setRowHeight(36);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setGridColor(COLOR_BORDER);
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(COLOR_TEXT_MAIN);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(226, 232, 240));
        header.setForeground(COLOR_TEXT_MAIN);
        header.setPreferredSize(new Dimension(0, 40));
    }

    void showImagePreview(File file) {
        try {
            ImageIcon original = new ImageIcon(file.getAbsolutePath());
            Image image = original.getImage();
            int maxW = 750, maxH = 500;
            double scale = Math.min((double) maxW / original.getIconWidth(), (double) maxH / original.getIconHeight());
            if (scale > 1) scale = 1;
            Image scaled = image.getScaledInstance(Math.max(1, (int)(original.getIconWidth() * scale)),
                    Math.max(1, (int)(original.getIconHeight() * scale)), Image.SCALE_SMOOTH);
            JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(scaled)), "Payment Proof Preview", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not display image preview.");
        }
    }

    static String[] getTimeSlots() {
        return new String[]{"8:00 AM - 9:00 AM", "9:00 AM - 10:00 AM", "10:00 AM - 11:00 AM",
                "11:00 AM - 12:00 PM", "1:00 PM - 2:00 PM", "2:00 PM - 3:00 PM",
                "3:00 PM - 4:00 PM", "4:00 PM - 5:00 PM", "5:00 PM - 6:00 PM",
                "6:00 PM - 7:00 PM", "7:00 PM - 8:00 PM", "8:00 PM - 9:00 PM"};
    }

    static boolean isSlotAvailable(String date, String time) {
        return isRangeAvailable(date, time, 1);
    }

    static boolean isRangeAvailable(String date, String time, int duration) {
        if (date == null || time == null) return false;
        int requestedStart = Arrays.asList(getTimeSlots()).indexOf(time);
        if (requestedStart < 0 || requestedStart + duration > getTimeSlots().length) return false;
        for (Reservation r : store.getReservations()) {
            if (r != null && r.getDate() != null && r.getDate().equals(date) && r.getStatus() != null && !r.getStatus().equals("Rejected")) {
                int existingStart = Arrays.asList(getTimeSlots()).indexOf(r.getTime());
                if (existingStart >= 0) {
                    int existingEnd = existingStart + Math.max(1, r.getDuration());
                    int requestedEnd = requestedStart + duration;
                    if (requestedStart < existingEnd && existingStart < requestedEnd) return false;
                }
            }
        }
        return true;
    }

    static Reservation findReservation(String id) {
        for (Reservation r : store.getReservations()) {
            if (r != null && r.getId() != null && r.getId().equals(id)) return r;
        }
        return null;
    }

    static String getExtension(String name) {
        if (name == null) return "jpg";
        int dot = name.lastIndexOf('.');
        return (dot < 0) ? "jpg" : name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    static String peso(int amount) { 
        return String.format("₱%,.2f", (double) amount); 
    }

    JLabel heading(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, size));
        label.setForeground(COLOR_TEXT_MAIN);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    void styleField(JComponent field, String tooltip) {
        field.setMaximumSize(new Dimension(380, 40));
        field.setPreferredSize(new Dimension(380, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setToolTipText(tooltip);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    void addAlignedField(JPanel panel, String labelText, JComponent field) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(COLOR_CARD);
        container.setMaximumSize(new Dimension(400, 68));
        container.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(COLOR_TEXT_MAIN);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(label);
        container.add(Box.createVerticalStrut(3));
        
        JPanel fieldWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        fieldWrapper.setBackground(COLOR_CARD);
        fieldWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldWrapper.add(field);
        container.add(fieldWrapper);

        panel.add(container);
    }

    JButton primaryButton(String text) {
        return styledActionButton(text, COLOR_PRIMARY);
    }

    JButton styledActionButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(380, 40));
        button.setMaximumSize(new Dimension(380, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    JButton tableActionButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(150, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new OOP_Project().setVisible(true));
    }
}