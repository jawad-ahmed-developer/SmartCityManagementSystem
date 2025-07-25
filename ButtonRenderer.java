import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class EditableTableModel extends DefaultTableModel {
    private int editableRow = -1;

    public void setEditableRow(int row) {
        this.editableRow = row;
    }

    public void resetEditableRow() {
        this.editableRow = -1;
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        // Allow editing only for Edit/Delete buttons and the currently editable row
        return column == getColumnCount() - 2   // Edit button column
                || column == getColumnCount() - 1   // Delete button column
                || row == editableRow && column != 0 && column < getColumnCount() - 2;
    }

}