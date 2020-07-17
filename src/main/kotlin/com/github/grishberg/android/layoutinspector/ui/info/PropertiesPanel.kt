package com.github.grishberg.android.layoutinspector.ui.info

import com.android.layoutinspector.model.ViewNode
import com.android.layoutinspector.model.ViewProperty
import com.github.grishberg.android.layoutinspector.domain.MetaRepository
import com.github.grishberg.android.layoutinspector.ui.gropedtables.GroupedTable
import com.github.grishberg.android.layoutinspector.ui.gropedtables.GroupedTableDataModel
import com.github.grishberg.android.layoutinspector.ui.gropedtables.TableRowInfo
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.swing.JComponent
import javax.swing.JScrollPane


/**
 * Shows nodes info.
 */
class PropertiesPanel(
    private val meta: MetaRepository
) {
    private val treeTable = GroupedTable()
    private val scrollPanel = JScrollPane(treeTable)
    private var sizeInDp = false

    fun getComponent(): JComponent = scrollPanel

    fun showProperties(node: ViewNode) {
        treeTable.updateData(TreeTableModel(node))
        scrollPanel.repaint()
        treeTable.repaint()
    }

    fun setSizeDpMode(enabled: Boolean) {
        val shouldInvalidate = sizeInDp != enabled
        sizeInDp = enabled
        if (shouldInvalidate) {
            treeTable.invalidateTable()
        }
    }

    private inner class TreeTableModel(
        private val node: ViewNode
    ) : GroupedTableDataModel {

        private val headers = mutableListOf<String>().apply {
            for (entry in node.groupedProperties) {
                add(entry.key)
            }
        }

        override fun getGroupsCount(): Int = node.groupedProperties.size

        override fun getTableRowInfo(groupIndex: Int): TableRowInfo {
            val key = headers[groupIndex]
            return ParameterModel(node.groupedProperties.getValue(key))
        }

        override fun getGroupName(groupIndex: Int): String = headers[groupIndex]
    }

    private inner class ParameterModel(
        private val properties: List<ViewProperty>
    ) : TableRowInfo {
        override fun getColumnName(col: Int): String {
            if (col == 0) {
                return "name"
            }
            return "value"
        }

        override fun getColumnCount() = 2

        override fun getRowCount() = properties.size

        override fun getValueAt(row: Int, col: Int): String {
            val currentProperty: ViewProperty = properties[row]

            if (col == 0) {
                return currentProperty.name
            }
            if (sizeInDp && currentProperty.isSizeProperty && meta.dpPerPixels > 1) {
                return roundOffDecimal(currentProperty.intValue.toDouble() / meta.dpPerPixels) + " dp"
            }
            return currentProperty.value
        }
    }

    fun roundOffDecimal(number: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number)
    }
}

