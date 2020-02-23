package myToolWindow.Nodes.Icons.FileNodes;

import com.jetbrains.php.PhpIcons;
import myToolWindow.Nodes.Icons.HasIcon;

import javax.swing.*;

public class PhpFileNode implements HasIcon {
    public PhpFileNode() {
    }

    @Override
    public Icon getIcon() {
        return PhpIcons.PHP_FILE;
    }
}
