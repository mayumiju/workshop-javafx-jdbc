package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{

	//Declara dependência do serviço Department no controlador para carregar os departamentos e mostrar na TableView
	private DepartmentService service;
	//"private DepartmentService service = new DepartmentService()" é ruim, pois é um acoplamento forte
	
	@FXML
	private TableView<Department> tableViewDepartments;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	//Mecanismo para injetar a dependência sem usar a implementação da classe
	public void setDepartmenteService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		//Padrão de método para inicialização das colunas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//Implementação para a TableView acompanhar o tamanho da janela
		Stage stage = (Stage) Main.getMainScene().getWindow(); //Window é uma superclasse do stage, entao precisa de downcasting
		tableViewDepartments.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list); //Instancia a obsList
		tableViewDepartments.setItems(obsList);
	}
}
