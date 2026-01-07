package org.yourcompany.yourproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MemoryManagement {
    
    private static int pageSize = 4096; // Default 4KB
    private static final String CONFIG_FILE = "memory_config.txt";
    
    public static void openMemoryManagement(JFrame parent) {
        loadPageSizeFromFile();
        
        JFrame frame = new JFrame("ABSAN-OS - Memory Management");
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(parent);
        frame.getContentPane().setBackground(new Color(20, 25, 35));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(142, 68, 173));
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("💾 MEMORY MANAGEMENT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel info = new JLabel("Page Size: " + pageSize + " bytes");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        info.setForeground(new Color(220, 220, 220));

        header.add(title, BorderLayout.NORTH);
        header.add(info, BorderLayout.SOUTH);

        // Left panel - Operations
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(30, 39, 46));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(155, 89, 182)),
                new EmptyBorder(20, 15, 20, 15)));

        String[][] operations = {
                {"Paging", "📄"},
                {"First Fit", "1️⃣"},
                {"Best Fit", "🎯"},
                {"Next Fit", "➡️"},
                {"Worst Fit", "⚠️"},
                {"LRU Page Replacement", "🔄"},
                {"Configuration", "⚙️"}
        };

        for (String[] op : operations) {
            JButton btn = createOperationButton(op[0], op[1]);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            btn.addActionListener(e -> handleMemoryOperation(op[0], frame, info));

            leftPanel.add(btn);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        JScrollPane leftScroll = new JScrollPane(leftPanel);
        leftScroll.setBorder(null);
        leftScroll.setPreferredSize(new Dimension(250, 600));

        // Center panel - Info
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(35, 43, 53));
        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(155, 89, 182), 2));

        JLabel welcomeText = new JLabel("<html><div style='text-align: center;'>" +
                "<h1 style='color: #9B59B6;'>Memory Management System</h1>" +
                "<p style='color: white; font-size: 16px;'>Select an operation from the left menu</p>" +
                "<br><p style='color: #ECF0F1;'>• Paging with configurable page size</p>" +
                "<p style='color: #ECF0F1;'>• Memory allocation algorithms</p>" +
                "<p style='color: #ECF0F1;'>• LRU page replacement</p>" +
                "</div></html>");
        welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(welcomeText, BorderLayout.CENTER);

        frame.setLayout(new BorderLayout(10, 10));
        frame.add(header, BorderLayout.NORTH);
        frame.add(leftScroll, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);

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
        btn.setBackground(new Color(52, 73, 94));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                new EmptyBorder(8, 12, 8, 12)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(142, 68, 173));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(52, 73, 94));
            }
        });

        return btn;
    }

    private static void handleMemoryOperation(String action, JFrame frame, JLabel info) {
        switch (action) {
            case "Paging":
                openPagingDialog(frame);
                break;
            case "First Fit":
                openMemoryAllocation(frame, "First Fit");
                break;
            case "Best Fit":
                openMemoryAllocation(frame, "Best Fit");
                break;
            case "Next Fit":
                openMemoryAllocation(frame, "Next Fit");
                break;
            case "Worst Fit":
                openMemoryAllocation(frame, "Worst Fit");
                break;
            case "LRU Page Replacement":
                openLRUDialog(frame);
                break;
            case "Configuration":
                openConfiguration(frame, info);
                break;
        }
    }

    // ==================== PAGING ====================
    private static void openPagingDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Paging System", true);
        dialog.setSize(600, 650);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(30, 39, 46));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(25, 35, 25, 35));
        main.setOpaque(false);

        JLabel title = new JLabel("📄 Paging System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(155, 89, 182));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 25)));

        JTextField logicalField = createStyledTextField("Logical Memory (bytes)");
        JTextField physicalField = createStyledTextField("Physical Memory (bytes)");
        JLabel pageSizeLabel = new JLabel("Page Size: " + pageSize + " bytes");
        pageSizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pageSizeLabel.setForeground(Color.WHITE);
        pageSizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        addFormRow(main, "Logical Memory:", logicalField);
        addFormRow(main, "Physical Memory:", physicalField);
        main.add(pageSizeLabel);
        main.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton calculateBtn = new JButton("Calculate Paging");
        styleActionButton(calculateBtn, new Color(142, 68, 173));
        calculateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        calculateBtn.addActionListener(e -> {
            try {
                int logSize = Integer.parseInt(logicalField.getText().trim());
                int phySize = Integer.parseInt(physicalField.getText().trim());

                int pages = logSize / pageSize;
                int frames = phySize / pageSize;

                dialog.dispose();
                showPageTableDialog(parent, pages, frames);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "❌ Please enter valid numbers!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        main.add(calculateBtn);
        dialog.add(main);
        dialog.setVisible(true);
    }

    private static void showPageTableDialog(JFrame parent, int pages, int frames) {
        JDialog dialog = new JDialog(parent, "Page Table Configuration", true);
        dialog.setSize(700, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(30, 39, 46));

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel info = new JLabel(String.format(
                "<html><div style='text-align: center;'>" +
                        "<p style='color: #9B59B6; font-size: 18px;'><b>Pages: %d | Frames: %d | Page Size: %d bytes</b></p>" +
                        "</div></html>", pages, frames, pageSize));
        info.setHorizontalAlignment(SwingConstants.CENTER);

        // Page Table
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Page Number", "Frame Number"}, 0);

        int[] pageTable = new int[pages];
        for (int i = 0; i < pages; i++) {
            int frame = (i < frames) ? i : -1;
            pageTable[i] = frame;
            model.addRow(new Object[]{i, frame == -1 ? "Not in Memory" : frame});
        }

        JTable table = createStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(155, 89, 182), 2));

        JButton translateBtn = new JButton("Translate Logical Address");
        styleActionButton(translateBtn, new Color(142, 68, 173));

        translateBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(dialog,
                    "Enter Logical Address:",
                    "Address Translation",
                    JOptionPane.QUESTION_MESSAGE);

            if (input != null && !input.trim().isEmpty()) {
                try {
                    int logAddr = Integer.parseInt(input.trim());
                    int pageNo = logAddr / pageSize;
                    int offset = logAddr % pageSize;

                    if (pageNo >= pages) {
                        JOptionPane.showMessageDialog(dialog,
                                "❌ Error: Logical address too large!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int frameNo = pageTable[pageNo];

                    if (frameNo == -1) {
                        JOptionPane.showMessageDialog(dialog,
                                String.format("⚠️ Page Fault!\nPage %d is not in memory.", pageNo),
                                "Page Fault", JOptionPane.WARNING_MESSAGE);
                    } else {
                        int phyAddr = frameNo * pageSize + offset;
                        JOptionPane.showMessageDialog(dialog,
                                String.format("<html><b>Address Translation:</b><br><br>" +
                                                "Logical Address: %d<br>" +
                                                "Page Number: %d<br>" +
                                                "Offset: %d<br>" +
                                                "Frame Number: %d<br>" +
                                                "<b>Physical Address: %d</b></html>",
                                        logAddr, pageNo, offset, frameNo, phyAddr),
                                "Translation Result", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "❌ Please enter a valid number!",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        main.add(info, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        main.add(translateBtn, BorderLayout.SOUTH);

        dialog.add(main);
        dialog.setVisible(true);
    }

    // ==================== MEMORY ALLOCATION ====================
    private static void openMemoryAllocation(JFrame parent, String algorithm) {
        JDialog dialog = new JDialog(parent, algorithm + " Algorithm", true);
        dialog.setSize(650, 550);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(0, 0, 0));


        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(25, 35, 25, 35));
        main.setOpaque(false);

        JLabel title = new JLabel(algorithm);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(155, 89, 182));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 25)));

        JTextField blocksField = createStyledTextField("Block Sizes (comma-separated)");
        JTextField processesField = createStyledTextField("Process Sizes (comma-separated)");

        addFormRow(main, "Memory Blocks:", blocksField);
        addFormRow(main, "Process Sizes:", processesField);

        main.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton allocateBtn = new JButton("Allocate Memory");
        styleActionButton(allocateBtn, new Color(142, 68, 173));
        allocateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        allocateBtn.addActionListener(e -> {
            try {
                String[] blockStr = blocksField.getText().trim().split(",");
                String[] processStr = processesField.getText().trim().split(",");

                int[] blocks = new int[blockStr.length];
                int[] processes = new int[processStr.length];

                for (int i = 0; i < blockStr.length; i++) {
                    blocks[i] = Integer.parseInt(blockStr[i].trim());
                }
                for (int i = 0; i < processStr.length; i++) {
                    processes[i] = Integer.parseInt(processStr[i].trim());
                }

                dialog.dispose();
                showAllocationResult(parent, algorithm, blocks, processes);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "❌ Please enter valid comma-separated numbers!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        main.add(allocateBtn);
        dialog.add(main);
        dialog.setVisible(true);
    }

    private static void showAllocationResult(JFrame parent, String algorithm,
                                           int[] blocks, int[] processes) {
        int[] allocation = null;

        switch (algorithm) {
            case "First Fit":
                allocation = firstFit(blocks, processes);
                break;
            case "Best Fit":
                allocation = bestFit(blocks, processes);
                break;
            case "Next Fit":
                allocation = nextFit(blocks, processes);
                break;
            case "Worst Fit":
                allocation = worstFit(blocks, processes);
                break;
        }

        JFrame frame = new JFrame("ABSAN-OS - " + algorithm + " Result");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(parent);
        frame.getContentPane().setBackground(new Color(20, 25, 35));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(142, 68, 173));
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(algorithm + " - Allocation Result");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Process No", "Process Size", "Allocated Block", 
                            "Original Block Size", "Internal Fragmentation"}, 0);

        for (int i = 0; i < processes.length; i++) {
            if (allocation[i] != -1) {
                int blockNo = allocation[i] + 1;
                int origSize = blocks[allocation[i]];
                int internalFrag = origSize - processes[i];
                model.addRow(new Object[]{i + 1, processes[i], blockNo, origSize, internalFrag});
            } else {
                model.addRow(new Object[]{i + 1, processes[i], "Not Allocated", "-", "-"});
            }
        }

        JTable table = createStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(155, 89, 182), 2));

        frame.setLayout(new BorderLayout(10, 10));
        frame.add(header, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Allocation Algorithms
    private static int[] firstFit(int[] blocks, int[] processes) {
        int[] allocation = new int[processes.length];
        int[] remaining = blocks.clone();

        for (int i = 0; i < processes.length; i++) {
            allocation[i] = -1;
            for (int j = 0; j < blocks.length; j++) {
                if (remaining[j] >= processes[i]) {
                    allocation[i] = j;
                    remaining[j] = 0;
                    break;
                }
            }
        }
        return allocation;
    }

    private static int[] bestFit(int[] blocks, int[] processes) {
        int[] allocation = new int[processes.length];
        int[] remaining = blocks.clone();

        for (int i = 0; i < processes.length; i++) {
            allocation[i] = -1;
            int bestIdx = -1;

            for (int j = 0; j < blocks.length; j++) {
                if (remaining[j] >= processes[i]) {
                    if (bestIdx == -1 || remaining[j] < remaining[bestIdx]) {
                        bestIdx = j;
                    }
                }
            }

            if (bestIdx != -1) {
                allocation[i] = bestIdx;
                remaining[bestIdx] = 0;
            }
        }
        return allocation;
    }

    private static int[] nextFit(int[] blocks, int[] processes) {
        int[] allocation = new int[processes.length];
        int[] remaining = blocks.clone();
        int lastAllocated = -1;

        for (int i = 0; i < processes.length; i++) {
            allocation[i] = -1;
            int start = (lastAllocated == -1) ? 0 : (lastAllocated + 1) % blocks.length;

            for (int j = 0; j < blocks.length; j++) {
                int current = (start + j) % blocks.length;
                if (remaining[current] >= processes[i]) {
                    allocation[i] = current;
                    remaining[current] = 0;
                    lastAllocated = current;
                    break;
                }
            }
        }
        return allocation;
    }

    private static int[] worstFit(int[] blocks, int[] processes) {
        int[] allocation = new int[processes.length];
        int[] remaining = blocks.clone();

        for (int i = 0; i < processes.length; i++) {
            allocation[i] = -1;
            int worstIdx = -1;

            for (int j = 0; j < blocks.length; j++) {
                if (remaining[j] >= processes[i]) {
                    if (worstIdx == -1 || remaining[j] > remaining[worstIdx]) {
                        worstIdx = j;
                    }
                }
            }

            if (worstIdx != -1) {
                allocation[i] = worstIdx;
                remaining[worstIdx] = 0;
            }
        }
        return allocation;
    }

    // ==================== LRU PAGE REPLACEMENT ====================
    private static void openLRUDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "LRU Page Replacement", true);
        dialog.setSize(650, 500);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(30, 39, 46));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(25, 35, 25, 35));
        main.setOpaque(false);

        JLabel title = new JLabel("🔄 LRU Page Replacement");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(155, 89, 182));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 25)));

        JTextField framesField = createStyledTextField("Number of Frames");
        JTextField pagesField = createStyledTextField("Page Reference String (comma-separated)");

        addFormRow(main, "Frames:", framesField);
        addFormRow(main, "Pages:", pagesField);

        main.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton simulateBtn = new JButton("Simulate LRU");
        styleActionButton(simulateBtn, new Color(142, 68, 173));
        simulateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        simulateBtn.addActionListener(e -> {
            try {
                int numFrames = Integer.parseInt(framesField.getText().trim());
                String[] pageStr = pagesField.getText().trim().split(",");
                int[] pages = new int[pageStr.length];

                for (int i = 0; i < pageStr.length; i++) {
                    pages[i] = Integer.parseInt(pageStr[i].trim());
                }

                dialog.dispose();
                showLRUResult(parent, numFrames, pages);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "❌ Please enter valid input!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        main.add(simulateBtn);
        dialog.add(main);
        dialog.setVisible(true);
    }

    private static void showLRUResult(JFrame parent, int numFrames, int[] pages) {
        List<Integer> frames = new ArrayList<>();
        LinkedList<Integer> lruOrder = new LinkedList<>();
        int pageFaults = 0;
        List<String> steps = new ArrayList<>();

        for (int page : pages) {
            if (frames.contains(page)) {
                lruOrder.remove((Integer) page);
                lruOrder.addLast(page);
                steps.add("Page " + page + " - Hit | Frames: " + frames.toString());
            } else {
                pageFaults++;
                if (frames.size() < numFrames) {
                    frames.add(page);
                } else {
                    int lru = lruOrder.removeFirst();
                    frames.set(frames.indexOf(lru), page);
                }
                lruOrder.addLast(page);
                steps.add("Page " + page + " - Fault | Frames: " + frames.toString());
            }
        }

        JFrame frame = new JFrame("ABSAN-OS - LRU Result");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(parent);
        frame.getContentPane().setBackground(new Color(20, 25, 35));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(142, 68, 173));
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(String.format("LRU Result - Page Faults: %d | Hit Rate: %.2f%%",
                pageFaults, ((pages.length - pageFaults) * 100.0 / pages.length)));
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Step", "Details"}, 0);
        for (int i = 0; i < steps.size(); i++) {
            model.addRow(new Object[]{i + 1, steps.get(i)});
        }

        JTable table = createStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(155, 89, 182), 2));

        frame.setLayout(new BorderLayout(10, 10));
        frame.add(header, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // ==================== CONFIGURATION ====================
    private static void openConfiguration(JFrame parent, JLabel info) {
        String input = JOptionPane.showInputDialog(parent,
                "Enter new page size (bytes):",
                "Configuration",
                JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                int newSize = Integer.parseInt(input.trim());
                if (newSize <= 0) {
                    throw new IllegalArgumentException("Page size must be positive!");
                }
                pageSize = newSize;
                savePageSizeToFile();
                info.setText("Page Size: " + pageSize + " bytes");
                JOptionPane.showMessageDialog(parent,
                        "✓ Page size updated to " + pageSize + " bytes!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent,
                        "❌ Please enter a valid positive number!",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // File operations
    private static void loadPageSizeFromFile() {
        try {
            File file = new File(CONFIG_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                if (line != null) {
                    pageSize = Integer.parseInt(line.trim());
                }
                reader.close();
            }
        } catch (Exception e) {
            pageSize = 4096; // Default
        }
    }

    private static void savePageSizeToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE));
            writer.write(String.valueOf(pageSize));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper methods
    private static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(44, 62, 80));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                new EmptyBorder(8, 10, 8, 10)));
        return field;
    }

    private static void addFormRow(JPanel panel, String label, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(550, 45));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        lbl.setPreferredSize(new Dimension(150, 30));

        row.add(lbl, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);

        panel.add(row);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private static void styleActionButton(JButton btn, Color color) {
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                new EmptyBorder(10, 20, 10, 20)));
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

    private static JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Consolas", Font.PLAIN, 14));
        table.setOpaque(false);
        table.setBackground(new Color(35, 43, 53));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 70, 80));
        table.setSelectionBackground(new Color(142, 68, 173));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setOpaque(false);
        header.setBackground(new Color(142, 68, 173));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 45));

        return table;
    }
}