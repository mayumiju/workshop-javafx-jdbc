package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService()); 
			controller.updateTableView(); 
		});
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			//a partir do objeto loader vc pode carregar a view e acessar o controller(referência para o controller dessa view)
			controller.setDepartmenteService(new DepartmentService()); //Injetar a dependência do service no controller
			controller.updateTableView(); //Com o controller já com depedência é só chamar o método par atualizar os dados na table view
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			// Método para mostrar a view dentro da janela principal
			// Para trabalhar com a janela principal é necessário uma referência da cena
			Scene mainScene = Main.getMainScene();

			/*
			 * Pq precisou de 1 referência pra cena? ScrollPane > content > VBox > children
			 * A ideia é acrescentar em children de Vbox os childrens do VBox da janela
			 * 'About' É necessário uma referência ao VBox da janela principal
			 */

			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			// Método getRoot() pega o primeiro elemento da View(que é o scrollpane)
			// Método getContent() acessa o 'content' que tá abaixo da hierarquia de
			// ScrollPane
			// 'content' já é uma referência pra o que está dentro do ScrollPane(como o
			// VBox), por isso o casting '(VBox)'

			Node mainMenu = mainVBox.getChildren().get(0); // Primeiro filho do VBox da janela principal
			mainVBox.getChildren().clear(); // Limpa todos os filhos do mainVBox
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
