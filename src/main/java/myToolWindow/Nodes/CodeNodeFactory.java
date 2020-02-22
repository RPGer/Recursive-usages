package myToolWindow.Nodes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocTagImpl;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import com.jetbrains.php.lang.psi.elements.impl.MethodReferenceImpl;
import com.jetbrains.php.lang.psi.elements.impl.VariableImpl;

import static com.intellij.testIntegration.TestFinderHelper.isTest;

public class CodeNodeFactory {

    public static CodeNode createNode(MethodImpl mel) {
        if (isRoute(mel)) {
            return new CodeNode(new RouteNode(), mel);
        } else if (isTest(mel)) {
            return new CodeNode(new TestNode(), mel);
        }
        return new CodeNode(new MethodNode(), mel);
    }

    private static boolean isRoute(MethodImpl mel) {
        return isSymfonyRouteDefinedByAnnotation(mel) || isLaravelRoute(mel);
    }

    private static boolean isSymfonyRouteDefinedByAnnotation(MethodImpl mel) {
        PsiElement[] annotations = mel.getNode().getTreePrev().getTreePrev().getPsi().getChildren();
        for (PsiElement annotation : annotations) {
            if (annotation instanceof PhpDocTagImpl && ((PhpDocTagImpl) annotation).getName().equals("@Route")) {
                return true;
            }
        }

        return false;
    }

    private static boolean isLaravelRoute(MethodImpl element) {
        Query<PsiReference> q = ReferencesSearch.search(element);

        for (PsiReference r : q) {
            PsiElement el = r.getElement();
            PsiFile file = el.getContainingFile();
            final int offset = el.getTextOffset();

            MethodReferenceImpl mel = PsiTreeUtil.findElementOfClassAtOffset(file, offset, MethodReferenceImpl.class, false);

            if (mel != null) {
                //VariableImpl v = (VariableImpl) el.getFirstChild();
            }
        }

        return false;
    }
}
