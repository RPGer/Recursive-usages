package myToolWindow.Nodes;

import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class CodeNodeSet {
    HashSet<CodeNode> set = new HashSet<>();

    public boolean contains(MethodImpl methodImpl){
        for (CodeNode codeNode: set){
            if (codeNode.getMethodImpl().equals(methodImpl)){
                return true;
            }
        }

        return false;
    }

    @Nullable
    public CodeNode find(MethodImpl methodImpl){
        for (CodeNode codeNode: set){
            if (codeNode.getMethodImpl().equals(methodImpl)){
                return codeNode;
            }
        }

        return null;
    }

    public void add(CodeNode codeNode){
        set.add(codeNode);
    }

    public void clear(){
        set.clear();
    }
}
