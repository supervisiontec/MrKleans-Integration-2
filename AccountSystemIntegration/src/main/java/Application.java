
import gui.FingerprintSyncGUI;
import gui.SystemTrayUtil;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author 'Kasun Chamara'
 */
public class Application {
//static Logger logger = LogManager.getLogger(FingerprintSync.class.getName());

    private static FingerprintSyncGUI fingerprintSyncGUI = null;

    public static void main(String[] args) throws SQLException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            fingerprintSyncGUI = new FingerprintSyncGUI();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FingerprintSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        SystemTrayUtil.getInstance().initSystemTray(fingerprintSyncGUI);
    }
}
