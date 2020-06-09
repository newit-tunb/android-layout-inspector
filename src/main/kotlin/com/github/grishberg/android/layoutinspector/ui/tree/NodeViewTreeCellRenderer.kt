package com.github.grishberg.android.layoutinspector.ui.tree

import com.android.layoutinspector.model.ViewNode
import com.github.grishberg.android.layoutinspector.ui.theme.ThemeColors
import java.awt.Component
import javax.swing.ImageIcon
import javax.swing.JTree
import javax.swing.tree.TreeCellRenderer


class NodeViewTreeCellRenderer(
    private val foundItems: List<ViewNode>,
    theme: ThemeColors
) : TreeCellRenderer {
    var hoveredNode: ViewNode? = null
    private val iconsStore = IconsStore()

    private val textIcon = iconsStore.createImageIcon("icons/text.png")
    private val fabIcon = iconsStore.createImageIcon("icons/fab.png")
    private val appBarIcon = iconsStore.createImageIcon("icons/appbar.png")
    private val coordinatorLayoutIcon = iconsStore.createImageIcon("icons/coordinator_layout.png")
    private val constraintLayoutIcon = iconsStore.createImageIcon("icons/constraint_layout.png")
    private val frameLayoutIcon = iconsStore.createImageIcon("icons/frame_layout.png")
    private val linearLayoutIcon = iconsStore.createImageIcon("icons/linear_layout.png")
    private val cardViewIcon = iconsStore.createImageIcon("icons/cardView.png")
    private val viewStubIcon = iconsStore.createImageIcon("icons/viewstub.png")
    private val toolbarIcon = iconsStore.createImageIcon("icons/toolbar.png")
    private val listViewIcon = iconsStore.createImageIcon("icons/recyclerView.png")
    private val relativeLayoutIcon = iconsStore.createImageIcon("icons/relativeLayout.png")
    private val imageViewIcon = iconsStore.createImageIcon("icons/imageView.png")
    private val nestedScrollViewIcon = iconsStore.createImageIcon("icons/nestedScrollView.png")
    private val viewSwitcherIcon = iconsStore.createImageIcon("icons/viewSwitcher.png")
    private val viewPagerIcon = iconsStore.createImageIcon("icons/viewPager.png")
    private val viewIcon = iconsStore.createImageIcon("icons/view.png")

    private val text1Foreground =
        TextForegroundColor(
            theme.text1ForegroundColor,
            theme.selectionForeground1,
            theme.hiddenText1Color,
            theme.selectionHiddenText1Color,
            theme.hoveredText1Color,
            theme.selectionHoveredText1Color,
            theme.foundTextColor,
            theme.selectedFoundTextColor
        )
    private val text2Foreground =
        TextForegroundColor(
            theme.text2ForegroundColor,
            theme.selectionForeground2,
            theme.hiddenText2Color,
            theme.selectionHiddenText2Color,
            theme.hoveredText2Color,
            theme.selectionHoveredText2Color,
            theme.foundTextColor,
            theme.selectedFoundTextColor
        )

    private val textViewRenderer = TextViewRenderer(textIcon, theme)
    private val defaultCellRenderer = SimpleViewNodeRenderer()

    override fun getTreeCellRendererComponent(
        tree: JTree?,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ): Component {
        defaultCellRenderer.getTreeCellRendererComponent(
            tree,
            value,
            selected,
            expanded,
            leaf,
            row,
            hasFocus
        )
        if (value !is ViewNode) {
            return defaultCellRenderer
        }
        val hovered = value == hoveredNode
        val visible = value.isDrawn
        val text = value.getText()
        val highlighted = foundItems.contains(value)

        val isTextView = text != null
        val item: TreeItem = if (isTextView) {
            textViewRenderer
        } else {
            defaultCellRenderer
        }

        var foreground1 = text1Foreground.textForeground(selected, hovered, highlighted, visible)
        var foreground2 = text2Foreground.textForeground(selected, hovered, highlighted, visible)
        item.setForeground(foreground1, foreground2)

        if (text != null) {
            item.setTitle(value.typeAsString(), value.getElliptizedText(text))
            textViewRenderer.selected = selected
            textViewRenderer.hovered = hovered
            return textViewRenderer
        }
        item.setTitle(value.getFormattedName())
        item.setIcon(iconForNode(value))
        return defaultCellRenderer
    }

    private fun iconForNode(node: ViewNode): ImageIcon {
        val nodeTypeShort = node.typeAsString()

        if (node.name == "android.view.ViewStub") {
            return viewStubIcon
        }

        if (node.name == "android.widget.FrameLayout" || node.name == "androidx.appcompat.widget.ContentFrameLayout" ||
            node.name == "androidx.appcompat.widget.FitWindowsFrameLayout"
        ) {
            return frameLayoutIcon
        }

        if (nodeTypeShort == "AppBarLayout" || node.name == "com.android.internal.widget.ActionBarContainer") {
            return appBarIcon
        }

        if (nodeTypeShort == "ConstraintLayout") {
            return constraintLayoutIcon
        }

        if (nodeTypeShort == "CollapsingToolbarLayout" || nodeTypeShort == "Toolbar") {
            return toolbarIcon
        }

        if (nodeTypeShort == "CoordinatorLayout") {
            return coordinatorLayoutIcon
        }

        if (nodeTypeShort == "AppCompatImageButton" || nodeTypeShort == "ImageButton" || nodeTypeShort == "ImageView") {
            return imageViewIcon
        }

        if (nodeTypeShort == "ViewSwitcher") {
            return viewSwitcherIcon
        }

        if (nodeTypeShort == "NestedScrollView") {
            return nestedScrollViewIcon
        }

        if (nodeTypeShort.contains("RecyclerView")) {
            return listViewIcon
        }

        if (nodeTypeShort.contains("RelativeLayout")) {
            return relativeLayoutIcon
        }

        if (nodeTypeShort.endsWith("CardView")) {
            return cardViewIcon
        }
        if (nodeTypeShort.endsWith("ViewPager")) {
            return viewPagerIcon
        }
        if (nodeTypeShort == "FloatingActionButton") {
            return fabIcon
        }
        if (nodeTypeShort.endsWith("LinearLayout")) {
            return linearLayoutIcon
        }
        return viewIcon
    }

}
