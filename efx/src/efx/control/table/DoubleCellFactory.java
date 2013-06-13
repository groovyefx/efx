package efx.control.table;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 
 * @author lovefree103@gmail.com
 *
 */
@SuppressWarnings("rawtypes")
public class DoubleCellFactory implements Callback<TableColumn,TableCell>{

	private DecimalFormat format;
	
	public DoubleCellFactory(DecimalFormat format) {
		this.format = format;
	}
	
	public DoubleCellFactory() {
		this.format = new DecimalFormat("0.00");
		this.format.setRoundingMode(RoundingMode.DOWN);
	}
	
	@Override
	public TableCell call(TableColumn arg0) {
		return new TextFieldTableCell(new StringConverter<Double>() {

			@Override
			public Double fromString(String arg0) {
				// TODO Auto-generated method stub
				return new Double(arg0);
			}

			@Override
			public String toString(Double arg0) {
				// TODO Auto-generated method stub
				if(arg0==null){
					return null;
				}else{
					return format.format(arg0);
				}
			}
		});
	}
	
}
