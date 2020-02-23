package myToolWindow.Nodes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocTagImpl;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import com.jetbrains.php.lang.psi.elements.impl.MethodReferenceImpl;
import myToolWindow.Utils.Resolver;
import myToolWindow.Nodes.Icons.ClassNodes.MethodNode;
import myToolWindow.Nodes.Icons.ClassNodes.RouteNode;
import myToolWindow.Nodes.Icons.ClassNodes.TestNode;
import myToolWindow.Nodes.Icons.FileNodes.PhpFileNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.intellij.testIntegration.TestFinderHelper.isTest;

public class UsageNodeFactory {

    @NotNull
    @Contract("_ -> new")
    public static UsageNode createFileNode(MethodReferenceImpl ref) {
        return new FileNode(new PhpFileNode(), ref);
    }

    @NotNull
    @Contract("_ -> new")
    public static UsageNode createMethodNode(MethodImpl mel) {
        if (isRoute(mel)) {
            return new ClassNode(new RouteNode(), mel);
        } else if (isTest(mel)) {
            return new ClassNode(new TestNode(), mel);
        }
        return new ClassNode(new MethodNode(), mel);
    }

    private static boolean isRoute(MethodImpl mel) {
        return isSymfonyRouteDefinedByAnnotation(mel) || isLaravelRoute(mel);
    }

    private static boolean isSymfonyRouteDefinedByAnnotation(@NotNull MethodImpl mel) {
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
                PsiElement resolved = Resolver.resolveReference(mel);

                if (resolved instanceof Method) {
                    final PhpClass phpClass = ((Method) resolved).getContainingClass();

                    List<String> list = Arrays.asList(
                            "\\Illuminate\\Contracts\\Routing\\Registrar",
                            "\\Illuminate\\Support\\Facades\\Route"
                    );

                    if (phpClass != null && list.contains(phpClass.getFQN())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
