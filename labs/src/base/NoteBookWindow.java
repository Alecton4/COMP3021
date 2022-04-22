package base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * NoteBook GUI with JAVAFX
 *
 * COMP 3021
 *
 *
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titlesListView = new ListView<String>();
	/**
	 *
	 * Combobox for selecting the folder
	 *
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";
	/**
	 * current note selected by the user
	 */
	String currentNote = "";

	Stage stage;

	File currentFile = null;

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	/**
	 * This create the top section
	 *
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load");
		buttonLoad.setPrefSize(100, 20);
		// buttonLoad.setDisable(true);
		Button buttonSave = new Button("Save");
		buttonSave.setPrefSize(100, 20);
		// buttonSave.setDisable(true);

		Label labelSearch = new Label("Search: ");
		TextField fieldSearch = new TextField();
		Button buttonSearch = new Button("Search");
		buttonSave.setPrefSize(100, 20);
		Button buttonClearSearch = new Button("Clear Search");
		buttonSave.setPrefSize(100, 20);

		hbox.getChildren().addAll(buttonLoad, buttonSave, labelSearch, fieldSearch, buttonSearch, buttonClearSearch);

		// REVIEW: lab08
		buttonLoad.setOnAction((event) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object!");

			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)",
					"*.ser");
			fileChooser.getExtensionFilters().add(extFilter);

			File file = fileChooser.showOpenDialog(stage);

			if (file != null) {
				// load the file
				currentFile = file;
				loadNoteBook(file);
			} else {
				System.out.println("file is null");
			}
		});

		// REVIEW: lab08
		buttonSave.setOnAction((event) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object!");

			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)",
					"*.ser");
			fileChooser.getExtensionFilters().add(extFilter);

			File file = fileChooser.showOpenDialog(stage);

			if (file != null) {
				// load the file
				currentFile = file;
				if (currentFile != null) {
					noteBook.save(currentFile.getAbsolutePath());

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Successfully saved");
					alert.setContentText("You file has been saved to file " + currentFile.getName());
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});

				}
			} else {
				System.out.println("file is null");
			}
		});

		// REVIEW: lab07
		buttonSearch.setOnAction((event) -> {
			currentSearch = fieldSearch.getText();
			textAreaNote.setText("");
			updateListView(currentSearch);
		});

		// REVIEW: lab07
		buttonClearSearch.setOnAction((event) -> {
			currentSearch = "";
			fieldSearch.setText("");
			textAreaNote.setText("");
			updateListView();
		});

		return hbox;
	}

	/**
	 * this create the section on the left
	 *
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		HBox hbox = new HBox();
		// hbox.setPadding(new Insets(12, 5, 12, 5));
		hbox.setSpacing(10);

		Button buttonAdd = new Button("Add a Folder");
		buttonAdd.setPrefSize(100, 20);

		buttonAdd.setOnAction((event) -> {
			TextInputDialog dialog = new TextInputDialog("Add a Folder");
			dialog.setTitle("Input");
			dialog.setHeaderText("Add a new folder for your notebook:");
			dialog.setContentText("Please enter the name you want to create:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				// REVIEW: lab08
				String newFolder = result.get();
				if (newFolder == "") {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Please input an vaild folder name");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
					return;
				}

				boolean checkIfExist = false;
				for (Folder folder : noteBook.getFolders()) {
					if (folder.equals(new Folder(newFolder))) {
						checkIfExist = true;
					}

				}

				if (checkIfExist) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("You already have a folder named with " + newFolder);
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
					return;
				}

				addFolder(newFolder);
			}
		});

		// REVIEW: This line is a fake folder list. We should display the folders in
		// noteBook variable! Replace this with your implementation
		// REF https://blog.csdn.net/zhangbinlong/article/details/86218758
		// foldersComboBox.getItems().addAll(noteBook.getFolders().stream().map(Folder::getName).collect(Collectors.joining(",")));
		for (Folder folder : noteBook.getFolders()) {
			foldersComboBox.getItems().add(folder.getName());
		}

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// REVIEW update listView
				updateListView();
			}
		});

		foldersComboBox.setValue("-----");

		titlesListView.setPrefHeight(100);

		titlesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				// This is the selected title
				// REVIEW load the content of the selected note in
				// textAreaNote
				String content = "";
				for (Folder folder : noteBook.getFolders()) {
					if (folder.getName().equals(currentFolder)) {
						for (Note note : folder.getNotes()) {
							if (note instanceof TextNote && note.getTitle().equals(title)) {
								content = ((TextNote) note).getContent();
							}
						}
					}
				}
				currentNote = title;
				textAreaNote.setText(content);
			}
		});

		Button buttonAddFolder = new Button("Add a Note");
		buttonAddFolder.setPrefSize(100, 20);

		buttonAddFolder.setOnAction((event) -> {
			if (noteBook == null || currentFolder == null || currentFolder == "" || currentFolder == "-----") {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning");
				alert.setContentText("Please select a folder first");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});
				return;
			}

			TextInputDialog dialog = new TextInputDialog("Add a Note");
			dialog.setTitle("Input");
			dialog.setHeaderText("Add a new Note for your notebook:");
			dialog.setContentText("Please enter the name you want to create:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				// REVIEW: lab08
				String newNote = result.get();

				if (newNote == "") {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Please input an vaild note name");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
					return;
				}

				noteBook.createTextNote(currentFolder, newNote, "");

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Successfully!");
				alert.setContentText("Insert note " + newNote + " to folder " + currentFolder + " successfully");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});

				updateListView();
			}
		});

		hbox.getChildren().addAll(foldersComboBox, buttonAdd);
		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titlesListView);
		vbox.getChildren().add(buttonAddFolder);

		return vbox;
	}

	private void updateListView() {
		ArrayList<String> list = new ArrayList<String>();

		// REVIEW populate the list object with all the TextNote titles of the
		// currentFolder
		for (Folder folder : noteBook.getFolders()) {
			if (folder.getName().equals(currentFolder)) {
				for (Note note : folder.getNotes()) {
					list.add(note.getTitle());
				}
			}
		}

		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titlesListView.setItems(combox2);
		textAreaNote.setText("");
	}

	private void updateListView(String keyword) {
		ArrayList<String> list = new ArrayList<String>();

		// REVIEW populate the list object with all the TextNote titles of the
		// currentFolder
		for (Folder folder : noteBook.getFolders()) {
			if (folder.getName().equals(currentFolder)) {
				for (Note note : folder.searchNotes(keyword)) {
					list.add(note.getTitle());
				}
			}
		}

		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titlesListView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		// 0 0 is the position in the grid

		HBox hbox = new HBox();
		hbox.setSpacing(10);

		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);

		ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
		deleteView.setFitHeight(18);
		deleteView.setFitWidth(18);
		deleteView.setPreserveRatio(true);

		Button buttonSave = new Button("Save Note");
		buttonSave.setPrefSize(100, 20);

		Button buttonDelete = new Button("Delete Note");
		buttonDelete.setPrefSize(100, 20);

		buttonSave.setOnAction((event) -> {
			if (currentFolder == null || currentFolder == "" || currentNote == "" || currentNote == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning");
				alert.setContentText("Please select a folder and a note");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});
				return;
			}

			String content = textAreaNote.getText();
			noteBook.editNote(currentFolder, new Note(currentNote), content);
		});

		buttonDelete.setOnAction((event) -> {
			if (currentFolder == null || currentFolder == "" || currentNote == "" || currentNote == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning");
				alert.setContentText("Please select a folder and a note");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});
				return;
			}

			boolean check = noteBook.removeNote(currentFolder, new Note(currentNote));

			if (check) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Successfully Delete");
				alert.setContentText("Your Note " + currentNote + " has been deleted");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});

				updateListView();
			}
		});

		// hbox.getChildren().addAll(buttonSave, buttonDelete);
		hbox.getChildren().addAll(saveView, buttonSave, deleteView, buttonDelete);
		grid.add(hbox, 0, 0);
		grid.add(textAreaNote, 0, 1);

		return grid;
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendance and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called “the most shocking play in NFL history” and the Washington Redskins dubbed the “Throwback Special”: the November 1985 play in which the Redskins’ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award–winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything—until it wasn’t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant—a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether’s Daddy Was a Number Runner and Dorothy Allison’s Bastard Out of Carolina, Jacqueline Woodson’s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood—the promise and peril of growing up—and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next August");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;
	}

	private void loadNoteBook(File file) {
		noteBook = new NoteBook(file.getAbsolutePath());

		if (noteBook != null) {
			foldersComboBox.getItems().clear();
			List<Folder> folders = noteBook.getFolders();
			for (int i = 0; i < noteBook.getFolders().size(); i++) {
				foldersComboBox.getItems().addAll(folders.get(i).getName());
			}
			foldersComboBox.setValue("-----");
			updateListView();
		} else {
			System.out.println("load fail");
		}
	}

	public void addFolder(String folderName) {
		if (currentFile == null)
			return;
		noteBook.insertFolder(folderName);
		currentFolder = folderName;
		foldersComboBox.getItems().addAll(folderName);
		foldersComboBox.setValue(folderName);

		updateListView();

	}
}
