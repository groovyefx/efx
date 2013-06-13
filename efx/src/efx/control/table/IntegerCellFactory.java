package efx.control.table;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

/**
 * 
 * @author lovefree103@gmail.com
 *
 */
@SuppressWarnings("rawtypes")
public class IntegerCellFactory implements Callback<TableColumn,TableCell>{
	
	@Override
	public TableCell call(TableColumn arg0) {
		return new TextFieldTableCell(new IntegerStringConverter());
	}
	
}
