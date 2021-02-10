package si.horvie.jmssender;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import si.horvie.jmssender.internal.JmsSender;

public class JmsSenderController {
	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
	private static final String NL = System.lineSeparator();

	private static final String INIT = "log.init";
	private static final String SUCCESS = "log.success";
	private static final String ERROR = "log.error";

	private final ResourceBundle res;

	private final String settings;

	@FXML
	private TextField jmsAddress;

	@FXML
	private TextField jmsPort;

	@FXML
	private TextField jmsUser;

	@FXML
	private TextField jmsPassword;

	@FXML
	private TextField jmsQueue;

	@FXML
	private TextArea jmsMessage;

	@FXML
	private TextArea jmsProps;

	@FXML
	private Button send;

	@FXML
	private Button clear;

	@FXML
	private TextArea log;

	public JmsSenderController(ResourceBundle res) {
		this.res = res;
		this.settings = System.getProperty("java.io.tmpdir") + File.separator + "jmsSender.settings";
	}

	public void initialize() {
		log.setText(now() + res.getString(INIT));

		this.loadPreviousSettings();

		send.setOnAction(e -> {
			Optional<String> response = new JmsSender(jmsAddress.getText(), jmsPort.getText(),
					jmsUser.getText(), jmsPassword.getText())
							.send(jmsQueue.getText(), jmsMessage.getText(), jmsProps.getText());

			log.appendText(NL);
			response.ifPresentOrElse(s -> log.appendText(now() + res.getString(ERROR) + ": " + s),
					() -> log.appendText(now() + res.getString(SUCCESS)));
		});

		clear.setOnAction(e -> {
			jmsAddress.clear();
			jmsPort.clear();
			jmsUser.clear();
			jmsPassword.clear();
			jmsQueue.clear();
			jmsMessage.clear();
			jmsProps.clear();
		});
	}

	private String now() {
		return DF.format(LocalDateTime.now()) + "    ";
	}

	public void saveCurrentSettings() {
		String serialized = jmsAddress.getText() + NL
				+ jmsPort.getText() + NL
				+ jmsUser.getText() + NL
				+ jmsPassword.getText() + NL
				+ jmsQueue.getText() + NL
				+ jmsProps.getText() + NL;

		try {
			Path path = Paths.get(settings);
			Files.writeString(path, serialized);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void loadPreviousSettings() {
		Path path = Paths.get(settings);
		if (path.toFile().exists()) {
			try {
				List<String> lines = Files.readAllLines(path);
				jmsAddress.setText(lines.get(0));
				jmsPort.setText(lines.get(1));
				jmsUser.setText(lines.get(2));
				jmsPassword.setText(lines.get(3));
				jmsQueue.setText(lines.get(4));
				jmsProps.setText(String.join(NL, lines.subList(5, lines.size())));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
