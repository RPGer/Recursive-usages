package myToolWindow.Actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import myToolWindow.MyToolWindow;
import org.jetbrains.annotations.NotNull;

public class StopFindUsagesAction extends AnAction {
    public MyToolWindow mtw;
    private boolean enabled = false;

    @SuppressWarnings("unused")
    public StopFindUsagesAction() {
    }

    public StopFindUsagesAction(MyToolWindow tw) {
        super("Stop Building A Tree Of Usages", "Stop building a tree of usages", AllIcons.Actions.Suspend);
        mtw = tw;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(enabled);
        super.update(e);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        mtw.stop();
    }
}
