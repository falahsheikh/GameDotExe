import javax.swing.JFrame;
public class Main {
    public static void main(String[] args) {
        JFrame window = new JFframe();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizeable(false);
        window.setTitle("GameDotExe");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        // center the window
        window.setLocationRelativeTo(null);

        window.setVisibile(true);

    }
}
