
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
	
	static ContextMenu contextMenu = new ContextMenu();
	static MenuItem showMeanings = new MenuItem("Show word meanings");
	static MenuItem addMeaning = new MenuItem("Add a new meaning");
	static MenuItem delete = new MenuItem("Delete word");
	static TextArea textArea = new TextArea();
	static int posX=_Main.posX, posY=_Main.posY;

	
//	----------| readFile |------------------------------------------------
	@SuppressWarnings("finally")
	public static AVLtree readFile(Stage stage){
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extensionFilter);
		fileChooser.setInitialDirectory(new File(".\\src\\IO"));
		File file = fileChooser.showOpenDialog(stage);
		Scanner input     = null;
		String  str       = null;
		AVLtree tree = new AVLtree();
		try{
			input = new Scanner(file,"UNICODE");	// input file must be stored in UNICODE format
			AVLnode word=null;
			while(input.hasNextLine()){
				str = input.nextLine();
				if(!str.isEmpty()){		// if the reading reaches an empty line, it will end reading.
//					System.out.println(str);
					String[] s = str.split("\t");
					for (String element : s){
						if (element.contains(":")){
							element = element.replace(":", "");
							word = tree.find(element);
							if (word == null){
								tree.insert(element);
								word = tree.find(element);
							}
						}
						else {
							word.meanings.addFirst(element);
						}
					}
				}
			}
		} catch(FileNotFoundException e){
			System.out.println("The file chosen cannot be read");
			e.printStackTrace();
		} catch(Exception e){
			System.out.println("Some error has occurred, please choose a proper file..");
			e.getMessage();
		} finally {
			if (file != null)
				input.close();
			return tree;
		}
	}

//	----------| search/insert |-------------------------------------------
	
	public static AVLnode searchAndInsert(String s, AVLtree T, Pane pane){
//		if (s == "")
//			return null;
		s = s.trim();
		AVLnode n = T.find(s);
		
		if (n != null){
			String str = n.data + ":\n" + n.meanings.print();
			textArea.setText(str);
			textArea.setLayoutX(530);
			textArea.setLayoutY(20);
			textArea.setPrefColumnCount(7);
			textArea.setPrefRowCount(6);
			textArea.setEditable(false);
			textArea.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
			pane.getChildren().remove(textArea);
			pane.getChildren().add(textArea);
		}
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Word was not found.");
			alert.setContentText("Do you want to add it as a new word?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				T.insert(s);
				n = T.find(s);
				insertMeaning(s, n);
			}
		}
		return n;
	}
	
	public static void insertMeaning(String s, AVLnode n){
		TextInputDialog dialog = new TextInputDialog(null);
		dialog.setTitle("Text Input Dialog");
		dialog.setHeaderText("Please insert a new meaning");
		dialog.setContentText("\""+s+"\"");

		// Traditional way to get the response value:
		Optional<String> result2 = dialog.showAndWait();
		if (result2.isPresent() && result2.get() != null){
			n.meanings.addFirst(result2.get());
			System.out.println(result2.get()+" has been inserted as a new meaning of "+n.data);
		}
		// The Java 8 way to get the response value (with lambda expression).
		// result2.ifPresent(name -> System.out.println("Your name: " + name));
	}

//	----| drawTree |------------------------------------------------------------------------
	
	public static void drawTree(Pane rootPane, int posX, int posY, AVLtree tree){
		if (tree.root == null)
			return;
		int offsetX=7*( 2 << tree.getHeight() ), offsetY=70, radius = 25;
		drawTree(tree, tree.root, rootPane, posX, posY , offsetX , offsetY, radius);
	}
	
	private static void drawTree(AVLtree tree, AVLnode word, Pane pane, int posX, int posY, int offsetX, int offsetY, int radius){
		Circle circle = new Circle(posX, posY, radius, Color.YELLOW);
		circle.setOnMouseClicked(e -> {
			if (e.getButton()==MouseButton.SECONDARY)
				contextMenu(pane, tree, word, e.getScreenX(), e.getScreenY());
				});
		Label label = new Label(word.data);
		label.setOnMouseClicked(e -> {
			if (e.getButton()==MouseButton.SECONDARY)
				contextMenu(pane, tree, word, e.getScreenX(), e.getScreenY());
		});
//		Another way to perform the event action for the circle:
//		circle.setOnMousePressed(new EventHandler<MouseEvent>() {
//		    public void handle(MouseEvent mouseEvent) {
//		    	if (mouseEvent.isSecondaryButtonDown())
//		    		Controller.contextMenu(Main.rootPane, word, mouseEvent.getScreenX(), mouseEvent.getScreenY());
//		    }
//		});
//		label.setOnMousePressed(new EventHandler<MouseEvent>() {
//		    public void handle(MouseEvent mouseEvent) {
//		    	if (mouseEvent.isSecondaryButtonDown())
//		    		Controller.contextMenu(Main.rootPane, word, mouseEvent.getScreenX(), mouseEvent.getScreenY());
//		    }
//		});
		label.setLayoutX(posX-radius);
		label.setLayoutY(posY-radius/2);
		pane.getChildren().addAll(circle, label);
		if (word.left != null){
			Line line = new Line(posX-radius/2, posY+radius/2, posX-offsetX, posY+offsetY);
			line.setStrokeWidth(2);
			pane.getChildren().add(line);
			drawTree(tree, word.left, pane, posX-offsetX, posY+offsetY,  offsetX/2 , offsetY, radius);
		}
		if (word.right != null){
			Line line = new Line(posX+radius/2, posY+radius/2, posX+offsetX, posY+offsetY);
			line.setStrokeWidth(2);
			pane.getChildren().add(line);
			drawTree(tree, word.right, pane, posX+offsetX, posY+offsetY, offsetX/2 , offsetY, radius);
		}
	}
	
//	----------| contextMenu |---------------------------------------------
	
	public static void contextMenu(Pane pane, AVLtree tree, AVLnode word, double clickX, double clickY){
		contextMenu.setHideOnEscape(true);
		contextMenu.setAutoHide(true);
		contextMenu.getItems().addAll(showMeanings, addMeaning, delete);
		contextMenu.show(pane, clickX, clickY);
		System.out.println("menu for "+word.data);
		showMeanings.setOnAction(e -> {
			System.out.println("Show meaning of "+word.data);
			textArea.setText(null);
			String s = word.meanings.print();;
			System.out.println("---------------\nPreorder:\n"+s+"---------------");
			textArea.appendText(s);
			textArea.setLayoutX(clickX-20);
			textArea.setLayoutY(clickY-40);
			textArea.setPrefColumnCount(7);
			textArea.setPrefRowCount(6);
			textArea.setEditable(false);
			textArea.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
			pane.getChildren().remove(textArea);
			pane.getChildren().add(textArea);
		});
//		addMeaning.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				System.out.println("Add meaning...");
//			}
//		});
		addMeaning.setOnAction(e -> {
			System.out.println("add meaning for "+word.data);
			insertMeaning(word.data, word);
		});
		delete.setOnAction(e -> {
			System.out.println("Deleting word "+word.data);
			tree.delete(word.data);
			pane.getChildren().clear();
			pane.getChildren().addAll(_Main.readButton, _Main.searchTextField, _Main.searchButton, _Main.printButton, _Main.writeFileButton, _Main.buttonExit);
			drawTree(pane, posX, posY, tree);
		});
	}
	
	public static void printWords(AVLtree tree, Pane pane){
		if (tree.root != null){
			String str = "Preorder:\n-------------\n" + tree.printPreorder() + 
					"\n________________________________\n" + "Inorder:\n-------------\n" + tree.printInorder() + 
					"\n________________________________\n" + "Postorder:\n-------------\n" + tree.printPostorder();
			textArea.setText(str);
			textArea.setLayoutX(530);
			textArea.setLayoutY(20);
			textArea.setPrefColumnCount(25);
			textArea.setPrefRowCount(6);
			textArea.setEditable(false);
			textArea.setWrapText(true);
			textArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
			pane.getChildren().remove(textArea);
			pane.getChildren().add(textArea);
		}
	}
	
	public static void writeOnFile(Stage stage, AVLtree tree){
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extensionFilter);
		fileChooser.setInitialDirectory(new File(".\\src\\IO"));
		File fileName = fileChooser.showSaveDialog(stage);
		if(fileName != null){
			BufferedWriter buff = null;
			try{
				buff = new BufferedWriter(new FileWriter(fileName));
				tree.sendToFileInorderWithMeanings(tree, buff);
				buff.flush();
			} catch(IOException e){
				System.out.println("Cannot write on the file");
				e.printStackTrace();
			} catch(Exception e){
				System.out.println("Some error has occurred");
				e.getMessage();
			}
			finally {
				try {
					buff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Runtime runtime = Runtime.getRuntime();
     		try {
				runtime.exec("C:\\Windows\\notepad.exe "+ fileName.getAbsolutePath());
			} catch (IOException e1) {
				System.out.println("Some error has occurred");
				textArea.setText("\n\n\n\n\n>> Some ERROR has occurred");
				e1.printStackTrace();
			}
		}
	}
}
