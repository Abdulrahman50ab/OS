package org.yourcompany.yourproject;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

public class IOManagement {

    // Semaphore for mutual exclusion (Task 4.1)
    private static final Semaphore semaphore = new Semaphore(1);
    private static int sharedResource = 0;

    // Shared message buffer for IPC (Task 4.2)
    private static String messageBuffer = "";

    public static void openIOManagement(JFrame parent) {

        JFrame frame = new JFrame("ABSAN-OS • I/O Management & Synchronization");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1280, 820);
        frame.setMinimumSize(new Dimension(980, 680));
        frame.setLocationRelativeTo(parent);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Colors
        Color sidebarBg   = new Color(38, 50, 56);
        Color contentBg   = new Color(245, 247, 250);
        Color accentColor = new Color(0, 150, 136);
        Color textColor   = new Color(33, 33, 33);

        // Left sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(sidebarBg);
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("I/O & Synchronization Tools");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        sidebar.add(title);

        // Message input section
        JLabel lblFrom = new JLabel("From:");
        lblFrom.setForeground(Color.WHITE);
        lblFrom.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblFrom.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblFrom);

        JComboBox<String> senderChooser = new JComboBox<>(new String[]{"Process-1", "Process-2"});
        senderChooser.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));
        senderChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        senderChooser.setBackground(Color.WHITE);
        sidebar.add(senderChooser);

        JLabel lblMessage = new JLabel("Message:");
        lblMessage.setForeground(Color.WHITE);
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblMessage);

        JTextField txtMessage = new JTextField("Test message");
        txtMessage.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMessage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        sidebar.add(txtMessage);

        JLabel lblTo = new JLabel("To:");
        lblTo.setForeground(Color.WHITE);
        lblTo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblTo);

        JComboBox<String> receiverChooser = new JComboBox<>(new String[]{"Process-1", "Process-2"});
        receiverChooser.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));
        receiverChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        receiverChooser.setBackground(Color.WHITE);
        sidebar.add(receiverChooser);

        sidebar.add(Box.createVerticalStrut(20));

        // Control buttons
        JButton btnP1      = createSidebarButton("Process-1 Access Resource", accentColor);
        JButton btnP2      = createSidebarButton("Process-2 Access Resource", accentColor);
        JButton btnSend    = createSidebarButton("Send Message",    new Color(76, 175, 80));
        JButton btnReceive = createSidebarButton("Receive Message", new Color(255, 152, 0));

        sidebar.add(btnP1);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(btnP2);
        sidebar.add(Box.createVerticalStrut(40));
        sidebar.add(btnSend);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(btnReceive);

        sidebar.add(Box.createVerticalGlue());

        // Log area
        JTextArea log = new JTextArea();
        log.setEditable(false);
        log.setFont(new Font("Consolas", Font.PLAIN, 14));
        log.setBackground(contentBg);
        log.setForeground(textColor);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        log.setMargin(new Insets(12, 14, 12, 14));

        JScrollPane scrollPane = new JScrollPane(log);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Initial log messages
        appendWithStyle(log, "[SYSTEM]", Color.CYAN, " I/O and Synchronization Simulator initialized.\n");
        appendWithStyle(log, "[INFO]",    Color.YELLOW, " Select sender/receiver and use the controls below.\n\n");

        // Dynamic status header
        JLabel header = new JLabel("", SwingConstants.LEFT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0, new Color(200,200,200)),
                BorderFactory.createEmptyBorder(12,16,12,16)
        ));
        header.setBackground(new Color(250,250,250));
        header.setOpaque(true);

        // Timer to update header periodically
        javax.swing.Timer statusTimer = new javax.swing.Timer(400, e -> {
            String bufferStatus = messageBuffer.isEmpty() ? "Empty" : "Has Message";
            header.setText(" ABSAN-OS I/O Simulator | Shared Resource: " + sharedResource + " | Message Buffer: " + bufferStatus);
        });
        statusTimer.start();

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(contentBg);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(header, BorderLayout.NORTH);

        // Event handlers
        btnP1.addActionListener(e -> accessResource("Process-1", log));
        btnP2.addActionListener(e -> accessResource("Process-2", log));

        btnSend.addActionListener(e -> {
            String sender = (String) senderChooser.getSelectedItem();
            String receiver = (String) receiverChooser.getSelectedItem();
            String msg = txtMessage.getText().trim();

            if (!msg.isEmpty()) {
                sendMessage(sender, msg);
                appendWithStyle(log, "[SEND]", new Color(76, 175, 80),
                        sender + " sent to " + receiver + ": " + msg + "\n");
                txtMessage.setText("");
                txtMessage.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Please enter a message before sending.",
                        "Empty Message",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        btnReceive.addActionListener(e -> {
            String receiver = (String) receiverChooser.getSelectedItem();
            String msg = receiveMessage();
            appendWithStyle(log, "[RECV]", new Color(255, 152, 0),
                    receiver + " received: " + msg + "\n");
        });

        // Frame layout
        frame.setLayout(new BorderLayout());
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static JButton createSidebarButton(String text, Color accent) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 48));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBackground(new Color(55, 71, 79));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(accent.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(55, 71, 79));
            }
        });

        return btn;
    }

    private static void appendWithStyle(JTextArea log, String prefix, Color color, String message) {
        String time = String.format("%tT", System.currentTimeMillis());
        String line = String.format("[%s] %s %s", time, prefix, message);
        log.append(line + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }

    private static void accessResource(String processName, JTextArea log) {
        new Thread(() -> {
            try {
                appendWithStyle(log, processName, new Color(156, 39, 176),
                        " is waiting to acquire resource...\n");

                semaphore.acquire();
                appendWithStyle(log, processName, new Color(0, 150, 136),
                        " entered critical section\n");

                sharedResource++;
                Thread.sleep(1800 + (int)(Math.random() * 800));

                appendWithStyle(log, processName, new Color(33, 150, 243),
                        " updated shared resource to " + sharedResource + "\n");

                semaphore.release();
                appendWithStyle(log, processName, new Color(244, 67, 54),
                        " exited critical section\n\n");

            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static synchronized void sendMessage(String sender, String msg) {
        messageBuffer = sender + ": " + msg;
    }

    private static synchronized String receiveMessage() {
        if (messageBuffer.isEmpty()) {
            return "No message available in buffer";
        }
        return messageBuffer;
        // To make receive destructive (clear buffer), uncomment next line:
        // messageBuffer = "";
    }
}