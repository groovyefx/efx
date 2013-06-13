package efx.control.table;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 
 * @author lovefree103@gmail.com
 *
 */
@SuppressWarnings("rawtypes")
public class ComboBoxCellFactory {	
	
	public static Callback<TableColumn, TableCell> getComboBoxTableCell(final ObservableList list){
		return new Callback<TableColumn, TableCell>(){

			@Override
			public TableCell call(TableColumn arg0) {
				return new ComboBoxTableCell(list);
			}
			
		};
	}
	
	public static Callback<TableColumn, TableCell> getComboBoxTableCell(final Object...objects){
		return new Callback<TableColumn, TableCell>(){

			@Override
			public TableCell call(TableColumn arg0) {
				return new ComboBoxTableCell(objects);
			}
			
		};
	}
	
	public static Callback<TableColumn, TableCell> getComboBoxTableCell(final StringConverter stringConverter,final Object...objects){
		return new Callback<TableColumn, TableCell>(){

			@Override
			public TableCell call(TableColumn arg0) {
				return new ComboBoxTableCell(stringConverter,objects);
			}
			
		};
	}
	
	public static Callback<TableColumn, TableCell> getComboBoxTableCell(final StringConverter stringConverter,final ObservableList list){
		return new Callback<TableColumn, TableCell>(){

			@Override
			public TableCell call(TableColumn arg0) {
				return new ComboBoxTableCell(stringConverter,list);
			}
			
		};
	}

}
