package efx.control.table;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

/**
 * 
 * @author lovefree103@gmail.com
 *
 */
@SuppressWarnings("rawtypes")
public class CheckBoxCellFactory implements Callback<TableColumn, TableCell> {

	@Override
	public TableCell call(TableColumn arg0) {
		// TODO Auto-generated method stub
		return new CheckBoxTableCell();
	}

}
