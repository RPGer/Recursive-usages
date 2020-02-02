package myToolWindow.Nodes;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocTagImpl;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;

import static com.intellij.testIntegration.TestFinderHelper.isTest;

public class CodeNodeFactory {

    public static CodeNode createNode(MethodImpl mel) {
        if (isRoute(mel)) {
            return new CodeNode(new RouteNode(), mel);
        } else if (isTest(mel)) {
            return new CodeNode(new TestNode(),mel);
        }
        return new CodeNode(new MethodNode(), mel);
    }

    private static boolean isRoute(MethodImpl mel) {
        PsiElement[] annotations = mel.getNode().getTreePrev().getTreePrev().getPsi().getChildren();
        for (PsiElement annotation : annotations) {
            if (annotation instanceof PhpDocTagImpl && ((PhpDocTagImpl) annotation).getName().equals("@Route")) {
                return true;
            }
        }

        return false;
    }
}
