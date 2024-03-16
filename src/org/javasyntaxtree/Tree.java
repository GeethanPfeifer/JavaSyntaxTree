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

import java.lang.*;
import java.util.*;
public class Tree {
    ArrayList<Node> nodes;
    ArrayList<Arc> edges;
    ArrayList<Arc> arrows;
    Map<String, Integer> labels;

    Integer depth = 0;


    public Tree(String str) throws Exception {
        ArrayList<Token> tokens;                /* tokens to iterate over */
        int i;                                  /* iterator */
        boolean slbf = false;                   /* left bracket at start flag */
        String tx = null;                       /* general string for storing text */
        boolean sc;                             /* success flag */
        Stack<Integer> ds;                      /* stack of node ids */
        Token tk;                               /* current token */
        int ni = 0;                             /* node iterator */
        boolean pf;                             /* flag for making next node parent */

        nodes = new ArrayList<Node>();
        edges = new ArrayList<Arc>();
        arrows = new ArrayList<Arc>();
        labels = new Hashtable<String, Integer>();
        /*
        Tokenizing the string.
        */
        tokens = Lexer.lex(str);

        /*
        Getting the first node, which will be the root node.
        There must be only zero or one brackets here.
        If there is zero brackets, there must only be one
        */
        sc = false;
        getfirstnode:for(i=0; i<tokens.size(); i++) {
            /*
            switch *should* be safe, see https://docs.oracle.com/javase/8/docs/technotes/guides/language/strings-switch.html
            */
            switch(tokens.get(i).getFunction()) {
                case "left-bracket":
                    if (slbf) {
                        throw new Exception("Unexpected token: left bracket");
                    } else {
                        slbf = true;
                    }
                    break;
                case "whitespace":
                    break;
                case "string":
                    tx = tokens.get(i).getText();
                    sc = true;
                    break getfirstnode;
                default:
                    throw new Exception("Unexpected token");
            }
        }
        if(!sc) {
            throw new Exception("Expected text token.");
        }
        i++;
        if(slbf && i >= tokens.size()) {                /* out of bounds */
            throw new Exception("Expected right bracket.");
        } else if (i >= tokens.size()) {                /* complete, set node, return */
            nodes.add(new Node(0, null, 0, tx));
            return;
        }

        /* checking for label for root node */
        if(tokens.get(i).getFunction().equals("label")) {
            i++;
            if(i >= tokens.size()) {
                throw new Exception("Expected label.");
            }
            if(!(tokens.get(i).getFunction().equals("string"))) { /* invalid token following : */
                throw new Exception("Unexpected token.");
            }
            /*
            makes root with label
            */
            nodes.add(new Node(0, null, 0, tx, tokens.get(i).getText()));
            labels.put(tokens.get(i).getText(), 0);
            if(!slbf) {
                i++;
                if(i != tokens.size()) {
                    throw new Exception("Multiple Expressions.");
                } else {
                    return;
                }
            }
            /* next token */
            i++;
        } else if(!slbf) {
            /* invalid */
            throw new Exception("Multiple expressions.");
        } else {
            /* make boring node */
            nodes.add(new Node(0, null, 0, tx));
        }

        /*
        From now on, we can safely assume that we are expecting a right bracket to end all things.
        */
        ds = new Stack<Integer>();
        ds.push(0);



        /*
        The main loop.
        */
        pf = false;
        for(; !ds.empty() && i < tokens.size() ; i++) {          /* init is i++: starts on next token */
            tk = tokens.get(i);

            switch(tk.getFunction()) {
                case "left-bracket":
                    if(pf) {
                        throw new Exception("Two left brackets in a row.");
                    } else {
                        pf = true;
                    }
                    break;
                case "right-bracket":
                    ds.pop();
                    break;
                case "forward-arc":
                    i++;
                    if(i >= tokens.size()) {
                        throw new Exception("Expected token.");
                    }
                    tk=tokens.get(i);
                    if(tk.getFunction().equals("integer")) {
                        arrows.add(new Arc(ni, Integer.parseInt(tk.getText())));
                    } else if(tk.getFunction().equals("string")){
                        arrows.add(new Arc(ni, tk.getText()));
                    } else {
                        throw new Exception("Invalid token.");
                    }
                    break;
                case "backward-arc":
                    i++;
                    if(i >= tokens.size()) {
                        throw new Exception("Expected token.");
                    }
                    tk=tokens.get(i);
                    if(tk.getFunction().equals("integer")) {
                        arrows.add(new Arc(Integer.parseInt(tk.getText()), ni));
                    } else if(tk.getFunction().equals("string")){
                        arrows.add(new Arc(tk.getText(), ni));
                    } else {
                        throw new Exception("Invalid token.");
                    }
                    break;
                case "bidirectional-arc":
                    i++;
                    if(i >= tokens.size()) {
                        throw new Exception("Expected token.");
                    }
                    tk=tokens.get(i);
                    if(tk.getFunction().equals("integer")) {
                        arrows.add(new Arc(ni, Integer.parseInt(tk.getText()), true));
                    } else if(tk.getFunction().equals("string")){
                        arrows.add(new Arc(ni, tk.getText(), true));
                    } else {
                        throw new Exception("Invalid token.");
                    }
                    break;
                case "label":
                    throw new Exception("Unexpected colon.");
                case "integer":
                    throw new Exception("Unexpected integer. If you want to use an integer as a node, put it in quotation marks.");

                case "whitespace":
                    /* Do nothing */
                    break;
                case "string":

                    ni++;

                    if(i+1 >= tokens.size()) {
                        throw new Exception("Expected more tokens.");
                    }
                    if(tokens.get(i+1).getFunction().equals("label")) {
                        i+=2;
                        if(i >= tokens.size()) {
                            throw new Exception("Expected more tokens.");
                        }
                        if(!(tokens.get(i).getFunction().equals("string"))) {
                            throw new Exception("Expected text.");
                        }
                        nodes.add(new Node(ni, ds.peek(), ds.size(), tk.getText(), tokens.get(i).getText()));
                        edges.add(new Arc(ds.peek(), ni));
                        labels.put(tokens.get(i).getText(), ni);
                        depth=Math.max(depth, ds.size());
                    } else {
                        nodes.add(new Node(ni, ds.peek(), ds.size(), tk.getText()));
                        edges.add(new Arc(ds.peek(), ni));
                        depth =Math.max(depth, ds.size());

                    }
                    if(pf) {
                        ds.push(ni);
                        pf = false;
                    }





                    break;
                default:
                    throw new Exception("unexpected token.");

            }




        }

        /* Accounting for whitespace at end of string */
        for(; ds.empty() && i < tokens.size(); i++) {
            if(tokens.get(i).getFunction().equals("right-bracket")) {
                throw new Exception("Brackets not matched correctly.");
            } else if(!tokens.get(i).getFunction().equals("whitespace")) {
                throw new Exception("Unexpected token.");
            }
        }


        if(ds.empty() && i == tokens.size()) {      /* success! all is well. replacing label names with ints in arcs, and return */
            for(i = 0; i<arrows.size(); i++) {
                arrows.get(i).updateArc(labels);
            }

            return;
        }
        /* failure: throw exception */
        throw new Exception("Brackets not matched correctly.");

    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }
    public ArrayList<Arc> getEdges() {
        return edges;
    }
    public ArrayList<Arc> getArrows() {
        return arrows;
    }

    public Integer getDepth() {
        return depth;
    }

    public ArrayList<ArrayList<Node>> getDepthLevels() {
        ArrayList<ArrayList<Node>> depthLevels;
        int i;                                          /* iterator */
        Node n;                                         /* temp node */

        depthLevels = new ArrayList<ArrayList<Node>>();

        /* populating depthLevels with (initially empty) depth levels */
        for(i=0; i <= this.getDepth(); i++) {
            depthLevels.add(new ArrayList<Node>());
        }

        /* iterating over nodes */
        for(i=0; i < this.getNodes().size(); i++) {
            n = this.getNodes().get(i);

            depthLevels.get(n.getDepth()).add(n);

        }
        return depthLevels;
    }



    public ArrayList<ArrayList<Integer>> buildChildrenMap() {
        ArrayList<ArrayList<Integer>> childrenMap = new ArrayList<ArrayList<Integer>>();
        int i;
        /* populating with empty lists */
        for(i=0; i<nodes.size(); i++) {
            childrenMap.add(new ArrayList<Integer>());
        }

        for(i = 1; i < nodes.size(); i++) {
            childrenMap.get(nodes.get(i).getParent()).add(nodes.get(i).getId());
        }
        return childrenMap;
    }

    public String toString() {
        int i;
        String x = "";
        for(i = 0; i<this.nodes.size(); i++) {
            x += this.nodes.get(i).toString() + ";" ;
        }
        return x;
    }



}
