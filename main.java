import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private JFrame frame;
    private JTextField taskField;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextArea statusArea;
    private ArrayList<String> tasks;
    private HashMap<String, Integer> taskProgress;

    public Main() {
        tasks = new ArrayList<>();
        taskProgress = new HashMap<>();

        frame = new JFrame("Task Progress Tracker");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        taskField = new JTextField(20);
        JButton addButton = new JButton("Add Task");

        topPanel.add(new JLabel("Task Name:"));
        topPanel.add(taskField);
        topPanel.add(addButton);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane listScroll = new JScrollPane(taskList);

        JPanel bottomPanel = new JPanel();
        JButton progressButton = new JButton("Start Progress");
        JButton completeButton = new JButton("Mark Completed");
        JButton deleteButton = new JButton("Delete Task");

        bottomPanel.add(progressButton);
        bottomPanel.add(completeButton);
        bottomPanel.add(deleteButton);

        statusArea = new JTextArea(8, 20);
        statusArea.setEditable(false);
        JScrollPane statusScroll = new JScrollPane(statusArea);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(listScroll, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(statusScroll, BorderLayout.EAST);

        addButton.addActionListener(e -> {
            String task = taskField.getText().trim();
            if (!task.isEmpty()) {
                tasks.add(task);
                taskProgress.put(task, 0);
                taskListModel.addElement(task);
                statusArea.append("Task Added: " + task + "\n");
                taskField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Enter task name.");
            }
        });

        progressButton.addActionListener(e -> {
            String selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                new Thread(() -> {
                    for (int i = taskProgress.get(selectedTask); i <= 100; i += 10) {
                        taskProgress.put(selectedTask, i);
                        statusArea.append(selectedTask + " Progress: " + i + "%\n");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task.");
            }
        });

        completeButton.addActionListener(e -> {
            String selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                taskProgress.put(selectedTask, 100);
                statusArea.append(selectedTask + " Completed\n");
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task.");
            }
        });

        deleteButton.addActionListener(e -> {
            String selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                tasks.remove(selectedTask);
                taskProgress.remove(selectedTask);
                taskListModel.removeElement(selectedTask);
                statusArea.append("Task Deleted: " + selectedTask + "\n");
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task.");
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
