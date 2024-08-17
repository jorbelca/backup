import javax.swing.*;

public class SimpleFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}