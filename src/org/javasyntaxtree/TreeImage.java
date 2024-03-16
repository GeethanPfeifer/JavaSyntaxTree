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

import java.awt.image.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import javax.imageio.*;


public class TreeImage {
    BufferedImage image;

    Integer width;
    Integer height;

    final private Integer borderWidth = 50;
    final private Integer widthBetweenWords = 50;
    final private Integer verticalDistanceBetweenTextAndArrow = 10;
    final private Integer arrowVerticalDistance = 75;
    final private Integer arcVerticalDistance = 50;
    final private Integer arrowHeadLength = 10;
    final private Double arrowHeadDiv = Math.PI / 6;        /* pi/6 radians = 30 degrees */
    final private Integer fontSize = 16;
    final private Font font = new Font("Serif", Font.PLAIN, fontSize);

    /* get font metrics: thanks to https://stackoverflow.com/a/18123024 */
    final private Canvas n = new Canvas();
    final private FontMetrics fontMetrics = n.getFontMetrics(font);


    public TreeImage(Tree T) {
        int i, j;           /* iterators */
        int x = 0;          /* x pos */
        int y = 0;          /* y pos */
        int id, p;          /* id, parent: random local variables */
        int lc;             /* last child */
        int on = 0;         /* offset next */
            /* TEXT */
        int s, ts;          /* total space, text space respectively */
        String text;        /* text */
            /* ARCS */
        int st, en;         /* start, end */
        int xs, xe, ys, ye; /* coords of start & end */
        Vector2D gr;        /* gradient */
        Vector2D tv, tv2;   /* temp 2d vectors */
        int xl, yl;         /* coord of end of line in arrow head */

        ThreePointCurve c;  /* curve defined by three points */


        this.width = getWidth(T);
        this.height = getHeight(T);
        this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);

        /* total space required for each node, by id */
        ArrayList<Integer> totalSpaceRequired = getSpaceRequiredPerNode(T);
        /* text space required */
        ArrayList<Integer> textSpaceRequired = getTextSpacePerNode(T);
        /* depth levels */
        ArrayList<ArrayList<Node>> depthLevels = T.getDepthLevels();
        /* arrows */
        ArrayList<Arc> arrows = T.getArrows();

        /* x-offsets: associated with depthLevels */
        /* ArrayList<ArrayList<Integer>> xOffsets = new ArrayList<ArrayList<Integer>>(); */
        /* populating with 0s */
        /*
        for(i = 0; i < depthLevels.size(); i++) {
            xOffsets.add(new ArrayList<Integer>());
            for(j = 0; j < depthLevels.get(i).size(); j++) {
                xOffsets.get(i).add(0);
            }
        } */


        /* maps ids to their children */
        ArrayList<ArrayList<Integer>> childrenMap = T.buildChildrenMap();

        /* string x-coordinates (centre ) */
        Integer[] stringCentresX = new Integer[T.getNodes().size()];
        Integer[] stringCentresY = new Integer[T.getNodes().size()];
        /* x-offsets: associated with nodes */
        Integer[] xOffsets = new Integer[T.getNodes().size()];
        /* populating with 0s */
        for(i=0; i<xOffsets.length; i++) {
            xOffsets[i] = 0;
        }

        Graphics2D imageGraphics = this.image.createGraphics();
        imageGraphics.setFont(font);
        imageGraphics.setPaint(Color.black);



        /* lower to border */
        y += borderWidth;

        for(i=0; i < depthLevels.size(); i++) {
            y += fontMetrics.getAscent();
            x = borderWidth;


            for(j = 0; j < depthLevels.get(i).size(); j++) {


                id = depthLevels.get(i).get(j).getId();

                x += xOffsets[id];
                ts = textSpaceRequired.get(id);
                s = totalSpaceRequired.get(id);
                text = depthLevels.get(i).get(j).getValue();
                /*text = T.getNodes().get(i).getValue();*/

                /* setting stringCentre (x)*/
                stringCentresX[id] = x + (s/2);
                stringCentresY[id] = y - fontMetrics.getAscent() + (fontMetrics.getHeight() / 2);

                imageGraphics.drawString(text, x + (s - ts) / 2, y);
                x += s + widthBetweenWords;
                /* if has a child, endow offset to first child */
                if(!childrenMap.get(id).isEmpty()) {
                    /* maybe change to += ? I don't think it matters because it should only be updated once. */
                    xOffsets[childrenMap.get(id).get(0)] = on + xOffsets[id];

                    on = 0;
                } else { /* otherwise: endow offset to next child, through on. */
                    on += s + widthBetweenWords + xOffsets[id];

                }

            }

            /* TEMP STUFF: just arrowspace * 2 and arrow */
            y += 2*verticalDistanceBetweenTextAndArrow + arrowVerticalDistance;

        }

                /* ARROWS (edges) */
        for(i = 1; i < T.getNodes().size(); i++) {
            p = T.getNodes().get(i).getParent();

            imageGraphics.drawLine(stringCentresX[p],
                    stringCentresY[p] + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow,
                    stringCentresX[i],
                    stringCentresY[i] - (fontMetrics.getHeight() / 2) - verticalDistanceBetweenTextAndArrow
                    );
        }


                /* Drawing Arcs */
        for(i = 0; i < arrows.size(); i++) {
            st = arrows.get(i).getStart();
            en = arrows.get(i).getEnd();

            xs = stringCentresX[st];
            ys = stringCentresY[st];
            xe = stringCentresX[en];
            ye = stringCentresY[en];

            c = new ThreePointCurve(xs, ys + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow,
                    (xs + xe)/2, (ys + ye)/2 + arrowVerticalDistance,
                    xe, ye + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow);

            imageGraphics.draw(c.getCurve());

            /* arrow heads: default: just t = 1, if bi, then t = 0 too */
            gr = c.gradient(1);
            /* negative to reverse direction */
            tv = gr.normalize().scale(-arrowHeadLength);
            tv2 = tv.rotate(arrowHeadDiv);
            xl = (int) (xe + tv2.getX());
            yl = (int) (ye + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow + tv2.getY());
            imageGraphics.drawLine(xe, ye + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow,
                    xl, yl);
            tv2 = tv.rotate(-arrowHeadDiv);
            xl = (int) (xe + tv2.getX());
            yl = (int) (ye + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow + tv2.getY());
            imageGraphics.drawLine(xe, ye + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow,
                    xl, yl);

            if(arrows.get(i).isBidirectional()) {
                gr = c.gradient(0);
                /* direction is already pointing away from start, no need to reverse*/
                tv = gr.normalize().scale(arrowHeadLength);
                tv2 = tv.rotate(arrowHeadDiv);
                xl = (int) (xs + tv2.getX());
                yl = (int) (ys + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow + tv2.getY());
                imageGraphics.drawLine(xs, ys + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow,
                        xl, yl);
                tv2 = tv.rotate(-arrowHeadDiv);
                xl = (int) (xs + tv2.getX());
                yl = (int) (ys + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow + tv2.getY());
                imageGraphics.drawLine(xs, ys + (fontMetrics.getHeight() / 2) + verticalDistanceBetweenTextAndArrow,
                        xl, yl);
            }


        }


    }

    private ArrayList<Integer> getSpaceRequiredPerNode(Tree T) {
        int p;      /* parent */
        int s;      /* size */




        int i;                                                                      /* iterator */
        ArrayList<Integer> spaceRequired = new ArrayList<Integer>();
        /* initially populating with -1 */
        for(i = 0; i < T.getNodes().size(); i++) {
            spaceRequired.add(-1);
        }

        /* iterating backwards should force children to be evaluated before parents */
        for(i = T.getNodes().size() - 1; i >= 0; i--) {

            s = fontMetrics.stringWidth(T.getNodes().get(i).getValue());

            /* below 0? no children, just set the space required to be as spec.d by fontmetrics
            otherwise, just set it to max of that and the stored length
            */
            if(spaceRequired.get(i) < 0) {
                spaceRequired.set(i, s);
            } else {
                spaceRequired.set(i, Math.max(s, spaceRequired.get(i)));
            }

            /* updating parent: only if i > 0 */
            if(i > 0){
                p = T.getNodes().get(i).getParent();
                if (spaceRequired.get(p) < 0) {
                    spaceRequired.set(p, 0);
                } else {
                    spaceRequired.set(p, spaceRequired.get(p) + widthBetweenWords);
                }
                spaceRequired.set(p, spaceRequired.get(p) + spaceRequired.get(i));

            }
            /*
            System.out.println(i);
            System.out.println(spaceRequired.get(i));
            */

        }
        return spaceRequired;
    }
    private ArrayList<Integer> getTextSpacePerNode(Tree T) {
        /* iterator */
        int i;
        ArrayList<Integer> spaceRequired = new ArrayList<Integer>();
        for(i=0; i<T.getNodes().size(); i++) {
            spaceRequired.add(fontMetrics.stringWidth(T.getNodes().get(i).getValue()));
        }
        return spaceRequired;
    }


    private Integer getWidth(Tree T) {

        return getSpaceRequiredPerNode(T).get(0) + 2 * borderWidth /* + 600 */;

    }
    private Integer getHeight(Tree T) {

        /* TODO: height should be more sophisticated */
        
        if(T.getArrows().isEmpty()) {
            return 2 * borderWidth +
                    /* 300 + */
                    (T.getDepth() + 1) * fontMetrics.getHeight() +
                    (T.getDepth()) * 2 * verticalDistanceBetweenTextAndArrow +
                    (T.getDepth()) *  arrowVerticalDistance;
        } else {
            return 2 * borderWidth +
                    /* 300 + */
                    (T.getDepth() + 1) * fontMetrics.getHeight() +
                    (2 * T.getDepth() + 1) * verticalDistanceBetweenTextAndArrow +
                    (T.getDepth()) * arrowVerticalDistance +
                    arcVerticalDistance;
        }

    }

    public BufferedImage getImage() {
        return image;
    }
}

