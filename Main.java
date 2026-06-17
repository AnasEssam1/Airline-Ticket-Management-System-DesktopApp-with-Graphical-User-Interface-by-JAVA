import service.AirlineService;
import ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirlineService service = new AirlineService();
            LoginFrame loginFrame = new LoginFrame(service);
            loginFrame.setVisible(true);
        });
    }
}
