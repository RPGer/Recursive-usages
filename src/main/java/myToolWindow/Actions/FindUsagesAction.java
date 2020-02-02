package myToolWindow.Actions;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import myToolWindow.MyToolWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FindUsagesAction extends AnAction {
    public MyToolWindow mtw;

    public FindUsagesAction() {
    }

    public FindUsagesAction(MyToolWindow tw, Icon icon) {
        super(icon);
        mtw = tw;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = (Editor) DataManager.getInstance().getDataContext().getData(DataConstants.EDITOR);

        if (editor != null) {
            CaretModel caretModel = editor.getCaretModel();

            VisualPosition visualPosition = caretModel.getVisualPosition();
            LogicalPosition position = editor.visualToLogicalPosition(visualPosition);

            Document document = editor.getDocument();
            final int offset = editor.logicalPositionToOffset(position);

            Project project = editor.getProject();
            if (project != null) {

                PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(document);
                if (file != null) {

                    MethodImpl mel = PsiTreeUtil.findElementOfClassAtOffset(file, offset, MethodImpl.class, false);
                    if (mel != null) {
                        mtw.createAndRenderTree(mel);
                    }
                }
            }
        }
    }
}
