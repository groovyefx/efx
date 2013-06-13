package efx.control.table;

import javafx.util.StringConverter;

/**
 * 
 * @author lovefree103@gmail.com
 *
 */
@SuppressWarnings("rawtypes")
public class TextFieldTableCell extends
		javafx.scene.control.cell.TextFieldTableCell {
	
	public TextFieldTableCell() {
	}
	
	
	public TextFieldTableCell(StringConverter stringConverter) {
		super(stringConverter);
	}
	
	@Override
	public void updateItem(Object item, boolean empty) {
		super.updateItem(item, false);
	}
}
