
import gui.SystemIntegrationSyncGUI;
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

    private static SystemIntegrationSyncGUI systemIntegrationSyncGUI = null;

    public static void main(String[] args) throws SQLException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            systemIntegrationSyncGUI = new SystemIntegrationSyncGUI();
            systemIntegrationSyncGUI.setVisible(true);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SystemIntegrationSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        SystemTrayUtil.getInstance().initSystemTray(systemIntegrationSyncGUI);
    }
}
