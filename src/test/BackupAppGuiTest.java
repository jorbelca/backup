package test;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class BackupAppGuiTest {

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();

        // Simula un clic en las coordenadas (x, y) de la pantalla
        robot.mouseMove(100, 200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        // Puedes agregar más interacciones, como escribir texto o presionar teclas
    }
}