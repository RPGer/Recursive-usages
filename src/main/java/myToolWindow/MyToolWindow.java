package myToolWindow;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.ui.components.JBScrollPane;
import myToolWindow.Nodes.RouteNode;
import myToolWindow.Nodes.TestNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class MyToolWindow {
    private JPanel myToolWindowContent;

    public MyToolWindow(ToolWindow toolWindow) {
        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode(new RouteNode
                        ("Root",
                                "root.html"));
        createNodes(top);

        Tree tree = new Tree(top);

        MyRenderer renderer = new MyRenderer();
        tree.setCellRenderer(renderer);


        JBScrollPane treeView = new JBScrollPane(tree);

        myToolWindowContent = new JPanel(new BorderLayout());
        myToolWindowContent.add(treeView);


        Project project = (Project) DataManager.getInstance().getDataContext().getData(DataConstants.PROJECT);
        if (project != null) {
            EditorEventMulticaster multicaster = EditorFactory.getInstance().getEventMulticaster();
            multicaster.addCaretListener(new MyCaretListener(), project);
        }


    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

    private void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        category = new DefaultMutableTreeNode(new RouteNode
                ("Books for Java Programmers",
                        "pp.html"));
        top.add(category);

        //original Tutorial
        book = new DefaultMutableTreeNode(new RouteNode
                ("The Java Tutorial: A Short Course on the Basics",
                        "tutorial.html"));
        category.add(book);

        //Tutorial Continued
        book = new DefaultMutableTreeNode(new TestNode
                ("The Java Tutorial Continued: The Rest of the JDK",
                        "tutorialcont.html"));
        category.add(book);

        //JFC Swing Tutorial
        book = new DefaultMutableTreeNode(new RouteNode
                ("The JFC Swing Tutorial: A Guide to Constructing GUIs",
                        "swingtutorial.html"));
        category.add(book);

        //Bloch
        book = new DefaultMutableTreeNode(new TestNode
                ("Effective Java Programming Language Guide",
                        "bloch.html"));
        category.add(book);

        //Arnold/Gosling
        book = new DefaultMutableTreeNode(new RouteNode
                ("The Java Programming Language", "arnold.html"));
        category.add(book);

        //Chan
        book = new DefaultMutableTreeNode(new TestNode
                ("The Java Developers Almanac",
                        "chan.html"));
        category.add(book);

        category = new DefaultMutableTreeNode(new RouteNode
                ("For Imlementors",
                        "vm.html"));
        top.add(category);

        //VM
        book = new DefaultMutableTreeNode(new RouteNode
                ("The Java Virtual Machine Specification",
                        "vm.html"));
        category.add(book);

        //Language Spec
        book = new DefaultMutableTreeNode(new TestNode
                ("The Java Language Specification",
                        "jls.html"));
        category.add(book);
    }
}
