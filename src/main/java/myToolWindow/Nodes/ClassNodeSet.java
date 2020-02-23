package myToolWindow.Nodes;

import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class ClassNodeSet {
    final HashSet<ClassNode> set = new HashSet<>();

    public boolean contains(MethodImpl methodImpl) {
        for (ClassNode classNode : set) {
            if (classNode.getElement().equals(methodImpl)) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    public ClassNode find(MethodImpl methodImpl) {
        for (ClassNode classNode : set) {
            if (classNode.getElement().equals(methodImpl)) {
                return classNode;
            }
        }

        return null;
    }

    public void add(ClassNode classNode) {
        set.add(classNode);
    }

    public void clear() {
        set.clear();
    }
}
