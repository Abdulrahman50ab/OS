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
import java.util.Collections;
import java.util.Comparator;
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
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;



public class ABSANOS {
    private static final List<ProcessControlBlock> processList = new ArrayList<>();
    private static DefaultTableModel processTableModel;
    private static JTable processTable;
    private static Random random = new Random();
    private static BufferedImage backgroundImage;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(ABSANOS::createMainWindow);
    }

    private static void createMainWindow() {
        try {
        backgroundImage = ImageIO.read(
                ABSANOS.class.getResource("/images/pic1.jpg")
             );
            } catch (Exception e) {
                     e.printStackTrace();
            }
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
                if (backgroundImage != null) {
                 g2d.drawImage(
                backgroundImage,
                0, 0,
                getWidth(), getHeight(),
                this
                );
         }
                g2d.dispose();
            }
        };
        mainPanel.setOpaque(false);

        // Title
        JLabel title = new JLabel("ABSAN-OS", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                Color start = new Color(150, 200, 20);
                Color end = new Color(0, 120, 254);

                GradientPaint gp = new GradientPaint(0, 0, start, w, h, end);
                g2d.setPaint(gp);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (w - fm.stringWidth(getText())) / 2;
                int y = fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2;

                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };
        title.setFont(new Font("Montserrat", Font.BOLD, 110));
        title.setBorder(new EmptyBorder(80, 0, 20, 0));
        title.setOpaque(false);

        JLabel subtitle = new JLabel("Operating System Simulation", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

        // Main menu buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(40, 40, 100, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        String[][] buttons = {
                {"Process Management","", "Process"},
                {"Memory Management","", "Memory"},
                {"Synchronization", "", "IO"},
                {"I/O Management","", "Other"}
        };

        int col = 0;
        for (String[] btn : buttons) {
            gbc.gridx = col++;
            gbc.gridy = 0;
            JButton button = createModernButton(btn[0], btn[1]);

            String action = btn[2];
            button.addActionListener(e -> {
                if(action.equals("Process")) {
                    openProcessManagement(frame);
                }
                else if (action.equals("Memory")) {
                 MemoryManagement.openMemoryManagement(frame);
                 }
                 else if (action.equals("IO")) {
                 IOManagement.openIOManagement(frame);
                }else if (action.equals("Other")) {
                   new IOSimulationGUI().setVisible(true);
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

        Color base  = new Color(20, 20, 20, 180);   
        Color hover = new Color(72, 219, 251);      



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
                g2.drawRoundRect(2, 2, c.getWidth() - 5, c.getHeight() - 5, 18, 18);

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
                new EmptyBorder(20, 15, 20, 15)));

        String[][] operations = {
                 {"Create Process", "➕"},
                {"Destroy Process", "❌"},
                {"Suspend Process", "⏸️"},
                {"Resume Process", "▶️"},
                {"Block Process", "🚫"},
                {"Wakeup Process", "⏰"},
                {"Dispatch Process", "▶️"},
                {"PCB", "📋"},
                {"Scheduling", "📊"}
        };

        for (String[] op : operations) {
            JButton btn = createOperationButton(op[0], op[1]);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            btn.addActionListener(e -> handleProcessOperation(op[0], frame, info));

            leftPanel.add(btn);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        JScrollPane leftScroll = new JScrollPane(leftPanel);
        leftScroll.setBorder(null);
        leftScroll.setPreferredSize(new Dimension(250, 600));

        // Center panel - Process Table
        processTableModel = new DefaultTableModel(
                new String[]{"PID", "Name", "Status", "Priority", "Arrival", "Burst"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        processTable = new JTable(processTableModel);
        processTable.setRowHeight(40);
        processTable.setFont(new Font("Consolas", Font.PLAIN, 13));
        processTable.setBackground(new Color(30, 30, 30));
        processTable.setForeground(Color.WHITE);
        processTable.setSelectionBackground(new Color(52, 152, 219));
        processTable.setSelectionForeground(Color.WHITE);
        processTable.setGridColor(new Color(60, 70, 80));

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
        JButton btn = new JButton("<html>" + emoji + " " +text + "</html>");
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
                new EmptyBorder(8, 12, 8, 12)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(52, 152, 219));
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
                askProcessCountAndCreate(frame, info);
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
            case "PCB":
                showPCBDetails();
                break;
            case "Scheduling":
                openSchedulingWindow(frame);
                break;
        }
    }
    private static void askProcessCountAndCreate(JFrame parent, JLabel info) {

    String input = JOptionPane.showInputDialog(
            parent,
            "How many processes do you want to create?",
            "Create Multiple Processes",
            JOptionPane.QUESTION_MESSAGE
    );

    if (input == null) return; // cancel pressed

    int count;
    try {
        count = Integer.parseInt(input.trim());
        if (count <= 0) throw new NumberFormatException();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(parent,
                "❌ Please enter a valid positive number!",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    // 🔁 LOOP to open dialog multiple times
    for (int i = 1; i <= count; i++) {
        openCreateProcessDialog(parent, info, i, count);
    }
}


    private static void openCreateProcessDialog(JFrame parent, JLabel info, int current,
        int total) {
        JDialog dialog = new JDialog(parent,  "Create Process (" + current + " of " + total + ")", true);
        dialog.setSize(550, 550);
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

        JTextField burstField = createStyledTextField("Burst Time");
        JTextField arrivalField = createStyledTextField("Arrival Time");

        addFormRow(main, "Process Name:", nameField);
        addFormRow(main, "Priority:", priorityBox);
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
                int burst = Integer.parseInt(burstField.getText().trim());
                int arrival = Integer.parseInt(arrivalField.getText().trim());

                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Process Name is required!");
                }

                ProcessControlBlock pcb = new ProcessControlBlock(name, priority, burst, arrival);
                processList.add(pcb);

                refreshProcessTable(info);
                JOptionPane.showMessageDialog(dialog,
                        "✓ Process Created Successfully!\nPID: " + pcb.pid,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "❌ Please enter valid numbers for Burst Time and Arrival Time!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
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
                new EmptyBorder(8, 10, 8, 10)));
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
                pcb.cpuCore = -1;
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
            selectedPcb.cpuCore = random.nextInt(4);

            refreshProcessTable(info);
            JOptionPane.showMessageDialog(null,
                    "🚀 Process " + pid + " dispatched to CPU Core " + selectedPcb.cpuCore + "!");
        }
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

    // ==================== SCHEDULING ====================
    private static void openSchedulingWindow(JFrame parent) {
        List<ProcessControlBlock> readyProcesses = new ArrayList<>();
        for (ProcessControlBlock pcb : processList) {
            if (pcb.state.equals("Ready")) {
                readyProcesses.add(pcb);
            }
        }

        if (readyProcesses.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                    "❌ No Ready processes available for scheduling!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(parent, "CPU Scheduling Algorithms", true);
        dialog.setSize(700, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(30, 39, 46));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(25, 35, 25, 35));
        main.setOpaque(false);

        JLabel title = new JLabel("📊 Select Scheduling Algorithm");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(72, 219, 251));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 30)));

        String[] algorithms = {"Round Robin", "FCFS", "SJF (Non-Preemptive)", 
                               "SJF (Preemptive)", "Priority Scheduling"};

        for (String algo : algorithms) {
            JButton btn = new JButton(algo);
            btn.setPreferredSize(new Dimension(400, 60));
            btn.setMaximumSize(new Dimension(400, 60));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            styleActionButton(btn, new Color(41, 128, 185));

            btn.addActionListener(e -> {
                if (algo.equals("Round Robin")) {
                    dialog.dispose();
                    askTimeQuantum(parent, readyProcesses);
                } else {
                    dialog.dispose();
                    showSchedulingResult(parent, algo, readyProcesses, 0);
                }
            });

            main.add(btn);
            main.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        dialog.add(main);
        dialog.setVisible(true);
    }

    private static void askTimeQuantum(JFrame parent, List<ProcessControlBlock> readyProcesses) {
        String input = JOptionPane.showInputDialog(parent,
                "Enter Time Quantum for Round Robin:",
                "Time Quantum",
                JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                int quantum = Integer.parseInt(input.trim());
                if (quantum <= 0) {
                    JOptionPane.showMessageDialog(parent,
                            "❌ Time Quantum must be greater than 0!",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                showSchedulingResult(parent, "Round Robin", readyProcesses, quantum);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent,
                        "❌ Please enter a valid number!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void showSchedulingResult(JFrame parent, String algorithm, 
                                           List<ProcessControlBlock> readyProcesses, int quantum) {
        JFrame frame = new JFrame("ABSAN-OS - " + algorithm);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(parent);
        frame.getContentPane().setBackground(new Color(20, 25, 35));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 152, 219));
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        String headerText = algorithm.equals("Round Robin") ? 
            algorithm.toUpperCase() + " (Quantum: " + quantum + ")" : 
            algorithm.toUpperCase();
        
        JLabel title = new JLabel("📊 " + headerText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);

        // Create result table
        DefaultTableModel scheduleModel = new DefaultTableModel(
                new String[]{"PID", "Name", "Arrival", "Burst", "Waiting Time", 
                            "Turnaround Time", "Completion Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        List<ScheduleResult> results = new ArrayList<>();

        switch (algorithm) {
            case "FCFS":
                results = scheduleFCFS(new ArrayList<>(readyProcesses));
                break;
            case "SJF (Non-Preemptive)":
                results = scheduleSJFNonPreemptive(new ArrayList<>(readyProcesses));
                break;
            case "SJF (Preemptive)":
                results = scheduleSJFPreemptive(new ArrayList<>(readyProcesses));
                break;
            case "Priority Scheduling":
                results = schedulePriority(new ArrayList<>(readyProcesses));
                break;
            case "Round Robin":
                results = scheduleRoundRobin(new ArrayList<>(readyProcesses), quantum);
                break;
        }

        double totalWaiting = 0;
        double totalTurnaround = 0;

        for (ScheduleResult result : results) {
            scheduleModel.addRow(new Object[]{
                    result.pid,
                    result.name,
                    result.arrival,
                    result.burst,
                    result.waitingTime,
                    result.turnaroundTime,
                    result.completionTime
            });
            totalWaiting += result.waitingTime;
            totalTurnaround += result.turnaroundTime;
        }

        JTable scheduleTable = new JTable(scheduleModel);
        scheduleTable.setRowHeight(40);
        scheduleTable.setFont(new Font("Consolas", Font.PLAIN, 14));
        scheduleTable.setBackground(new Color(35, 43, 53));
        scheduleTable.setForeground(Color.WHITE);
        scheduleTable.setGridColor(new Color(60, 70, 80));

        JTableHeader tableHeader = scheduleTable.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableHeader.setOpaque(false);
        tableHeader.setBackground(new Color(52, 152, 219));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(100, 45));

        JScrollPane tableScroll = new JScrollPane(scheduleTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(72, 219, 251), 2));

        // Statistics Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(new Color(30, 39, 46));
        statsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        double avgWaiting = totalWaiting / results.size();
        double avgTurnaround = totalTurnaround / results.size();

        JLabel stats = new JLabel(String.format(
                "<html><div style='text-align: center;'>" +
                        "<h2 style='color: #48DBFb;'>Performance Metrics</h2>" +
                        "<p style='color: white; font-size: 16px;'>Average Waiting Time: <b>%.2f</b></p>" +
                        "<p style='color: white; font-size: 16px;'>Average Turnaround Time: <b>%.2f</b></p>" +
                        "</div></html>",
                avgWaiting, avgTurnaround));
        stats.setHorizontalAlignment(SwingConstants.CENTER);
        statsPanel.add(stats);

        frame.setLayout(new BorderLayout(10, 10));
        frame.add(header, BorderLayout.NORTH);
        frame.add(tableScroll, BorderLayout.CENTER);
        frame.add(statsPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // FCFS Scheduling
    private static List<ScheduleResult> scheduleFCFS(List<ProcessControlBlock> processes) {
        Collections.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));
        List<ScheduleResult> results = new ArrayList<>();
        int currentTime = 0;

        for (ProcessControlBlock pcb : processes) {
            if (currentTime < pcb.arrivalTime) {
                currentTime = pcb.arrivalTime;
            }
            int completionTime = currentTime + pcb.burstTime;
            int turnaroundTime = completionTime - pcb.arrivalTime;
            int waitingTime = turnaroundTime - pcb.burstTime;

            results.add(new ScheduleResult(pcb.pid, pcb.processName, pcb.arrivalTime,
                    pcb.burstTime, waitingTime, turnaroundTime, completionTime));
            currentTime = completionTime;
        }
        return results;
    }

    // SJF Non-Preemptive Scheduling
    private static List<ScheduleResult> scheduleSJFNonPreemptive(List<ProcessControlBlock> processes) {
        Collections.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));
        List<ScheduleResult> results = new ArrayList<>();
        boolean[] done = new boolean[processes.size()];
        int completed = 0;
        int currentTime = 0;

        while (completed < processes.size()) {
            int idx = -1;
            int minBurst = Integer.MAX_VALUE;

            for (int i = 0; i < processes.size(); i++) {
                if (!done[i] && processes.get(i).arrivalTime <= currentTime 
                    && processes.get(i).burstTime < minBurst) {
                    minBurst = processes.get(i).burstTime;
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++;
                continue;
            }

            ProcessControlBlock pcb = processes.get(idx);
            currentTime += pcb.burstTime;
            int completionTime = currentTime;
            int turnaroundTime = completionTime - pcb.arrivalTime;
            int waitingTime = turnaroundTime - pcb.burstTime;

            results.add(new ScheduleResult(pcb.pid, pcb.processName, pcb.arrivalTime,
                    pcb.burstTime, waitingTime, turnaroundTime, completionTime));
            done[idx] = true;
            completed++;
        }
        return results;
    }

    // SJF Preemptive Scheduling (SRTF)
    private static List<ScheduleResult> scheduleSJFPreemptive(List<ProcessControlBlock> processes) {
        List<ProcessControlBlock> processCopy = new ArrayList<>();
        for (ProcessControlBlock pcb : processes) {
            ProcessControlBlock copy = new ProcessControlBlock(pcb.processName, pcb.priority, 
                                                               pcb.burstTime, pcb.arrivalTime);
            copy.pid = pcb.pid;
            copy.remainingTime = pcb.burstTime;
            processCopy.add(copy);
        }

        List<ScheduleResult> results = new ArrayList<>();
        int currentTime = 0;
        int completed = 0;
        int[] completionTimes = new int[processCopy.size()];
        boolean[] done = new boolean[processCopy.size()];

        while (completed < processCopy.size()) {
            int idx = -1;
            int minRemaining = Integer.MAX_VALUE;

            for (int i = 0; i < processCopy.size(); i++) {
                if (!done[i] && processCopy.get(i).arrivalTime <= currentTime 
                    && processCopy.get(i).remainingTime < minRemaining 
                    && processCopy.get(i).remainingTime > 0) {
                    minRemaining = processCopy.get(i).remainingTime;
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++;
                continue;
            }

            processCopy.get(idx).remainingTime--;
            currentTime++;

            if (processCopy.get(idx).remainingTime == 0) {
                done[idx] = true;
                completed++;
                completionTimes[idx] = currentTime;
            }
        }

        for (int i = 0; i < processCopy.size(); i++) {
            ProcessControlBlock pcb = processCopy.get(i);
            int completionTime = completionTimes[i];
            int turnaroundTime = completionTime - pcb.arrivalTime;
            int waitingTime = turnaroundTime - pcb.burstTime;

            results.add(new ScheduleResult(pcb.pid, pcb.processName, pcb.arrivalTime,
                    pcb.burstTime, waitingTime, turnaroundTime, completionTime));
        }
        return results;
    }

    // Priority Scheduling (Non-Preemptive)
    private static List<ScheduleResult> schedulePriority(List<ProcessControlBlock> processes) {
        Collections.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));
        List<ScheduleResult> results = new ArrayList<>();
        boolean[] done = new boolean[processes.size()];
        int completed = 0;
        int currentTime = 0;

        while (completed < processes.size()) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < processes.size(); i++) {
                if (!done[i] && processes.get(i).arrivalTime <= currentTime 
                    && processes.get(i).priority < highestPriority) {
                    highestPriority = processes.get(i).priority;
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++;
                continue;
            }

            ProcessControlBlock pcb = processes.get(idx);
            currentTime += pcb.burstTime;
            int completionTime = currentTime;
            int turnaroundTime = completionTime - pcb.arrivalTime;
            int waitingTime = turnaroundTime - pcb.burstTime;

            results.add(new ScheduleResult(pcb.pid, pcb.processName, pcb.arrivalTime,
                    pcb.burstTime, waitingTime, turnaroundTime, completionTime));
            done[idx] = true;
            completed++;
        }
        return results;
    }

    // Round Robin Scheduling
    private static List<ScheduleResult> scheduleRoundRobin(List<ProcessControlBlock> processes, int quantum) {
        List<ProcessControlBlock> processCopy = new ArrayList<>();
        for (ProcessControlBlock pcb : processes) {
            ProcessControlBlock copy = new ProcessControlBlock(pcb.processName, pcb.priority, 
                                                               pcb.burstTime, pcb.arrivalTime);
            copy.pid = pcb.pid;
            copy.remainingTime = pcb.burstTime;
            processCopy.add(copy);
        }

        List<ScheduleResult> results = new ArrayList<>();
        List<ProcessControlBlock> queue = new ArrayList<>();
        int currentTime = 0;
        int idx = 0;
        boolean[] inQueue = new boolean[processCopy.size()];

        while (results.size() < processCopy.size()) {
            // Add arrived processes to queue
            for (int i = 0; i < processCopy.size(); i++) {
                if (!inQueue[i] && processCopy.get(i).arrivalTime <= currentTime 
                    && processCopy.get(i).remainingTime > 0) {
                    queue.add(processCopy.get(i));
                    inQueue[i] = true;
                }
            }

            if (queue.isEmpty()) {
                currentTime++;
                continue;
            }

            ProcessControlBlock pcb = queue.remove(0);
            int executeTime = Math.min(quantum, pcb.remainingTime);
            currentTime += executeTime;
            pcb.remainingTime -= executeTime;

            // Add newly arrived processes
            for (int i = 0; i < processCopy.size(); i++) {
                if (!inQueue[i] && processCopy.get(i).arrivalTime <= currentTime 
                    && processCopy.get(i).remainingTime > 0) {
                    queue.add(processCopy.get(i));
                    inQueue[i] = true;
                }
            }

            if (pcb.remainingTime > 0) {
                queue.add(pcb);
            } else {
                int completionTime = currentTime;
                int turnaroundTime = completionTime - pcb.arrivalTime;
                int waitingTime = turnaroundTime - pcb.burstTime;

                results.add(new ScheduleResult(pcb.pid, pcb.processName, pcb.arrivalTime,
                        pcb.burstTime, waitingTime, turnaroundTime, completionTime));
            }
        }
        return results;
    }

    private static void styleActionButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(72, 219, 251), 2),
                new EmptyBorder(8, 18, 8, 18)));
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

    // ==================== CLASSES ====================
    static class ProcessControlBlock {
        int pid;
        String processName;
        String state;
        String owner;
        int priority;
        String allocatedMemory;
        String registerSaveArea;
        int cpuCore;
        String ioState;
        int arrivalTime;
        int burstTime;
        int remainingTime;

        ProcessControlBlock(String name, int priority, int burst, int arrival) {
            this.pid = generateRandomPID();
            this.processName = name;
            this.state = "Ready";
            this.owner = "System";
            this.priority = priority;
            this.allocatedMemory = "0x" + Integer.toHexString(random.nextInt(0xFFFFFF) + 0x100000).toUpperCase();
            this.registerSaveArea = "REG_" + this.pid;
            this.cpuCore = -1;
            this.ioState = "No I/O";
            this.arrivalTime = arrival;
            this.burstTime = burst;
            this.remainingTime = burst;
        }

        private int generateRandomPID() {
            int newPid;
            boolean exists;
            do {
                newPid = 1000 + random.nextInt(9000);
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

    static class ScheduleResult {
        int pid;
        String name;
        int arrival;
        int burst;
        int waitingTime;
        int turnaroundTime;
        int completionTime;

        ScheduleResult(int pid, String name, int arrival, int burst, int waitingTime,
                       int turnaroundTime, int completionTime) {
            this.pid = pid;
            this.name = name;
            this.arrival = arrival;
            this.burst = burst;
            this.waitingTime = waitingTime;
            this.turnaroundTime = turnaroundTime;
            this.completionTime = completionTime;
        }
    }
}