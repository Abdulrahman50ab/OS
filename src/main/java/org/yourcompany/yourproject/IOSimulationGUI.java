package org.yourcompany.yourproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/*
 * =========================================
 * I/O DEVICE SIMULATION – SINGLE FILE
 * =========================================
 */

public class IOSimulationGUI extends JFrame {

    private JTextArea logArea;
    private IOManager ioManager;
    private int pidCounter = 1;

    public IOSimulationGUI() {
        setTitle("ABSAN-OS – I/O Device Simulation");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ioManager = new IOManager(this);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(20, 25, 35));

        add(createHeader(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.WEST);
        add(createLogPanel(), BorderLayout.CENTER);
    }

    // ================= HEADER =================
    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(142, 68, 173));
        header.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("I/O DEVICE SIMULATION");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        header.add(title);
        return header;
    }

    // ================= BUTTON PANEL =================
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBackground(new Color(30, 39, 46));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(createDeviceButton("Keyboard"));
        panel.add(createDeviceButton("Disk"));
        panel.add(createDeviceButton("Printer"));
        panel.add(createDeviceButton("Display"));

        return panel;
    }

    private JButton createDeviceButton(String device) {
        JButton btn = new JButton(device);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(true);


        btn.addActionListener(e -> {
            ProcessControlBlock pcb = new ProcessControlBlock(pidCounter++);
            pcb.setState(ProcessControlBlock.State.RUNNING);
            log("CPU → " + pcb + " RUNNING");
            ioManager.requestIO(device, pcb);
        });

        return btn;
    }

    // ================= LOG PANEL =================
    private JScrollPane createLogPanel() {
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        logArea.setBackground(new Color(35, 43, 53));
        logArea.setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(new EmptyBorder(10, 10, 10, 10));
        return scroll;
    }

    public void log(String msg) {
        SwingUtilities.invokeLater(() ->
                logArea.append(msg + "\n")
        );
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new IOSimulationGUI().setVisible(true);
        });
    }

    /*
     * =========================================
     * INNER CLASSES (OS LOGIC)
     * =========================================
     */

    // -------- PCB --------
    static class ProcessControlBlock {

        enum State { READY, RUNNING, BLOCKED }

        private int pid;
        private State state;

        public ProcessControlBlock(int pid) {
            this.pid = pid;
            this.state = State.READY;
        }

        public void setState(State state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return "P" + pid + " [" + state + "]";
        }
    }

    // -------- IO REQUEST --------
    static class IORequest {
        ProcessControlBlock process;

        IORequest(ProcessControlBlock process) {
            this.process = process;
        }
    }

    // -------- IO DEVICE --------
    static class IODevice {

        enum Status { IDLE, BUSY }

        private String deviceId;
        private Status status = Status.IDLE;
        private Queue<IORequest> queue = new LinkedList<>();
        private IOSimulationGUI gui;
        private ProcessControlBlock current;

        public IODevice(String deviceId, IOSimulationGUI gui) {
            this.deviceId = deviceId;
            this.gui = gui;
        }

        public void request(ProcessControlBlock pcb) {
            pcb.setState(ProcessControlBlock.State.BLOCKED);
            queue.add(new IORequest(pcb));
            gui.log(pcb + " → BLOCKED (" + deviceId + " I/O requested)");
            schedule();
        }

        private void schedule() {
            if (status == Status.IDLE && !queue.isEmpty()) {
                IORequest req = queue.poll();
                current = req.process;
                status = Status.BUSY;

                gui.log(deviceId + " → Assigned to " + current);

                // Interrupt Simulation
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {}

                    complete();
                }).start();
            }
        }

        private void complete() {
            gui.log("INTERRUPT → " + deviceId + " completed for " + current);
            current.setState(ProcessControlBlock.State.READY);
            gui.log(current + " → READY");

            current = null;
            status = Status.IDLE;
            schedule();
        }
    }

    // -------- IO MANAGER --------
    static class IOManager {

        private Map<String, IODevice> devices = new HashMap<>();

        public IOManager(IOSimulationGUI gui) {
            devices.put("Keyboard", new IODevice("Keyboard", gui));
            devices.put("Disk", new IODevice("Disk", gui));
            devices.put("Printer", new IODevice("Printer", gui));
            devices.put("Display", new IODevice("Display", gui));
        }

        public void requestIO(String device, ProcessControlBlock pcb) {
            devices.get(device).request(pcb);
        }
    }
}
