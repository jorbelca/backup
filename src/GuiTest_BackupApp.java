import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;



//ANTES DE EJECUTAR TESTS = export APP_ENV="development"
public class GuiTest_BackupApp {

    public static void main(String[] args) throws AWTException, InterruptedException {
        // Inicializamos la GUI (este es el método principal de la app que lanza la
        // ventana)
        SwingUtilities.invokeLater(() -> {
            try {
                new BackupApp();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Espera para que la GUI esté completamente cargada
        Thread.sleep(2000);
        JFrame frame = BackupApp.getFrame();

        if (frame != null) {
            // Depuración: Imprimir todos los botones disponibles
            ArrayList<JButton> buttons = findAllButtons(frame.getContentPane());
            for (JButton button : buttons) {
                System.out.println("Botón encontrado: " + button.getText());
            }

            // Aquí podemos buscar el botón "Test Backup" y realizar acciones sobre él
            JButton testBackupButton = findButtonByText(frame.getContentPane(), "Test Backup");
            if (testBackupButton != null) {
                System.out.println("Botón 'Test Backup' encontrado");

                // Ejecutar el clic en otro bloque para liberar el EDT
                SwingUtilities.invokeLater(() -> {
                    testBackupButton.doClick(); // Aquí se ejecuta el clic
                });

                // Verificar el resultado después de hacer clic
                String message = getJOptionPaneMessage();
                if (message != null && message.equals("El test del backup se completó exitosamente.")) {
                    System.out.println("Prueba de 'Test Backup' completada exitosamente.");
                } else {
                    System.err.println("Error en la prueba del botón 'Test Backup'. Mensaje: " + message);
                }
            } else {
                System.err.println("Botón 'Test Backup' no encontrado");
            }
        } else {
            System.err.println("No se pudo obtener el JFrame");
        }

        // Cierra la ventana de la aplicación después de la prueba
        SwingUtilities.invokeLater(() -> {
            if (frame != null) {
                frame.dispose(); // Cierra la ventana de la GUI
            }
        });

        // Salir de la aplicación
        System.exit(0);

        System.out.println("Prueba de GUI con Robot completada");
    }

    // Método para encontrar todos los botones en la jerarquía de la ventana
    private static ArrayList<JButton> findAllButtons(Container container) {
        ArrayList<JButton> buttons = new ArrayList<>();
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                buttons.add((JButton) component);
            } else if (component instanceof Container) {
                buttons.addAll(findAllButtons((Container) component)); // Buscar en sub-contenedores
            }
        }
        return buttons;
    }

    // Método para encontrar un botón por su texto
    private static JButton findButtonByText(Container container, String text) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals(text)) {
                    return button;
                }
            } else if (component instanceof Container) {
                JButton button = findButtonByText((Container) component, text); // Buscar en sub-contenedores
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }

    // Método para capturar el mensaje mostrado en el JOptionPane (MessageDialog)
    private static String getJOptionPaneMessage() throws InterruptedException {
        int retries = 10; // Intentos para verificar si aparece el JOptionPane
        while (retries-- > 0) {
            for (Window window : Window.getWindows()) {
                if (window instanceof JDialog) {
                    JDialog dialog = (JDialog) window;
                    for (Component component : dialog.getContentPane().getComponents()) {
                        if (component instanceof JOptionPane) {
                            JOptionPane optionPane = (JOptionPane) component;
                            Object message = optionPane.getMessage();
                            if (message instanceof String) {
                                // Verificar si el mensaje es el correcto
                                if (message.equals("El test del backup se completó exitosamente.")) {
                                    System.out.println("Mensaje correcto: " + message);

                                    // Simular clic en el botón "Aceptar" si el mensaje es correcto
                                    clickOkButton(optionPane);

                                }
                                return (String) message;

                            }
                        }
                    }
                }
            }
            // Espera un poco antes de reintentar
            Thread.sleep(500);
        }
        return null; // Si no encuentra el JOptionPane
    }

    // Método para encontrar el botón "Aceptar" en el JOptionPane y hacer clic
    private static void clickOkButton(JOptionPane optionPane) {
        SwingUtilities.invokeLater(() -> {
            for (Component component : optionPane.getComponents()) {
                if (component instanceof JPanel) {
                    JPanel panel = (JPanel) component;
                    for (Component buttonComponent : panel.getComponents()) {
                        if (buttonComponent instanceof JButton) {
                            JButton button = (JButton) buttonComponent;
                            if (button.getText().equals("Aceptar") || button.getText().equals("OK")) {
                                // Simular clic en el botón "Aceptar" para cerrar la ventana
                                button.doClick();
                                return;
                            }
                        }
                    }
                }
            }
        });
    }
}