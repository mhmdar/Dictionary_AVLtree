//				Department of Computer Science
//						DATA STRUCTURES
//	COMP232/242/2321 (First Semester – Fall 2016/2017)
//	
//	==================================================
//						Project # 3
//	--------------------------------------------------
//	Due Date: 31/12/2016
//	
//	Write a project that allows the user to read words and their meanings (more than one) from file
//	and store them in AVL tree and then print the tree inorder, postorder, and preorder, and also
//	print the result to file in the form of dictionary
//	A:
//	..........
//	B:
//	..........
//	and so on, and then search for a word and it's all meanings and if the word is not found add it to
//	the tree then draw the tree in java graphics and finally ask the user if he want to delete any word.
//	
//	Good luck!


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

public class _Main extends Application {

	static Pane rootPane = new Pane();
	static Scene scene = new Scene(rootPane,1000,650);
	static AVLtree tree;
	static Button buttonExit = new Button("      Exit     ");
	static Button readButton = new Button("   Read Dictionary file   ");
	static TextField searchTextField = new TextField();
	static Button searchButton = new Button(" Search / insert ");
	static Button printButton = new Button(" Print words ");
	static Button writeFileButton = new Button(" Write on file ");
	static int posX=500, posY=150;

	public static void main(String[] args) {
		System.out.println("Starting JavaFX GUI..");
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Dictionary implementation on AVL Tree ADT");
		primaryStage.setScene(scene);
		primaryStage.setX(0);
		primaryStage.setY(0);
		primaryStage.show();
		
		
		buttonExit.setLayoutX(920);
		buttonExit.setLayoutY(610);
		buttonExit.setOnAction(e -> System.exit(0));
		
		readButton.setLayoutX(20);
		readButton.setLayoutY(20);
		readButton.setOnAction(e -> {
			tree = Controller.readFile(primaryStage);
			rootPane.getChildren().clear();
			rootPane.getChildren().addAll(readButton, searchTextField, searchButton, printButton, writeFileButton, buttonExit);
			Controller.drawTree(rootPane, posX, posY, tree);
			searchTextField.setDisable(false);
			searchButton.setDisable(false);
			printButton.setDisable(false);
			writeFileButton.setDisable(false);
		});
		
		searchTextField.setLayoutX(280);
		searchTextField.setLayoutY(20);
		searchTextField.setMaxWidth(120);
		searchTextField.setDisable(true);
		searchTextField.setOnAction(e -> searchButton.fire());
		
		searchButton.setLayoutX(410);
		searchButton.setLayoutY(20);
		searchButton.setDisable(true);
		searchButton.setOnAction(e -> {
			Controller.searchAndInsert(searchTextField.getText(), tree, rootPane);
			rootPane.getChildren().clear();
			rootPane.getChildren().addAll(readButton, searchTextField, searchButton, printButton, writeFileButton, buttonExit, Controller.textArea);
			Controller.drawTree(rootPane, posX, posY, tree);
		});
		
		printButton.setLayoutX(850);
		printButton.setLayoutY(20);
		printButton.setDisable(true);
		printButton.setOnAction(e -> {
			Controller.printWords(tree, rootPane);
		});
		
		writeFileButton.setLayoutX(850);
		writeFileButton.setLayoutY(60);
		writeFileButton.setDisable(true);
		writeFileButton.setOnAction(e -> {
			Controller.writeOnFile(primaryStage, tree);
		});
		
		rootPane.setOnMouseClicked(e -> {
			if (e.getButton()==MouseButton.PRIMARY){
				Controller.contextMenu.hide();
				rootPane.getChildren().remove(Controller.textArea);
			}
		});
		
		rootPane.getChildren().addAll(readButton, searchTextField, searchButton, printButton, writeFileButton, buttonExit);
		
	}
	
}

