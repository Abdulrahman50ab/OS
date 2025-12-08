package org.yourcompany.yourproject;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ABSANOS {
    private static final List<ProcessControlBlock> processList = new ArrayList<>();
    private static DefaultTableModel processTableModel;
    private static JTable processTable;
    private static Random random = new Random();
    
    // Kernel Configuration
    private static int maxProcesses = 50;
    private static int maxMemory = 1024; // MB
    private static int timeQuantum = 4;
    private static String schedulingAlgorithm = "Round Robin";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(ABSANOS::createMainWindow);
    }

    private static void createMainWindow() {
        JFrame frame = new JFrame("ABSAN-OS - Operating System Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLocationRelativeTo(null);

     JPanel mainPanel = new JPanel(new BorderLayout()) {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();

        // TOP → DEEP PURPLE
        // MIDDLE → MAGENTA / PINK
        // BOTTOM → ORANGE / RED

        Color top    = new Color(60, 0, 120);      // Deep Purple
        Color middle = new Color(120, 189, 255);     // Vibrant Magenta
        Color bottom = new Color(160, 80, 0);      // Hot Orange-Red

        GradientPaint gp1 = new GradientPaint(0, 0, top, 0, h * 0.5f, middle);
        GradientPaint gp2 = new GradientPaint(0, h * 0.5f, middle, 0, h, bottom);

        g2d.setPaint(gp1);
        g2d.fillRect(0, 0, w, h / 2);

        g2d.setPaint(gp2);
        g2d.fillRect(0, h / 2, w, h / 2);

        g2d.dispose();
    }
};
mainPanel.setOpaque(false); // Zaroori hai!

        // Title Panel
     // KILLER GRADIENT TITLE (ABSAN-OS)
JLabel title = new JLabel("ABSAN-OS", SwingConstants.CENTER) {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();

        // Gradient colors (same as background but brighter for glow)
        Color start = new Color(100, 200, 20);   
        Color end   = new Color(0, 120, 254);   // Pink

        GradientPaint gp = new GradientPaint(0, 0, start, w, h, end);
        g2d.setPaint(gp);
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(getText())) / 2;
        int y = fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2;

        g2d.drawString(getText(), x, y);

        // Optional: Add subtle glow (outline)
        g2d.setColor(new Color(255, 255, 255, 80));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawString(getText(), x, y);

        g2d.dispose();
    }
};
title.setFont(new Font("Montserrat", Font.BOLD, 110));  // PREMIUM FONT
title.setBorder(new EmptyBorder(80, 0, 20, 0));
title.setOpaque(false);

// GRADIENT SUBTITLE
JLabel subtitle = new JLabel("Operating System Simulation", SwingConstants.CENTER) {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, new Color(180, 220, 255),
                                            w, h, new Color(255, 180, 255));
        g2d.setPaint(gp);
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(getText())) / 2;
        int y = fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2;

        g2d.drawString(getText(), x, y);
        g2d.dispose();
    }
};
subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
subtitle.setOpaque(false);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.CENTER);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        // Main menu buttons - Only 4 buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(40, 40, 100, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        String[][] buttons = {
            {"Process Management", "🖥️", "Process"},
            {"Memory Management", "💾", "Memory"},
            {"I/O Management", "", "IO"},
            {"Other Operations", "", "Other"}
        };

        int col = 0;
        for (String[] btn : buttons) {
            gbc.gridx = col++;
            gbc.gridy = 0;
            JButton button = createModernButton(btn[0], btn[1]);
            
            String action = btn[2];
            button.addActionListener(e -> {
                if (action.equals("Process")) {
                    openProcessManagement(frame);
                } else {
                    JOptionPane.showMessageDialog(frame,
                        btn[0] + " module is under development", "ABSAN-OS", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });
            buttonPanel.add(button, gbc);
        }

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JButton createModernButton(String text, String emoji) {
        JButton btn = new JButton("<html><center>" + emoji + "<br>" + text + "</center></html>");
        btn.setPreferredSize(new Dimension(200, 110));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Color base = new Color(0, 0, 0);
        Color hover = new Color(52, 152, 219);

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bg = btn.getModel().isRollover() ? hover : base;
                
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
                
                g2.setColor(new Color(0, 255, 255, 80));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(2, 2, c.getWidth()-5, c.getHeight()-5, 18, 18);
                
                super.paint(g2, c);
                g2.dispose();
            }
        });
        return btn;
    }

    // ==================== PROCESS MANAGEMENT ====================
    private static void openProcessManagement(JFrame parent) {
        JFrame frame = new JFrame("ABSAN-OS - Process Management");
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(parent);
        frame.getContentPane().setBackground(new Color(20, 25, 35));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("PROCESS MANAGEMENT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel info = new JLabel("Total: 0 | Running: 0 | Ready: 0 | Blocked: 0 | Suspended: 0");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        info.setForeground(new Color(220, 220, 220));

        header.add(title, BorderLayout.NORTH);
        header.add(info, BorderLayout.SOUTH);

        // Left panel - Operations
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(30, 39, 46));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(72, 219, 251)),
            new EmptyBorder(20, 15, 20, 15)
        ));

        String[][] operations = {
            {"Create Process", "➕"},
            {"Destroy Process", "❌"},
            {"Suspend Process", "⏸️"},
            {"Resume Process", "▶️"},
            {"Block Process", "🚫"},
            {"Wakeup Process", "⏰"},
            {"Dispatch Process", "🚀"},
            {"Change Priority", "⚖️"},
            {"IPC", "📡"},
            {"View PCB", "📋"},
            {"Configuration", "🔧"}
        };

        for (String[] op : operations) {
            JButton btn = createOperationButton(op[0], op[1]);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            btn.addActionListener(e -> handleProcessOperation(op[0], frame, info));
            
            leftPanel.add(btn);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        JScrollPane leftScroll = new JScrollPane(leftPanel);
        leftScroll.setBorder(null);
        leftScroll.setPreferredSize(new Dimension(250, 600));

        // Center panel - Process Table
        processTableModel = new DefaultTableModel(
            new String[]{"PID", "Name", "Status", "Priority", "Memory", "CPU Core", "Arrival", "Burst"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        processTable = new JTable(processTableModel);
        processTable.setRowHeight(40);
        processTable.setFont(new Font("Consolas", Font.PLAIN, 13));
        processTable.setBackground(new Color(35, 43, 53));
        processTable.setForeground(Color.WHITE);
        processTable.setSelectionBackground(new Color(52, 152, 219));
        processTable.setSelectionForeground(Color.WHITE);
        processTable.setGridColor(new Color(60, 70, 80));
        processTable.setShowGrid(true);
        processTable.setShowHorizontalLines(true);
        processTable.setShowVerticalLines(true);

        JTableHeader tableHeader = processTable.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableHeader.setOpaque(false);
        tableHeader.setBackground(new Color(41, 128, 185));
        tableHeader.setForeground(Color.WHITE);            
       
        tableHeader.setPreferredSize(new Dimension(100, 45));

        JScrollPane tableScroll = new JScrollPane(processTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(72, 219, 251), 2));
        tableScroll.getViewport().setBackground(new Color(35, 43, 53));

        // Main layout
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(header, BorderLayout.NORTH);
        frame.add(leftScroll, BorderLayout.WEST);
        frame.add(tableScroll, BorderLayout.CENTER);

        refreshProcessTable(info);
        frame.setVisible(true);
    }

    private static JButton createOperationButton(String text, String emoji) {
        JButton btn = new JButton("<html>" + emoji + "  " + text + "</html>");
        btn.setPreferredSize(new Dimension(220, 48));
        btn.setMaximumSize(new Dimension(220, 48));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(72, 219, 251), 2),
            new EmptyBorder(8, 12, 8, 12)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(30, 30, 30));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.BLACK);
            }
        });

        return btn;
    }

    private static void handleProcessOperation(String action, JFrame frame, JLabel info) {
        switch (action) {
            case "Create Process":
                openCreateProcessDialog(frame, info);
                break;
            case "Destroy Process":
                destroyProcess(info);
                break;
            case "Suspend Process":
                changeProcessState("Suspended", info);
                break;
            case "Resume Process":
                resumeProcess(info);
                break;
            case "Block Process":
                changeProcessState("Blocked", info);
                break;
            case "Wakeup Process":
                wakeupProcess(info);
                break;
            case "Dispatch Process":
                dispatchProcess(info);
                break;
            case "Change Priority":
                changePriority(info);
                break;
            case "IPC":
                openIPCDialog(frame);
                break;
            case "View PCB":
                showPCBDetails();
                break;
            case "Configuration":
                openConfiguration(frame);
                break;
        }
    }

    private static void openCreateProcessDialog(JFrame parent, JLabel info) {
        JDialog dialog = new JDialog(parent, "Create New Process", true);
        dialog.setSize(550, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(30, 39, 46));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(25, 35, 25, 35));
        main.setOpaque(false);

        JLabel title = new JLabel("➕ Create New Process");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(72, 219, 251));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 25)));

        // Input fields
        JTextField nameField = createStyledTextField("Process Name");
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"1 (Highest)", "2", "3", "4", "5 (Lowest)"});
        priorityBox.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        JTextField memoryField = createStyledTextField("Memory Required (MB)");
        JTextField burstField = createStyledTextField("Burst Time");
        JTextField arrivalField = createStyledTextField("Arrival Time");

        addFormRow(main, "Process Name:", nameField);
        addFormRow(main, "Priority:", priorityBox);
        addFormRow(main, "Memory (MB):", memoryField);
        addFormRow(main, "Burst Time:", burstField);
        addFormRow(main, "Arrival Time:", arrivalField);

        main.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton createBtn = new JButton("Create Process");
        createBtn.setContentAreaFilled(false);
        createBtn.setOpaque(true);
        styleActionButton(createBtn, new Color(39, 174, 96));
        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        createBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int priority = priorityBox.getSelectedIndex() + 1;
                int memory = Integer.parseInt(memoryField.getText().trim());
                int burst = Integer.parseInt(burstField.getText().trim());
                int arrival = Integer.parseInt(arrivalField.getText().trim());

                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Process Name is required!");
                }

                ProcessControlBlock pcb = new ProcessControlBlock(name, priority, memory, burst, arrival);
                processList.add(pcb);

                refreshProcessTable(info);
                JOptionPane.showMessageDialog(dialog, 
                    "✓ Process Created Successfully!\nPID: " + pcb.pid, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Error: " + ex.getMessage(), 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        main.add(createBtn);
        dialog.add(main);
        dialog.setVisible(true);
    }

    private static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(44, 62, 80));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(72, 219, 251), 2),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private static void addFormRow(JPanel panel, String label, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(480, 45));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        lbl.setPreferredSize(new Dimension(130, 30));

        row.add(lbl, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);

        panel.add(row);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private static void refreshProcessTable(JLabel info) {
        processTableModel.setRowCount(0);
        int running = 0, ready = 0, blocked = 0, suspended = 0;

        for (ProcessControlBlock pcb : processList) {
            processTableModel.addRow(new Object[]{
                pcb.pid,
                pcb.processName,
                pcb.state,
                pcb.priority,
                pcb.owner,
                pcb.memoryRequirements + " MB",
                pcb.cpuCore >= 0 ? pcb.cpuCore : "-",
                pcb.arrivalTime,
                pcb.burstTime
            });

            if (pcb.state.equals("Running")) running++;
            else if (pcb.state.equals("Ready")) ready++;
            else if (pcb.state.equals("Blocked")) blocked++;
            else if (pcb.state.equals("Suspended")) suspended++;
        }

        if (info != null) {
            info.setText(String.format("Total: %d | Running: %d | Ready: %d | Blocked: %d | Suspended: %d",
                processList.size(), running, ready, blocked, suspended));
        }
    }

    private static void destroyProcess(JLabel info) {
        int row = processTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "⚠️ Please select a process first!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int pid = (int) processTableModel.getValueAt(row, 0);
        ProcessControlBlock toRemove = null;
        
        for (ProcessControlBlock pcb : processList) {
            if (pcb.pid == pid) {
                toRemove = pcb;
                break;
            }
        }

        if (toRemove != null) {
            processList.remove(toRemove);
            refreshProcessTable(info);
            JOptionPane.showMessageDialog(null, "✓ Process " + pid + " destroyed successfully!");
        }
    }

    private static void changeProcessState(String newState, JLabel info) {
        int row = processTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "⚠️ Please select a process first!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int pid = (int) processTableModel.getValueAt(row, 0);
        for (ProcessControlBlock pcb : processList) {
            if (pcb.pid == pid) {
                String oldState = pcb.state;
                pcb.state = newState;
                if (newState.equals("Blocked") || newState.equals("Suspended")) {
                    pcb.cpuCore = -1;
                }
                refreshProcessTable(info);
                JOptionPane.showMessageDialog(null, 
                    "✓ Process " + pid + " state changed from " + oldState + " to " + newState);
                return;
            }
        }
    }

    private static void resumeProcess(JLabel info) {
        int row = processTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "⚠️ Please select a process first!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int pid = (int) processTableModel.getValueAt(row, 0);
        for (ProcessControlBlock pcb : processList) {
            if (pcb.pid == pid) {
                if (!pcb.state.equals("Suspended")) {
                    JOptionPane.showMessageDialog(null, 
                        "❌ Only Suspended processes can be resumed!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                pcb.state = "Ready";
                refreshProcessTable(info);
                JOptionPane.showMessageDialog(null, "✓ Process " + pid + " resumed to Ready state!");
                return;
            }
        }
    }

    private static void wakeupProcess(JLabel info) {
        int row = processTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "⚠️ Please select a process first!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int pid = (int) processTableModel.getValueAt(row, 0);
        for (ProcessControlBlock pcb : processList) {
            if (pcb.pid == pid) {
                if (!pcb.state.equals("Blocked")) {
                    JOptionPane.showMessageDialog(null, 
                        "❌ Only Blocked processes can be woken up!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                pcb.state = "Ready";
                refreshProcessTable(info);
                JOptionPane.showMessageDialog(null, "✓ Process " + pid + " woken up to Ready state!");
                return;
            }
        }
    }

    private static void dispatchProcess(JLabel info) {
        int row = processTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "⚠️ Please select a process first!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int pid = (int) processTableModel.getValueAt(row, 0);
        ProcessControlBlock selectedPcb = null;
        
        for (ProcessControlBlock pcb : processList) {
            if (pcb.pid == pid) {
                selectedPcb = pcb;
                break;
            }
        }

        if (selectedPcb != null) {
            if (!selectedPcb.state.equals("Ready")) {
                JOptionPane.showMessageDialog(null, 
                    "❌ Only Ready processes can be dispatched!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Set all currently running processes to Ready
            for (ProcessControlBlock pcb : processList) {
                if (pcb.state.equals("Running")) {
                    pcb.state = "Ready";
                    pcb.cpuCore = -1;
                }
            }

            // Dispatch selected process
            selectedPcb.state = "Running";
            selectedPcb.cpuCore = random.nextInt(4); // Assign CPU core 0-3
            
            refreshProcessTable(info);
            JOptionPane.showMessageDialog(null, 
                "🚀 Process " + pid + " dispatched to CPU Core " + selectedPcb.cpuCore + "!");
        }
    }

    private static void changePriority(JLabel info) {
        int row = processTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "⚠️ Please select a process first!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] priorities = {"1 (Highest)", "2", "3", "4", "5 (Lowest)"};
        String selected = (String) JOptionPane.showInputDialog(null, 
            "Select new priority:", "Change Priority",
            JOptionPane.QUESTION_MESSAGE, null, priorities, priorities[2]);

        if (selected != null) {
            int newPriority = Integer.parseInt(selected.substring(0, 1));
            int pid = (int) processTableModel.getValueAt(row, 0);
            
            for (ProcessControlBlock pcb : processList) {
                if (pcb.pid == pid) {
                    int oldPriority = pcb.priority;
                    pcb.priority = newPriority;
                    refreshProcessTable(info);
                    JOptionPane.showMessageDialog(null, 
                        "✓ Process " + pid + " priority changed from " + oldPriority + " to " + newPriority);
                    return;
                }
            }
        }
    }

    private static void openIPCDialog(JFrame parent) {
        if (processList.size() < 2) {
            JOptionPane.showMessageDialog(parent, 
                "❌ Need at least 2 processes for IPC!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(parent, "Inter-Process Communication", true);
        dialog.setSize(650, 480);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(30, 39, 46));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(25, 35, 25, 35));
        main.setOpaque(false);

        JLabel title = new JLabel("📡 Inter-Process Communication");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(72, 219, 251));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComboBox<String> senderBox = new JComboBox<>();
        JComboBox<String> receiverBox = new JComboBox<>();
        JComboBox<String> methodBox = new JComboBox<>(new String[]{
            "Message Passing", "Shared Memory", "Pipe", "Socket"
        });

        for (ProcessControlBlock pcb : processList) {
            String item = "PID " + pcb.pid + " - " + pcb.processName;
            senderBox.addItem(item);
            receiverBox.addItem(item);
        }

        JTextArea messageArea = new JTextArea(4, 30);
        messageArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        messageArea.setBackground(new Color(44, 62, 80));
        messageArea.setForeground(Color.WHITE);
        messageArea.setCaretColor(Color.WHITE);
        messageArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(messageArea);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 25)));
        addFormRow(main, "Sender:", senderBox);
        addFormRow(main, "Receiver:", receiverBox);
        addFormRow(main, "Method:", methodBox);
        
        JLabel msgLabel = new JLabel("Message:");
        msgLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        msgLabel.setForeground(Color.WHITE);
        main.add(msgLabel);
        main.add(Box.createRigidArea(new Dimension(0, 8)));
        main.add(scroll);

        main.add(Box.createRigidArea(new Dimension(0, 18)));

        JButton sendBtn = new JButton("📨 Send Message");
        styleActionButton(sendBtn, new Color(41, 128, 185));
        sendBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        sendBtn.addActionListener(e -> {
            String message = messageArea.getText().trim();
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "❌ Please enter a message!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (senderBox.getSelectedIndex() == receiverBox.getSelectedIndex()) {
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Sender and Receiver must be different!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String method = (String) methodBox.getSelectedItem();
            JOptionPane.showMessageDialog(dialog, 
                "✓ Message sent via " + method + "!\n" +
                "From: " + senderBox.getSelectedItem() + "\n" +
                "To: " + receiverBox.getSelectedItem(), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        main.add(sendBtn);
        dialog.add(main);
        dialog.setVisible(true);
    }

    private static void showPCBDetails() {
        int row = processTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "⚠️ Please select a process first!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int pid = (int) processTableModel.getValueAt(row, 0);
        ProcessControlBlock pcb = null;
        
        for (ProcessControlBlock p : processList) {
            if (p.pid == pid) {
                pcb = p;
                break;
            }
        }

        if (pcb != null) {
            StringBuilder details = new StringBuilder();
            details.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            details.append("     PROCESS CONTROL BLOCK     \n");
            details.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            details.append("Process ID: ").append(pcb.pid).append("\n");
            details.append("Process Name: ").append(pcb.processName).append("\n");
            details.append("Current State: ").append(pcb.state).append("\n");
            details.append("Owner: ").append(pcb.owner).append("\n");
            details.append("Priority: ").append(pcb.priority).append("\n");
            details.append("Parent PID: ").append(pcb.parentPID != null ? pcb.parentPID : "None (Root Process)").append("\n");
            details.append("Child Processes: ").append(pcb.childProcesses.isEmpty() ? "None" : pcb.childProcesses.toString()).append("\n");
            details.append("Memory Requirements: ").append(pcb.memoryRequirements).append(" MB\n");
            details.append("Allocated Memory: ").append(pcb.allocatedMemory).append("\n");
            details.append("CPU Core: ").append(pcb.cpuCore >= 0 ? pcb.cpuCore : "Not Assigned").append("\n");
            details.append("Arrival Time: ").append(pcb.arrivalTime).append("\n");
            details.append("Burst Time: ").append(pcb.burstTime).append("\n");
            details.append("Register Save Area: ").append(pcb.registerSaveArea).append("\n");
            details.append("I/O State: ").append(pcb.ioState).append("\n");

            JTextArea textArea = new JTextArea(details.toString());
            textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
            textArea.setEditable(false);
            textArea.setBackground(new Color(35, 43, 53));
            textArea.setForeground(new Color(72, 219, 251));
            textArea.setBorder(new EmptyBorder(15, 15, 15, 15));

            JScrollPane scroll = new JScrollPane(textArea);
            scroll.setPreferredSize(new Dimension(520, 420));

            JOptionPane.showMessageDialog(null, scroll, 
                "📋 PCB Details - Process " + pid, JOptionPane.PLAIN_MESSAGE);
        }
    }

    // ==================== CONFIGURATION ====================
    private static void openConfiguration(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Kernel Configuration", true);
        dialog.setSize(650, 550);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(30, 39, 46));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(25, 35, 25, 35));
        main.setOpaque(false);

        JLabel title = new JLabel("🔧 KERNEL CONFIGURATION");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(72, 219, 251));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 25)));

        JTextField maxProcField = createStyledTextField(String.valueOf(maxProcesses));
        JTextField maxMemField = createStyledTextField(String.valueOf(maxMemory));
        JTextField quantumField = createStyledTextField(String.valueOf(timeQuantum));
        
        JComboBox<String> schedBox = new JComboBox<>(new String[]{
            "Round Robin", "FCFS", "SJF", "Priority Scheduling", "Multilevel Queue"
        });
        schedBox.setSelectedItem(schedulingAlgorithm);
        schedBox.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        addFormRow(main, "Max Processes:", maxProcField);
        addFormRow(main, "Max Memory (MB):", maxMemField);
        addFormRow(main, "Time Quantum:", quantumField);
        addFormRow(main, "Scheduling:", schedBox);

        main.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton saveBtn = new JButton(" Save Configuration");
        saveBtn.setContentAreaFilled(false);
        saveBtn.setOpaque(true);
        styleActionButton(saveBtn, new Color(39, 174, 96));
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        saveBtn.addActionListener(e -> {
            try {
                maxProcesses = Integer.parseInt(maxProcField.getText().trim());
                maxMemory = Integer.parseInt(maxMemField.getText().trim());
                timeQuantum = Integer.parseInt(quantumField.getText().trim());
                schedulingAlgorithm = (String) schedBox.getSelectedItem();

                JOptionPane.showMessageDialog(dialog, 
                    "✓ Configuration saved successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Invalid input! Please enter valid numbers.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        main.add(saveBtn);
        dialog.add(main);
        dialog.setVisible(true);
    }

    private static void styleActionButton(JButton btn, Color color) {
        btn.setPreferredSize(new Dimension(230, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(72, 219, 251), 2),
            new EmptyBorder(8, 18, 8, 18)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
    }

    // ==================== PROCESS CONTROL BLOCK ====================
    static class ProcessControlBlock {
        int pid;
        String processName;
        String state;
        String owner;
        int priority;
        Integer parentPID;
        List<Integer> childProcesses;
        int memoryRequirements;
        String allocatedMemory;
        String registerSaveArea;
        int cpuCore;
        String ioState;
        int arrivalTime;
        int burstTime;

        ProcessControlBlock(String name, int priority, 
                           int memory, int burst, int arrival) {
            this.pid = generateRandomPID();
            this.processName = name;
            this.state = "Ready";
            this.owner = "System";
            this.priority = priority;
            this.parentPID = null;
            this.childProcesses = new ArrayList<>();
            this.memoryRequirements = memory;
            this.allocatedMemory = "0x" + Integer.toHexString(random.nextInt(0xFFFFFF) + 0x100000).toUpperCase();
            this.registerSaveArea = "REG_" + this.pid;
            this.cpuCore = -1;
            this.ioState = "No I/O";
            this.arrivalTime = arrival;
            this.burstTime = burst;
        }
        
        private int generateRandomPID() {
            int newPid;
            boolean exists;
            do {
                newPid = 1000 + random.nextInt(9000); // Generate PID between 1000-9999
                exists = false;
                for (ProcessControlBlock pcb : processList) {
                    if (pcb.pid == newPid) {
                        exists = true;
                        break;
                    }
                }
            } while (exists);
            return newPid;
        }
    }
}