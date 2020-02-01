package myToolWindow;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import org.jetbrains.annotations.NotNull;

public class MyCaretListener implements CaretListener {
    public MyToolWindow mtw;

    public MyCaretListener(MyToolWindow tw) {
        mtw = tw;
    }

    public void caretPositionChanged(@NotNull CaretEvent e) {
        LogicalPosition position = e.getNewPosition();
        Editor editor = e.getEditor();
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


//    private void test(){
//        final List<PsiFile> psiRoots = file.getViewProvider().getAllFiles();
//        for (PsiElement root : psiRoots) {
////                    if (root.getLanguage() == Language.findLanguageByID("PHP")) {
////
////                    }
//            PsiElement elementAt = root.findElementAt(offset);
//
//            if (elementAt != null) {
//                PsiElement methodElement = findParentByType(elementAt, "com.jetbrains.php.lang.psi.elements.impl.MethodImpl");
//
//                MethodImpl methodImpl = (MethodImpl) methodElement;
//                if (methodImpl != null){
//                    String methodName = methodImpl.getName();
//                    System.err.println(methodName);
//                }
//
//            }
//
//
////                        PhpMethodElementType
////                    ((LeafPsiElement) elementAt).getElementType();
////                    ReferencesSearch.search()
////                    Function el = PsiTreeUtil.findElementOfClassAtOffset(file, offset, Function.class, false);
//
//        }
//    }

//    @Nullable
//    private PsiElement findParentByType(@NotNull PsiElement element, String className) {
//        if (element.getClass().getName().equals(className)){
//            return element;
//        } else {
//            PsiElement parent = element.getParent();
//            if (parent != null) {
//                return findParentByType(parent, className);
//            } else {
//                return null;
//            }
//        }
//    }

    public void caretAdded(@NotNull CaretEvent event) {
    }

    public void caretRemoved(@NotNull CaretEvent event) {
    }
}
