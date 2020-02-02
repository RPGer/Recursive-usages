package myToolWindow.Nodes;

import com.intellij.icons.AllIcons;

import javax.swing.*;

public class RouteNode implements HasIcon {
    public RouteNode() {
    }

    @Override
    public Icon getIcon() {
        return AllIcons.Nodes.Enum;
    }
}
