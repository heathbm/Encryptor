package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.text.rtf.*;
import javax.swing.text.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller {

	@FXML
	private TextArea textField;

	@FXML
	private Button encryptButton;

	@FXML
	private Button decryptButton;

	@FXML
	private Button fileButton;

	@FXML
	private TextField passwordField;

	@FXML
	private Button restoreButton;

	@FXML
	private Button clearButton;

	@FXML
	private Button maxButton;

	@FXML
	private Pane maxView;

	@FXML
	private Label maxLbl;

	List<Integer> passwordValue = new ArrayList<Integer>();
	int currentCharValue = 0;
	String currentText = "";
	String firstText = "";
	File currentFile;
	boolean filePresent = false;
	boolean textPresent = false;

	public void clearMaxFunc() {
		maxButton.setOpacity(0);
		maxView.setOpacity(0);
		maxLbl.setOpacity(0);
	}

	public void restoreFunc() {
		if (!firstText.isEmpty()) {
			textField.setText(firstText);
		}
	}

	public void clearFunc() {
		textField.clear();
		textField.setEditable(true);
		textPresent = false;
		firstText = "";
		fileButton.setText("OPEN");
		currentFile = null;
		filePresent = false;
		passwordField.clear();
	}

	public void fileBoolFunc() {
		if (textField.getText().isEmpty()) {
			fileButton.setText("OPEN");
			textPresent = false;
		} else {
			if (filePresent == true) {
				fileButton.setText("OVERWRITE FILE");
			} else {
				fileButton.setText("SAVE AS");
			}
			textPresent = true;
		}

	}

	public void savePassword() {
		if (!passwordField.getText().isEmpty()) {

			currentText = textField.getText();
			textField.clear();
			passwordValue.clear();
			String password = passwordField.getText();

			for (int i = 0; i < password.length(); i++) {
				int charValue = (int) password.charAt(i);
				passwordValue.add(charValue);
			}
		}
	}

	public boolean canDecrypt() {

		int check = (int) textField.getText().charAt(0);
		if (check < 127) {
			return false;
		} else {
			return true;
		}

	}

	public void decryptFunc() {

		if (canDecrypt() == true) {

			savePassword();

			StringBuilder temp = new StringBuilder();
			int passMax = passwordValue.size();
			int passInc = 0;

			for (int i = 0; i < currentText.length(); i++) {

				int c = (int) currentText.charAt(i) - passwordValue.get(passInc);
				temp.append((char) c);

				passInc++;
				if (passInc == passMax) {
					passInc = 0;
				}

			}

			textField.setText(temp.toString());

			int check = (int) textField.getText().charAt(0);
			if (check < 127) {
				textField.setEditable(true);
			}

		}

	}

	public void encryptFunc() {

		if (!passwordField.getText().isEmpty()) {

			if ((int) textField.getText().charAt(0) > 1800) {
				maxButton.setOpacity(1);
				maxView.setOpacity(1);
				maxLbl.setOpacity(1);
			} else {

				savePassword();

				textField.setEditable(false);
				textPresent = true;

				StringBuilder temp = new StringBuilder();
				int passMax = passwordValue.size();
				int passInc = 0;

				for (int i = 0; i < currentText.length(); i++) {

					int c = (int) currentText.charAt(i) + passwordValue.get(passInc);
					System.out.println("value:" + c);
					temp.append((char) c);

					passInc++;
					if (passInc == passMax) {
						passInc = 0;
					}

				}

				textField.setText(temp.toString());

			}

		}
	}

	public void fileFunc() throws IOException, BadLocationException {
		if (textPresent == true) {
			saveFile();
		} else {
			openFile();
		}
	}

	private void SaveFileAction(String content, File file) {
		try {

			BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF-8"));
			try {
				String ls = System.getProperty("line.separator");
				String text = content;
				content.replaceAll("\n", ls);
				out.write(text);
			} finally {
				out.close();
			}

		} catch (IOException ex) {
			System.out.println("save error");
		}

	}

	public void saveFile() throws IOException {
		if (filePresent == true) { // save

			BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(currentFile.getAbsolutePath()), "UTF-8"));
			try {
				String ls = System.getProperty("line.separator");
				String text = textField.getText().replaceAll("\n", ls);
				out.write(text);
			} finally {
				out.close();
			}

		} else { // save as

			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(extFilter);
			File file = fileChooser.showSaveDialog(textField.getScene().getWindow());

			if (file != null) {
				SaveFileAction(textField.getText(), file);
			}
		}
	}

	public void menuSaveFunc() throws IOException {
		saveFile();
	}

	public void menuSaveAsFunc() {

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(textField.getScene().getWindow());

		if (file != null) {
			SaveFileAction(textField.getText(), file);
		}
	}

	private String getFileExtension(File file) {
		String name = file.getName();
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}

	public void openFile() throws IOException, BadLocationException {
		textField.clear();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt", "*.rtf"));
		currentFile = fileChooser.showOpenDialog(textField.getScene().getWindow());

		if (getFileExtension(currentFile).equals("rtf")) {
			System.out.println("true");
			System.out.println("rtf here");
			RTFEditorKit rtf = new RTFEditorKit();
			Document doc = rtf.createDefaultDocument();

			FileInputStream fis = new FileInputStream(currentFile.getAbsolutePath());
			rtf.read(fis, doc, 0);
			firstText = doc.getText(0, doc.getLength());
			textField.setText(doc.getText(0, doc.getLength()));

			fileButton.setText("SAVE AS");
			textPresent = true;
			filePresent = false;
		} else {

			StringBuilder builder = new StringBuilder();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(new FileInputStream(currentFile.getAbsolutePath()), "UTF-8"));
			String ls = System.getProperty("line.separator");

			try {
				String str = "";
				while ((str = in.readLine()) != null) {
					builder.append(str + ls);
				}

				builder.setLength(builder.length() - 1);

				textField.setText(builder.toString());
				firstText = builder.toString();

			} finally {
				in.close();
			}

			fileButton.setText("OVERWRITE FILE");
			textPresent = true;
			filePresent = true;
		}

		int check = (int) textField.getText().charAt(0);
		if (check > 127) {
			textField.setEditable(false);
		}

	}

	public void menuOpenFunc() throws IOException, BadLocationException {
		openFile();
	}

}
