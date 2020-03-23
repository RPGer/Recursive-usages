package myToolWindow.Utils;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Resolver {
    @Nullable
    static public PsiElement resolveReference(@NotNull MethodReference methodReference, ProgressIndicator indicator) {
        PsiElement result = null;
        try {
            final ResolveResult[] resolved = methodReference.multiResolve(false);
            if (resolved.length > 0) {
                if (resolved.length == 1) {
                    // case: one option only; just get it
                    result = resolved[0].getElement();
                } else {
                    // case: multiple options; get rid of duplicates and narrow to the "lowest" child
                    final Map<String, Method> methods = new LinkedHashMap<>();
                    for (final ResolveResult value : resolved) {
                        indicator.checkCanceled();
                        final PsiElement element = value.getElement();
                        if (element instanceof Method) {
                            methods.put(((Method) element).getFQN(), (Method) element);
                        }
                    }
                    if (methods.size() == 1) {
                        // doubled declarations eliminated; get the one left
                        result = methods.values().iterator().next();
                    } else {
                        // try narrowing down to a child class
                        final Set<String> remaining = new HashSet<>(methods.keySet());
                        for (final Method method : methods.values()) {
                            indicator.checkCanceled();
                            if (remaining.contains(method.getFQN())) {
                                final PhpClass phpClass = method.getContainingClass();
                                if (phpClass != null) {
                                    final PhpClass parent = phpClass.getSuperClass();
                                    if (parent != null) {
                                        final Method parentMethod = parent.findMethodByName(method.getName());
                                        if (parentMethod != null) {
                                            remaining.remove(parentMethod.getFQN());
                                        }
                                    }
                                }
                            }
                        }
                        // if not narrowed, do fallback
                        result = remaining.size() == 1 ? methods.get(remaining.iterator().next()) : methodReference.resolve();
                        remaining.clear();
                    }
                    methods.clear();
                }
            }
            return result;
        } catch (final Throwable error) {
            if (error instanceof ProcessCanceledException) {
                throw error;
            }
            return null;
        }
    }
}
