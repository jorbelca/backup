import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class BackupApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Backup a iCloud");
        JButton button = new JButton("Ejecutar Backup");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command("/ruta/al/script/backup.sh");
                try {
                    Process process = processBuilder.start();
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        JOptionPane.showMessageDialog(frame, "Backup completado exitosamente!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "El backup falló con código de salida: " + exitCode);
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error al ejecutar el backup.");
                }
            }
        });

        frame.getContentPane().add(button);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}