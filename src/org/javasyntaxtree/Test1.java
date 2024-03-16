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
import java.lang.*;
import java.util.*;


public class Test1 {
    public static void main(String args[]) {
        Tree t;


        /* try { t = new Tree("[a:X [cd b ->X][e ->4]]"); } */
        /* try { t = new Tree("[S [NP [PN Anna:X]] [VP [V Loves] [NP [PN Bob ->X]]]]"); } */
        /* try { t = new Tree("[S [A [B [D Joe]] C]]"); } */
        try {t = new Tree("[S [A B:X] [C D <>X]]");}
        /* try {t = new Tree("[S]");} */
        /* try {t = new Tree("[S:X ->X]");} */
        catch(Exception e){System.out.println(e);return;}

        /* System.out.println(t.toString()); */
        /*
        System.out.println(t.arrows.get(1).start);
        */
        TreeImage ti = new TreeImage(t);

        /*losely thanks to:
        https://stackoverflow.com/a/9215300
        */
        ImageWindow x = new ImageWindow(ti.getImage());


    }


}
