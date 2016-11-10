package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;


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
	static String IV = "AAAAAAAAAAAAAAAA";
	static String plaintext = "test text 123\0\0\0"; /*Note null padding*/
	static String encryptionKey = "1234ashvmgjrue72";

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
			passwordValue.clear();
			String password = passwordField.getText();
			int passLength = password.length();
			int index = 0;
			
			for (int i = 0; i < passLength * 3; i++) {
				
				int charValue = (int) password.charAt(index) + (i - index) + index;
				passwordValue.add(charValue);
				index++;
				if(index == passLength) {
					index = 0;
				}
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

	public void decryptFunc() throws Exception {

		if (canDecrypt() == true) {
						
			savePassword();

			StringBuilder temp = new StringBuilder();
			int passMax = passwordValue.size();
			int passInc = 0;

			for (int i = 0; i < textField.getText().length(); i++) {

				int c = (int) textField.getText().charAt(i) - passwordValue.get(passInc);
				temp.append((char) c);

				passInc++;
				if (passInc == passMax) {
					passInc = 0;
				}

			}
			
			Controller.setKey(passwordField.getText());

            Controller.decrypt(temp.toString().trim());
			
			textField.setText(Controller.getDecryptedString());			
			
			int check = (int) textField.getText().charAt(0);
			if (check < 127) {
				textField.setEditable(true);
			}

		}

	}

	public void encryptFunc() throws Exception {

		if (!passwordField.getText().isEmpty()) {

			if ((int) textField.getText().charAt(0) > 1800) {
				maxButton.setOpacity(1);
				maxView.setOpacity(1);
				maxLbl.setOpacity(1);
			} else {
								
				Controller.setKey(passwordField.getText());
	               
				Controller.encrypt(textField.getText().trim());
				
				String enc = Controller.getEncryptedString();
								
				savePassword();

				textField.setEditable(false);
				textPresent = true;

				StringBuilder temp = new StringBuilder();
				int passMax = passwordValue.size();
				int passInc = 0;

				for (int i = 0; i < enc.length(); i++) {

					int c = (int) enc.charAt(i) + passwordValue.get(passInc);
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
	
	private static SecretKeySpec secretKey ;
    private static byte[] key ;
    
	private static String decryptedString;
	private static String encryptedString;
 
    
    public static void setKey(String myKey) throws UnsupportedEncodingException{
    	
   
    	MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
	    	key = Arrays.copyOf(key, 16); // use only first 128 bit
		    secretKey = new SecretKeySpec(key, "AES");
		    
		    
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	    	  
	

    }
    
    public static String getDecryptedString() {
		return decryptedString;
	}

	public static void setDecryptedString(String decryptedString) {
		Controller.decryptedString = decryptedString;
	}

    public static String getEncryptedString() {
		return encryptedString;
	}

	public static void setEncryptedString(String encryptedString) {
		Controller.encryptedString = encryptedString;
	}

	public static String encrypt(String strToEncrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
         
            setEncryptedString(Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));
        
        }
        catch (Exception e)
        {
           
            System.out.println("Error while encrypting: "+e.toString());
        }
        return null;

    }

    public static String decrypt(String strToDecrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
           
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            setDecryptedString(new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt))));
            
        }
        catch (Exception e)
        {
         
            System.out.println("Error while decrypting: "+e.toString());

        }
        return null;
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
