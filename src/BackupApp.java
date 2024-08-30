import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

public class BackupApp {
    public static String basePath = new File("").getAbsolutePath();

    private static boolean isBackupRunning = false; // Bandera para controlar el estado del backup

    private static boolean stopRequested = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Backup a iCloud");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        JTextField folderPathField = new JTextField();
        folderPathField.setEditable(false); // El campo no es editable manualmente
        frame.add(folderPathField, BorderLayout.CENTER);

        JButton selectFolderButton;
        // Comprobar que el icono existe.
        String iconPath = basePath + "/../icons/folder-open-regular.png";

        File ficon = new File(iconPath);
        if (ficon.exists()) {
            ImageIcon originalIcon = new ImageIcon(iconPath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon folderIcon = new ImageIcon(scaledImage);
            selectFolderButton = new JButton(folderIcon);
        } else {
            System.err.println("El ícono no fue encontrado.");
            selectFolderButton = new JButton("Seleccionar Carpeta");
        }

        // Crear un botón para abrir el JFileChooser
        frame.add(selectFolderButton, BorderLayout.NORTH);

        // Añadir el panel para los JCheckBox
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(2, 1)); // 2 filas, 1 columna

        JCheckBox lastThreeDaysCheckBox = new JCheckBox("Mantener los últimos 3 días");
        JCheckBox lastWeekCheckBox = new JCheckBox("Mantener la última semana");

        checkBoxPanel.add(lastThreeDaysCheckBox);
        checkBoxPanel.add(lastWeekCheckBox);

        frame.add(checkBoxPanel, BorderLayout.WEST); // Añadir el panel a la izquierda

        // CARPETA SELECTOR
        selectFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser folderChooser = new JFileChooser();
                folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                folderChooser.setDialogTitle("Selecciona la carpeta para hacer backup");

                int result = folderChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = folderChooser.getSelectedFile();
                    folderPathField.setText(selectedFolder.getAbsolutePath());
                }
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        // Crear un botón para iniciar el backup
        JButton backupButton = new JButton("Iniciar Backup");
        backupButton.setForeground(Color.green);
        // Crear un botón para testear el backup
        JButton testButton = new JButton("Test Backup");
        testButton.setForeground(Color.gray);
        JButton stopButton = new JButton("Parar Backup");
        stopButton.setForeground(Color.red);
        // Añadir los tres botones al final del GUI
        buttonPanel.add(backupButton);
        buttonPanel.add(testButton);
        buttonPanel.add(stopButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        backupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String folderPath = folderPathField.getText();
                if (!folderPath.isEmpty()) {
                    isBackupRunning = true;
                    stopRequested = false;
                    new Thread(() -> {
                        while (isBackupRunning && !stopRequested) {
                            boolean keepLastThreeDays = lastThreeDaysCheckBox.isSelected();
                            boolean keepLastWeek = lastWeekCheckBox.isSelected();

                            if (backup(folderPath, keepLastThreeDays, keepLastWeek)) {
                                JOptionPane.showMessageDialog(null, "El backup se completó exitosamente.");
                            } else {
                                JOptionPane.showMessageDialog(null, "El backup falló.");
                            }

                            try {
                                Thread.sleep(86400000); // Espera 24 horas antes de la siguiente ejecución
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    JOptionPane.showMessageDialog(frame, "Por favor, selecciona una carpeta primero.");
                }
            }
        });

        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (testBackup()) {
                    JOptionPane.showMessageDialog(null, "El test del backup se completó exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "El test del backup falló.");
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isBackupRunning) {
                    stopBackup();
                    JOptionPane.showMessageDialog(frame, "Deteniendo backup...");
                } else {
                    JOptionPane.showMessageDialog(frame, "No hay un backup en ejecución para detener.");
                }
            }
        });

        frame.setVisible(true);
    }

    public static boolean testBackup() {

        try {
            // Comando para ejecutar el script bash
            String[] command = { "/bin/bash", "-c", basePath + "/../src/bash/backup_test.sh" };

            // Ejecutar el comando
            Process process = Runtime.getRuntime().exec(command);

            // Leer la salida del comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            return "true".equals(result);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean backup(String folderPath, boolean keepLastThreeDays, boolean keepLastWeek) {

        try {

            // Convertir booleanos a strings para pasar como parámetros
            String keepThreeDays = keepLastThreeDays ? "true" : "false";
            String keepWeek = keepLastWeek ? "true" : "false";

            // Comando para ejecutar el script bash
            String[] command = { "/bin/bash", "-c", basePath + "/../src/bash/backup_main.sh " + folderPath,
                    keepThreeDays,
                    keepWeek };

            // Ejecutar el comando
            Process process = Runtime.getRuntime().exec(command);
            // Leer la salida estándar
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Salida estándar: " + line);
            }

            // Leer la salida de errores
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("Error: " + errorLine);
            }

            int exitCode = process.waitFor(); // Espera a que el proceso termine
            return exitCode == 0; // True si el proceso terminó con éxito
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void stopBackup() {
        try {
            String stopSignalPath = basePath + "/../src/bash/stop_signal.sh";
            if (!Files.exists(Paths.get(stopSignalPath))) {
                Files.createFile(Paths.get(stopSignalPath));
            }
            Files.deleteIfExists(Paths.get(stopSignalPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
