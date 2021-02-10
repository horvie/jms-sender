package si.horvie.jmssender;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JmsSenderFX extends Application {
	private static final String FXML = "app.fxml";
	private static final String LANG_SL = "sl";
	private static final String TITLE = "window.title";

	@Override
	public void start(Stage stage) throws IOException {
		ResourceBundle resBundle = ResourceBundle.getBundle("app", getLocale());
		JmsSenderController controller = new JmsSenderController(resBundle);
		FXMLLoader loader = new FXMLLoader(JmsSenderFX.class.getResource(FXML), resBundle);
		loader.setController(controller);

		Scene scene = new Scene(loader.load());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle(resBundle.getString(TITLE));

		stage.setOnCloseRequest(e -> controller.saveCurrentSettings());

		stage.show();
	}

	private Locale getLocale() {
		Locale locale = Locale.getDefault();
		if (!locale.getLanguage().equalsIgnoreCase(LANG_SL)) {
			locale = Locale.ENGLISH;
		}
		return locale;
	}

	public static void main(String[] args) {
		launch(args);
	}

}