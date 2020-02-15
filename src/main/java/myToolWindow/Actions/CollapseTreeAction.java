package myToolWindow.Actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import myToolWindow.MyToolWindow;
import org.jetbrains.annotations.NotNull;

public class CollapseTreeAction extends AnAction {
    public MyToolWindow mtw;

    public CollapseTreeAction() {
    }

    public CollapseTreeAction(MyToolWindow tw) {
        super("Collapse All", "Collapse all", AllIcons.Actions.Collapseall);
        mtw = tw;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (mtw.tree != null) {
            for (int i = mtw.tree.getRowCount() - 1; i >= 0 ; i--) {
                mtw.tree.collapseRow(i);
            }
        }
    }
}
