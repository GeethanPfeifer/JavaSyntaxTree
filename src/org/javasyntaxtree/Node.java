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

import java.util.*;
import java.lang.*;

public class Node {

    /* data associated with node
        value:      value of node
        labelExt:   "external" label of node spec.d by LABEL:VALUE
        labelInt:   internal id of node. Not optional.

    */
    String value;
    String extLabel = null;
    int id;

    Integer parent = null;
    int depth;

    boolean triangular = false;
    boolean terminal = false;

    public Node(int id, Integer parent, int depth, String value, String label) {
        this.id = id;
        this.parent = parent;
        this.depth = depth;
        this.value = value;
        this.extLabel = label;
    }
    public Node(int id, Integer parent, int depth, String value) {
        this.id = id;
        this.parent = parent;
        this.depth = depth;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public int getId() {
        return id;
    }
    public String getLabel() {
        return extLabel;
    }

    public Integer getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isTriangular() {
        return triangular;
    }
    public boolean isTerminal() {
        return terminal;
    }

    public String toString() {
        return "id: " + id +
                ", parent: " + parent +
                ", depth: " + depth +
                ", text: " + value +
                ", label: " + extLabel;

    }





}
