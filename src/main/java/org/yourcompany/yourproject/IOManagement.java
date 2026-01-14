package org.yourcompany.yourproject;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

public class IOManagement {

    private static final Semaphore semaphore = new Semaphore(1);
    private static int sharedResource = 0;
    private static String messageBuffer = "";

    public static void openIOManagement(JFrame parent) {

        // ──────────────── Main Frame ────────────────
        JFrame frame = new JFrame("ABSAN-OS  •  I/O Management & Synchronization");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1280, 820);           // good starting size
        frame.setMinimumSize(new Dimension(980, 680));
        frame.setLocationRelativeTo(parent);

        // Try to make it look nicer on high-DPI screens
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // ──────────────── Colors ────────────────
        Color sidebarBg   = new Color(38, 50, 56);    // dark teal-gray
        Color contentBg   = new Color(245, 247, 250);
        Color accentColor = new Color(0, 150, 136);   // teal
        Color textColor   = new Color(33, 33, 33);

        // ──────────────── LEFT SIDEBAR ────────────────
        JPanel sidebar = new JPanel();
        sidebar.setBackground(sidebarBg);
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Title in sidebar
        JLabel title = new JLabel("I/O & Sync Tools");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        sidebar.add(title);

        // Buttons in sidebar
        JButton btnP1       = createSidebarButton("Process-1 → Resource", accentColor);
        JButton btnP2       = createSidebarButton("Process-2 → Resource", accentColor);
        JButton btnSend     = createSidebarButton("Send Message",    new Color(76, 175, 80));
        JButton btnReceive  = createSidebarButton("Receive Message", new Color(255, 152, 0));

        sidebar.add(btnP1);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(btnP2);
        sidebar.add(Box.createVerticalStrut(40));
        sidebar.add(btnSend);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(btnReceive);

        sidebar.add(Box.createVerticalGlue());

        // ──────────────── LOG AREA ────────────────
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

        // ──────────────── Actions ────────────────
        btnP1.addActionListener(e -> accessResource("P1", log));
        btnP2.addActionListener(e -> accessResource("P2", log));

        btnSend.addActionListener(e -> {
            sendMessage("Process-1", "Hello from P1 @ " + System.currentTimeMillis());
            appendWithStyle(log, "[SEND]", new Color(0, 150, 136), " Message sent by Process-1\n");
        });

        btnReceive.addActionListener(e -> {
            String msg = receiveMessage();
            appendWithStyle(log, "[RECV]", new Color(255, 152, 0), " " + msg + "\n");
        });

        // ──────────────── Layout ────────────────
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(contentBg);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Header bar (optional status)
        JLabel header = new JLabel("  ABSAN-OS I/O & Synchronization Simulator", SwingConstants.LEFT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0, new Color(200,200,200)),
                BorderFactory.createEmptyBorder(12,16,12,16)
        ));
        header.setBackground(new Color(250,250,250));
        header.setOpaque(true);

        mainPanel.add(header, BorderLayout.NORTH);

        // Put everything together
        frame.setLayout(new BorderLayout());
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // ─── Helper: nice looking sidebar button ───
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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(accent.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(55, 71, 79));
            }
        });

        return btn;
    }

    // ─── Helper: styled log append ───
    private static void appendWithStyle(JTextArea log, String prefix, Color color, String message) {
        String time = String.format("%tT", System.currentTimeMillis());
        String line = String.format("[%s] %s %s", time, prefix, message);

        log.append(line);

        // Auto scroll to bottom
        log.setCaretPosition(log.getDocument().getLength());
    }

    // ─── Critical Section (Task 4.1) ───
    private static void accessResource(String processName, JTextArea log) {
        new Thread(() -> {
            try {
                appendWithStyle(log, processName, new Color(156, 39, 176), " is WAITING for resource...\n");

                semaphore.acquire();
                appendWithStyle(log, processName, new Color(0, 150, 136), " ENTERED critical section\n");

                sharedResource++;
                Thread.sleep(1800 + (int)(Math.random()*800)); // 1.8–2.6s

                appendWithStyle(log, processName, new Color(33, 150, 243),
                        " updated shared resource → " + sharedResource + "\n");

                semaphore.release();
                appendWithStyle(log, processName, new Color(244, 67, 54), " EXITED critical section\n");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ─── IPC (Task 4.2) ───
    private static synchronized void sendMessage(String sender, String msg) {
        messageBuffer = sender + ": " + msg;
    }

    private static synchronized String receiveMessage() {
        if (messageBuffer.isEmpty()) {
            return "No message available";
        }
        String msg = messageBuffer;
        // messageBuffer = "";   // ← uncomment if you want destructive read
        return msg;
    }
}