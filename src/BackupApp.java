import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;

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

        File ficon = new File("/Users/k/Documents/backup/icons/folder-open-regular.png");
        if (ficon.exists()) {
            System.out.println("El ícono fue encontrado.");
            ImageIcon originalIcon = new ImageIcon("/Users/k/Documents/backup/icons/folder-open-regular.png");
            // Redimensionar el icono al tamaño deseado (por ejemplo, 24x24 píxeles)
            Image scaledImage = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon folderIcon = new ImageIcon(scaledImage);
            selectFolderButton = new JButton(folderIcon);
        } else {
            System.out.println("El ícono no fue encontrado.");
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
        // Crear un botón para iniciar el backup (opcional)
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

        frame.setVisible(true);
    }
}