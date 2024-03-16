/*
    This file is part of JavaSyntaxTree.
    Copyright (C) 2024 Geethan Pfeifer

    JavaSyntaxTree is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    JavaSyntaxTree is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package org.javasyntaxtree;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.lang.*;
import java.util.*;

public class TreeWindow extends JFrame {
    public TreeWindow(Tree T) {

        BufferedImage image = new TreeImage(T).getImage();
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("test");
        menuBar.add(menu);
        JFrame f = new JFrame();
        f.setJMenuBar(menuBar);
        f.getContentPane().add(new JLabel(new ImageIcon(image)));
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
