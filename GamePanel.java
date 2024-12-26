import javax.swing.JPanel;
public class GamePanel extends JPanel {
    // screen settings
    final int originalTitleSize = 16 // 16x16
    final int scale = 3; // for characters
    final int titleSize = originalTitleSize * titleSize;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = titleSize * maxScreenCol;
    final int screenHeight = titleSize * maxScreenRow;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.green);
        this.setDoubleBuffered(true);
    }



}
