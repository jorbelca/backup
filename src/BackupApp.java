import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

// cd ~/Library/Mobile\ Documents/com~apple~CloudDocs
public class BackupApp {
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
        String basePath = new File("").getAbsolutePath();
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

        // Acción al pulsar el botón
        selectFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear el JFileChooser configurado para seleccionar directorios
                JFileChooser folderChooser = new JFileChooser();
                folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                folderChooser.setDialogTitle("Selecciona la carpeta para hacer backup");

                // Mostrar el diálogo y capturar la selección del usuario
                int result = folderChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = folderChooser.getSelectedFile();
                    folderPathField.setText(selectedFolder.getAbsolutePath());
                    // Aquí podrías hacer algo con la ruta seleccionada, como iniciar el backup
                    String path = "Carpeta seleccionada: " + selectedFolder.getAbsolutePath();

                    frame.add(new JLabel(path), BorderLayout.CENTER);
                }
            }
        });
        // Crear un botón para iniciar el backup
        JButton backupButton = new JButton("Iniciar Backup");
        frame.add(backupButton, BorderLayout.SOUTH);

        backupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String folderPath = folderPathField.getText();
                if (!folderPath.isEmpty()) {
                    // Aquí puedes iniciar el proceso de backup con la carpeta seleccionada
                    JOptionPane.showMessageDialog(frame, "Iniciando backup para: " + folderPath);
                    // Llamar al método que realiza el backup
                    // backup(folderPath);
                } else {
                    JOptionPane.showMessageDialog(frame, "Por favor, selecciona una carpeta primero.");
                }
            }
        });

        // Crear un botón para testear el backup
        JButton testButton = new JButton("Test Backup");
        frame.add(testButton, BorderLayout.EAST);

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
        frame.setVisible(true);
    }

    public static boolean testBackup() {
        String basePath = new File("").getAbsolutePath();
        try {
            // Comando para ejecutar el script bash
            String[] command = { "/bin/bash", "-c", basePath + "/../src/bash/backup_test.sh" };

            // Ejecutar el comando
            Process process = Runtime.getRuntime().exec(command);

            // Leer la salida del comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();

            // Verificar el resultado
            if ("true".equals(result)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}